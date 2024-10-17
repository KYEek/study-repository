package jdbc.day04.board.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jdbc.day04.board.domain.BoardDTO;
import jdbc.day04.member.domain.MemberDTO;

public class BoardDAO_imple implements BoardDAO {
	
	
	
	
	// field, attribute, property
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	
	
	// method, operation, 기능
	
	// 자원반납을 해주는 메소드 //
	private void close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	//		게시판에 글을 쓰는 메소드		//
	// === Transaction 처리 ===
	//    (tbl_board 테이블에 insert 가 성공되어지면 tbl_member 테이블의 point 컬럼에 10씩 증가 update 를 할 것이다.
	//     그런데 insert 또는 update 가 하나라도 실패하면 모두 rollback 할 것이고,
	//     insert 와 update 가 모두 성공해야만 commit 할 것이다.)
	
	// 게시판 글쓰기 Transaction 처리하여 성공되어지면 1을 리턴시켜 줄 것이고,
	// 장애(오류)가 발생되어 실패하면 -1 리턴시켜 줄 것이다
	@Override
	public int write(BoardDTO bdto) {
		
		int result = 0;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");

			// Transaction 처리를 위해서 수동 commit 으로 전환 시킨다.
			conn.setAutoCommit(false);
			
			String sql = " insert into tbl_board(boardno, fk_userid, subject, contents, boardpasswd) "
					+ " values(seq_board.nextval, ?, ?, ? ,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bdto.getFk_userid());
			pstmt.setString(2, bdto.getSubject());
			pstmt.setString(3, bdto.getContents());
			pstmt.setString(4, bdto.getBoardpasswd());

			int n1 = pstmt.executeUpdate();

			if( n1 == 1) {	// tbl_board 테이블에 insert가 성공되었다라면
				sql = " update tbl_member set point = point + 10 "
					+ " where userid = ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bdto.getFk_userid());
				
				int n2 = pstmt.executeUpdate();
				
				if(n2 == 1) {
					conn.commit();
					result = 1;
				}
				
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		} catch (SQLException e) {
			if(e.getErrorCode()==2290) {
				System.out.println(">> 아이디 " +bdto.getFk_userid()+ "님의 포인트는 30을 초과할 수 없기 때문에 오류발생 하였습니다. <<");
			}
			else {
				e.printStackTrace();
			}
			try {
				conn.rollback();
				result = -1;
			}
			catch (SQLException e1) {	}
			
		} finally {
			close();
		}

		return result;
	}//end of write ---------------------------

	//			글 목록 보기			//
	@Override
	public List<BoardDTO> boardList() {
		
		List<BoardDTO> boardList = new ArrayList<>();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");

			String sql = " select boardno "
					+ " , case when length(subject) > 15 then substr(subject, 1, 13)||'..' else subject end subject "
					+ " , name, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') writeday, viewcount "
					+ " from tbl_board B join tbl_member M "
					+ " on B.fk_userid = M.userid "
					+ " order by boardno desc ";
			

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				
				BoardDTO board = new BoardDTO();
				
				board.setBoardno(rs.getInt("boardno"));
				board.setSubject(rs.getString("subject"));
				MemberDTO member = new MemberDTO();
				
				member.setName(rs.getString("name"));
				board.setMbrdto(member);
				
				board.setWriteday(rs.getString("writeday"));
				board.setViewcount(rs.getInt("viewcount"));
				
				boardList.add(board);
			}

		} catch (ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return boardList;
	}


	// 글 1개 내용 보기
	// == 현재 로그인 사용자가 자신이 쓴 글을 볼때는 조회수 증가가 없지만
	//    다른 사용자가 쓴 글을 볼때는 조회수를 1증가 해주어야 한다.
	@Override
	public BoardDTO viewContents(Map<String, String> paraMap) {
		
		BoardDTO bdto = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");

			String sql = " select subject, contents, name, viewcount, fk_userid "
					+ " from  "
					+ " ( "
					+ "     select subject, contents, viewcount, fk_userid "
					+ "     from  tbl_board "
					+ "     where boardno = ? "
					+ " ) B join tbl_member M "
					+ " on B.fk_userid = M.userid ";
			

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("boardNo"));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bdto = new BoardDTO();
				
				bdto.setSubject(rs.getString("subject"));
				bdto.setContents(rs.getString("contents"));
				
				MemberDTO mbrdto = new MemberDTO();
				mbrdto.setName(rs.getString("name"));
				bdto.setViewcount(rs.getInt("viewcount"));
				//————————————————————————————————
				
				//로그인한 사용자가 다른 사용자가 쓴 글을 조회할 경우에만 글 조회수 1증가 시켜야 한다.
				if(!paraMap.get("login_userid").equals(rs.getString("fk_userid"))); {
					sql = " update tbl_board set viewcount = viewcount + 1 "
							+ "where boardno = ? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, paraMap.get("boardNo"));
					
					pstmt.executeUpdate();
					
					bdto.setViewcount(bdto.getViewcount() + 1);
					
				}//end of if————————————————————————————————
				
			}

		} catch (ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		} catch (SQLException e) {
			if(e.getErrorCode()==1722) {
				System.out.println(">> [경고] 글번호는 정수만 가능합니다. << \n");
			}
			else {
				e.printStackTrace();
			}
			
			
		} finally {
			close();
		}
		
		
		
		return bdto;
	}// end of viewContents————————————————————————————————————————
			
			
			
			
			
			
			
			
			
			
			
}
