package jdbc.day03;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MemberCtrl {

	// field, arrtibute, property, 속성
	
	//method, operation, 기능
	MemberDAO mdao = new MemberDAO_imple();
	
	//		시작 메뉴를 보여주는 메소드(기능)			//
	public void menu_Start(Scanner sc) {
		
		
		
		
		
		String s_Choice = "";
		boolean isSuccess_Login = false;
		MemberDTO member = null;
		
		
		
		do {
			// 로그인 하기전
			if(isSuccess_Login == false) {
				do {
					System.out.println("\n⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙시작메뉴⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙ \n"
									+ "1. 회원가입  2. 로그인  3. 프로그램 종료 \n"
									+ "⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙⁙");
					
					System.out.print("메뉴번호 선택 : ");
					s_Choice = sc.nextLine();
		
					switch (s_Choice) {
					case "1": // 회원가입
						memberRegister(sc);
						break;
					case "2": // 로그인
						member = login(sc);
						if(member != null)
							isSuccess_Login = true;
						break;
					case "3": // 프로그램 종료
		
						return;	//menu_Start(Scanner sc) 이 메서드를 종료함
					default:
						System.out.println("😣 메뉴에 없는 번호 입니다. 다시 선택하세요!!");
						break;
					}
				} while (!("2".equals(s_Choice) && isSuccess_Login == true));		
			}// end of if-------------------------------
			
			
			
			// 로그인 한 후
			if(isSuccess_Login = true) {
				String admin_menu = "admin".equals(member.getUserid()) ? "4. 모든회원조회" : "";
				String bar = "admin".equals(member.getUserid()) ?"⁙".repeat(66):"⁙".repeat(48);
				
				System.out.println("\n⁙⁙⁙⁙⁙시작메뉴 [" +member.getName() +"]님 로그인 중...⁙⁙⁙⁙⁙⁙ \n"
						+ "1. 로그아웃  2. 회원탈퇴  3. 나의정보보기  " + admin_menu + " \n"
						+ bar);
		
				System.out.print("메뉴번호 선택 : ");
				s_Choice = sc.nextLine();
				
				
				switch (s_Choice) {
				case "1":	//로그아웃
					member = null;
					isSuccess_Login = false;
					System.out.println("로그아웃 되었습니다 O(∩_∩)O");
					break;
				case "2":	//회원탈퇴
					String yn = null;
					
					
					do {
						System.out.print(" /_ \\ 정말로 탈퇴 하시겠습니까? [Y/N] : ");
						yn = sc.nextLine();
						
						if("y".equalsIgnoreCase(yn)) {
							mdao.memberDelete(member.getUserseq());
						}
						else if("n".equalsIgnoreCase(yn)) {
							
						}
						
						
					} while(!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
					//----------end of while----------------------
					
					break;
				case "3":	//나의정보조회
		
					break;
				case "4":	//admin 으로 로그인 시 모든 회원 조회, 일반회원으로 로그인 시 메뉴에 없는 번호로 표시
					if("admin".equals(member.getUserid())) {
						System.out.println("모든 회원들의 정보를 보여주겠습니다🙇");
						break;
					}
				default:
					System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요!! <<<");
					break;
				}
			}//end of if---------------------------------------------
			
		} while (true);
		//end of while---------------------------------------------
		
	}
	
	
	
	//		회원가입을 해주는 메소드 		//
	private void memberRegister(Scanner sc) {
		
		System.out.println("\n 😊 회원가입 😊");
		System.out.print("1. 아이디 : ");
		String userid = sc.nextLine();
		
		System.out.print("2. 비밀번호 : ");
		String passwd = sc.nextLine();
		
		System.out.print("3. 회원명 : ");
		String name = sc.nextLine();
		
		System.out.print("4. 연락처(휴대폰) : ");
		String mobile = sc.nextLine();
		
		MemberDTO member = new MemberDTO();
		member.setUserid(userid);
		member.setPasswd(passwd);
		member.setName(name);
		member.setMobile(mobile);
		
		int n = mdao.memberRegister(member);

		if(n==1) {
			System.out.println("\n회원가입을 축하드립니다. (((o(*ﾟ▽ﾟ*)o)))\n");
		}
		else {
			System.out.println("\n😭회원가입이 실패되었습니다.");
		}
		
		
	}//------------------------end of memberRegister()


	//	로그인 시도하기		//
	private MemberDTO login(Scanner sc) {
	
		System.out.println("\n —————————로그인—————————");
		
		System.out.print("🫵 아이디 : ");
		String userid = sc.nextLine();
		System.out.print("🫵 비밀번호 : ");
		String passwd = sc.nextLine();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", userid);
		paraMap.put("passwd", passwd);
		
		MemberDTO member = mdao.login(paraMap);
		
		if(member != null ) {
			System.out.println("로그인 성공👍");
		}
		else {
			System.out.println("로그인 실패😭");
		}
		
		
		
		return member;
	}
	
	



}


