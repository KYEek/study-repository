package jdbc.day04.board.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jdbc.day04.board.domain.BoardDTO;

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
		
		
		return null;
	}
			
			
			
			
			
			
			
			
			
			
			
}
