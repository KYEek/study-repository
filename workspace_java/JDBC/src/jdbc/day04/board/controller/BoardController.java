package jdbc.day04.board.controller;

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
		
		String add_menu = "admin".equals(member.getUserid())?"7.최근1주일간 일자별 게시글 작성건수 \n8.이번달 일자별 게시글 작성건수   9.나가기\n":"7.나가기 \n";
		
		System.out.println("\n--------------게시판 메뉴 [" + member.getName() + "님 로그인중..]------------------\n"
						+ "1.글목록보기   2.글내용보기   3.글쓰기   4.댓글쓰기 \n"
						+ "5.글수정하기   6.글삭제하기   " + add_menu);
		
		System.out.print("🤚메뉴번호 선택 : ");
		String s_menuNo = sc.nextLine();
		
		switch (s_menuNo) {
		case "1":	//글목록보기
			//boardList();
			break;
		case "2":	//글내용보기
			//viewContents(member.getUserid(), sc);
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

			break;
		case "8":	//관리자 일 때 이번달 일자별 게시글 작성건수

			break;
		case "9":	//admin 일때 나가기

			break;
		default:
			System.out.println(">> 메뉴에 없는 번호 입니다. <<");
			
			break;
		}// end of switch --------------------------------------
		
	}

	
	// *** 글쓰기를 해주는 메소드 *** //
	// === Transaction 처리 ===
	//    (tbl_board 테이블에 insert 가 성공되어지면 tbl_member 테이블의 point 컬럼에 10씩 증가 update 를 할 것이다.
	//     그런데 insert 또는 update 가 하나라도 실패하면 모두 rollback 할 것이고,
	//     insert 와 update 가 모두 성공해야만 commit 할 것이다.)
	private int write(MemberDTO member, Scanner sc) {
		
		int result = 0;
	
		System.out.print("⌂⌂⌂⌂⌂글쓰기⌂⌂⌂⌂⌂");
		
		System.out.println("1. 작성자명 : " + member.getName());
		
		System.out.println("2. 글제목 [최대 100글자] : " + member.getName());
		String subject = sc.nextLine();
		
		System.out.println("3. 글내용 [최대 200글자] : " + member.getName());
		String contents = sc.nextLine();
		
		System.out.println("4. 글암호 [최대 20글자] : " + member.getName());
		String boardpasswd = sc.nextLine();
		
		
		BoardDTO bdto = new BoardDTO();
		bdto.setFk_userid(member.getName());
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
	
	
	
	
}