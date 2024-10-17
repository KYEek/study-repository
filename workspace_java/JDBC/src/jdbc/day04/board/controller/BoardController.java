package jdbc.day04.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jdbc.day04.board.domain.BoardDTO;
import jdbc.day04.member.domain.MemberDTO;
import jdbc.day04.board.model.*;

public class BoardController {
	
	
	
	//field
	BoardDAO bdao = new BoardDAO_imple();
	
	
	
	//method
	//			게시판 메뉴	를 보여주는 메소드		//
	public void menu_Board(MemberDTO member, Scanner sc) {
		//———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
		boolean isExit= false;
		do {
			String add_menu = "admin".equals(member.getUserid())?"7.최근1주일간 일자별 게시글 작성건수 \n8.이번달 일자별 게시글 작성건수   9.나가기\n":"7.나가기 \n";
			
			System.out.println("\n--------------게시판 메뉴 [" + member.getName() + "님 로그인중..]------------------\n"
							+ "1.글목록보기   2.글내용보기   3.글쓰기   4.댓글쓰기 \n"
							+ "5.글수정하기   6.글삭제하기   " + add_menu);
			
			System.out.print("🤚메뉴번호 선택 : ");
			String s_menuNo = sc.nextLine();
			
			switch (s_menuNo) {
			case "1":	//글목록보기
				boardList();
				break;
			case "2":	//글내용보기
				viewContents(member.getUserid(), sc);
				break;
			case "3":	//글쓰기
				int n = write(member, sc);
				if(n==1)
					System.out.println(">> 글쓰기 성공!! <<");
				else if(n==0)
					System.out.println(">> 글쓰기 취소!! <<");
				else if(n== -1)
					System.out.println(">> 글쓰기 실패!! <<");
				
				break;
			case "4":	//댓글쓰기
				
				break;
			case "5":	//글수정하기
	
				break;
			case "6":	//글삭제하기
	
				break;
			case "7":	//관리자(최근1주일간 일자별 게시글 작성건수) 유저(나가기)
				if(!"admin".equals(member.getUserid())) 
					isExit = true;
				else {
					
				}
				break;
			case "8":	//관리자 일 때 이번달 일자별 게시글 작성건수
				if("admin".equals(member.getUserid())) {
					break;
				}
			case "9":	//admin 일때 나가기
				if("admin".equals(member.getUserid())) {
					isExit = true;
					break;
				}
			default:
				System.out.println(">> 메뉴에 없는 번호 입니다. <<");
				
				break;
			}// end of switch --------------------------------------
		}while (!isExit);
		//———————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————————
	}

	
	





	// *** 글쓰기를 해주는 메소드 *** //
	// === Transaction 처리 ===
	//    (tbl_board 테이블에 insert 가 성공되어지면 tbl_member 테이블의 point 컬럼에 10씩 증가 update 를 할 것이다.
	//     그런데 insert 또는 update 가 하나라도 실패하면 모두 rollback 할 것이고,
	//     insert 와 update 가 모두 성공해야만 commit 할 것이다.)
	private int write(MemberDTO member, Scanner sc) {
		
		int result = 0;
	
		System.out.println("⌂⌂⌂⌂⌂글쓰기⌂⌂⌂⌂⌂");
		
		System.out.println("1. 작성자명 : " + member.getName());
		
		System.out.print("2. 글제목 [최대 100글자] : " );
		String subject = sc.nextLine();
		
		System.out.print("3. 글내용 [최대 200글자] : " );
		String contents = sc.nextLine();
		
		System.out.print("4. 글암호 [최대 20글자] : " );
		String boardpasswd = sc.nextLine();
		
		
		BoardDTO bdto = new BoardDTO();
		bdto.setFk_userid(member.getUserid());
		bdto.setSubject(subject);
		bdto.setContents(contents);
		bdto.setBoardpasswd(boardpasswd);
		
		do {
			System.out.print(" 정말로 글쓰기를 하시겠습니까?[Y/N] => ");
			String yn = sc.nextLine();
			
			if("y".equalsIgnoreCase(yn)) {
				int subject_length = subject.length();
				int contents_length = contents.length();
				int boardpasswd_length = boardpasswd.length();
				
				if((1<=subject_length && subject_length <=100) && (1<=contents_length && contents_length <=200) && (1<=boardpasswd_length && boardpasswd_length <=20))
				{
					result = bdao.write(bdto);
				}
				else {
					System.out.println(">> 입력한 데이터가 너무 크므로 입력이 불가합니다. <<");
				}
				
				break;
			}
			else if("n".equalsIgnoreCase(yn)) {
				break;
			}
			else {
				System.out.println("Y 또는 N 만 입력하세요!! 喝喝喝");
			}
		} while (true);
		return result;
	}
	
	
	//		글목록보기 해주는 메소드		//
	private void boardList() {
		
		List<BoardDTO> boardList = bdao.boardList();
		
		if(boardList.size() > 0) {
			//게시글이 존재 하는 경우
			StringBuilder sb = new StringBuilder();
			
			sb.append("\n" + "-".repeat(30) + " [게시글 목록] " + "-".repeat(35) + "\n");
			sb.append("글번호\t글제목\t\t작성자\t작성일자\t\t\t\t조회수\n");
			sb.append("-".repeat(75) +"\n");
			
			for(int i = 0; i<boardList.size(); i++) {
				sb.append(boardList.get(i).boardInfo() + "\n");
						//boardList.get(i) 는 BoardDTO 이다
			}//end of for-----------------
			
			System.out.println(sb);
		}
		else {
			//게시글이 존재 하지 않는 경우
			System.out.println("글 목록이 없어ㅡㅡ");
		}
		
		
	}//end of private void boardList() -----------------------------------
	
	
	
	//	글 내용보기 해주는 메소드			//
	// == 현재 로그인 사용자가 자신이 쓴 글을 볼때는 조회수 증가가 없지만
	//    다른 사용자가 쓴 글을 볼때는 조회수를 1증가 해주어야 한다.
	private void viewContents(String login_userid, Scanner sc) {
		
		
		System.out.println("\n>>> 글내용 보기 <<< ");
		
		System.out.print("🤚 글번호 : ");
		String boardNo = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("login_userid", login_userid);
		paraMap.put("boardNo", boardNo);
		
		BoardDTO bdto = bdao.viewContents(paraMap);
		
		if(bdto != null) {
			// 존재하는 글번호를 입력한 경우
			System.out.println("[글제목] " + bdto.getSubject() + "\n"
							+ "[글내용] " + bdto.getContents() + "\n"
							+ "[작성자] " + bdto.getMbrdto().getName() + "\n"
							+ "[조회수] " + bdto.getViewcount());
		}
		else {
			// 존재하지 않는 글번호 또는 글번호를 숫자가 아닌 문자로 입력한 경우
			System.out.println(">> 글번호 " + boardNo + "은(는) 글 목록에 존재하지 않습니다. << \n");
		}
		
	}//end of viewContents————————————————————————————————
	
	
	
}
