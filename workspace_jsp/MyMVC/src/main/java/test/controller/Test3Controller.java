package test.controller;

import common.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Test3Controller extends AbstractController {

	public Test3Controller() {
	//	System.out.println("$$$ 확인용 Test3Controller 클래스 생성자 호출함 $$$"); 
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse respone) throws Exception {
		
		request.setAttribute("center", "쌍용강북교육센터 G클래스");
		
		/////////////////////////////////////////////////////
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/test/test3.jsp");
	}	
	
}
