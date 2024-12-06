package member.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.domain.MemberVO;
import member.model.*;

public class MemberRegister extends AbstractController {

	private MemberDAO mdao = new MemberDAO_imple();
	
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String method = request.getMethod();

		if ("GET".equalsIgnoreCase(method)) {

			super.setViewPage("/WEB-INF/member/memberRegister.jsp");
		} else {

			String userid = request.getParameter("userid");
			String name = request.getParameter("name");
			String pwd = request.getParameter("pwd");
			String email = request.getParameter("email");
			String hp1 = request.getParameter("hp1");
			String hp2 = request.getParameter("hp2");
			String hp3 = request.getParameter("hp3");
			String postcode = request.getParameter("postcode");
			String address = request.getParameter("address");
			String detailaddress = request.getParameter("detailaddress");
			String extraaddress = request.getParameter("extraaddress");
			String gender = request.getParameter("gender");
			String birthday = request.getParameter("birthday");

			String mobile = hp1 + hp2 + hp3;

			MemberVO member = new MemberVO();

			member.setUserid(userid);
			member.setPwd(pwd);
			member.setName(name);
			member.setEmail(email);
			member.setMobile(mobile);
			member.setPostcode(postcode);
			member.setAddress(address);
			member.setDetailaddress(detailaddress);
			member.setExtraaddress(extraaddress);
			member.setGender(gender);
			member.setBirthday(birthday);
			
			//== 회원가입이 성공되어지면 "회원가입 성공"이라는 alert를 띄우고 시작페이지로 이동한다
			String message = "";
			String loc = "";
			try {
				int n = mdao.registerMember(member);
				if(n==1) {
					message ="회원가입 성공";
					loc=request.getContextPath() + "/index.up";
				}
			} catch (Exception e) {
				e.printStackTrace();
				message = "회원가입 실패";
				loc = "javascript:history.back()";//자바 스크립트를 이용한 이전페이지로 이동하는것
			}
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}// end of execute-------------------------------------

}