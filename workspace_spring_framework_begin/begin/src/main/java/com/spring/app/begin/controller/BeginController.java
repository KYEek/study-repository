package com.spring.app.begin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.begin.domain.BeginDateVO;
import com.spring.app.begin.domain.BeginVO;
import com.spring.app.begin.service.BeginService;

import oracle.jdbc.proxy.annotation.Post;

/*
사용자 웹브라우저 요청(View)  ==> DispatcherServlet ==> @Controller 클래스 <==>> Service단(핵심업무로직단, business logic단) <==>> Model단[Repository](DAO, DTO) <==>> myBatis <==>> DB(오라클)           
(http://...  *.action)                                  |                                                                                                                              
 ↑                                                View Resolver
 |                                                      ↓
 |                                                View단(.jsp 또는 Bean명)
 -------------------------------------------------------| 

사용자(클라이언트)가 웹브라우저에서 http://localhost:9099/begin/test/test_insert.action 을 실행하면
배치서술자인 web.xml 에 기술된 대로  org.springframework.web.servlet.DispatcherServlet 이 작동된다.
DispatcherServlet 은 bean 으로 등록된 객체중 controller 빈을 찾아서  URL값이 "/begin/test/test_insert.action" 으로
매핑된 메소드를 실행시키게 된다.                                               
Service(서비스)단 객체를 업무 로직단(비지니스 로직단)이라고 부른다.
Service(서비스)단 객체가 하는 일은 Model단에서 작성된 데이터베이스 관련 여러 메소드들 중 관련있는것들만을 모아 모아서
하나의 트랜잭션 처리 작업이 이루어지도록 만들어주는 객체이다.
여기서 업무라는 것은 데이터베이스와 관련된 처리 업무를 말하는 것으로 Model 단에서 작성된 메소드를 말하는 것이다.
이 서비스 객체는 @Controller 단에서 넘겨받은 어떤 값을 가지고 Model 단에서 작성된 여러 메소드를 호출하여 실행되어지도록 해주는 것이다.
실행되어진 결과값을 @Controller 단으로 넘겨준다.
*/

//==== #1. 컨트롤러 선언 ====
//@Component
/* XML에서 빈을 만드는 대신에 클래스명 앞에 @Component 어노테이션을 적어주면 해당 클래스는 bean으로 자동 등록된다. 
  그리고 bean의 이름(첫글자는 소문자)은 해당 클래스명이 된다. 
  즉, 여기서 bean의 이름은 beginController 가 된다. 
  여기서는 @Controller 를 사용하므로 @Component 기능이 이미 있으므로 @Component를 명기하지 않아도 BeginController 는 bean 으로 등록되어 스프링컨테이너가 자동적으로 관리해준다. 
*/
@Controller
@RequestMapping(value="/test/")
public class BeginController { //beginController이렇게 객체화
	
	// === 의존객체 주입하기(DI: Dependency Injection) ===
    // ※ 의존객체주입(DI : Dependency Injection) 
   //  ==> 스프링 프레임워크는 객체를 관리해주는 컨테이너를 제공해주고 있다.
   //      스프링 컨테이너는 bean으로 등록되어진 BeginController 클래스 객체가 사용되어질때, 
   //      BeginController 클래스의 인스턴스 객체변수(의존객체)인 BeginService service 에 
   //      자동적으로 bean 으로 등록되어 생성되어진 BeginService service 객체를  
   //      BeginController 클래스의 인스턴스 변수 객체로 사용되어지게끔 넣어주는 것을 의존객체주입(DI : Dependency Injection)이라고 부른다. 
   //      이것이 바로 IoC(Inversion of Control == 제어의 역전) 인 것이다.
   //      즉, 개발자가 인스턴스 변수 객체를 필요에 의해 생성해주던 것에서 탈피하여 스프링은 컨테이너에 객체를 담아 두고, 
   //      필요할 때에 컨테이너로부터 객체를 가져와 사용할 수 있도록 하고 있다. 
   //      스프링은 객체의 생성 및 생명주기를 관리할 수 있는 기능을 제공하고 있으므로, 더이상 개발자에 의해 객체를 생성 및 소멸하도록 하지 않고
   //      객체 생성 및 관리를 스프링 프레임워크가 가지고 있는 객체 관리기능을 사용하므로 Inversion of Control == 제어의 역전 이라고 부른다.  
   //      그래서 스프링 컨테이너를 IoC 컨테이너라고도 부른다.
   
   //  IOC(Inversion of Control) 란 ?
   //  ==> 스프링 프레임워크는 사용하고자 하는 객체를 빈형태로 이미 만들어 두고서 컨테이너(Container)에 넣어둔후
   //      필요한 객체사용시 컨테이너(Container)에서 꺼내어 사용하도록 되어있다.
   //      이와 같이 객체 생성 및 소멸에 대한 제어권을 개발자가 하는것이 아니라 스프링 Container 가 하게됨으로써 
   //      객체에 대한 제어역할이 개발자에게서 스프링 Container로 넘어가게 됨을 뜻하는 의미가 제어의 역전 
   //      즉, IOC(Inversion of Control) 이라고 부른다.
   
   
   //  === 느슨한 결합 ===
   //      스프링 컨테이너가 BeginController 클래스 객체에서 BeginService 클래스 객체를 사용할 수 있도록 
   //      만들어주는 것을 "느슨한 결합" 이라고 부른다.
   //      느스한 결합은 BeginController 객체가 메모리에서 삭제되더라도 BeginService service 객체는 메모리에서 동시에 삭제되는 것이 아니라 남아 있다.
   
   // ===> 단단한 결합(개발자가 인스턴스 변수 객체를 필요에 의해서 생성해주던 것)
   // private BeginService service = new BeginService_imple(); 
   // ===> BeginController 객체가 메모리에서 삭제 되어지면  BeginService service 객체는 멤버변수(필드)이므로 메모리에서 자동적으로 삭제되어진다.
	
	@Autowired
	private BeginService service;
	
	
//	@RequestMapping(value ="insert.action", method= {RequestMethod.GET})// 오로지 GET방식만 허락하는 것임.
//	@RequestMapping(value ="insert.action", method= {RequestMethod.POST})// 오로지 POST방식만 허락하는 것임.
//	@RequestMapping(value ="insert.action")// GET방식 또는 POST방식 둘 모두 허락하는 것임. 
    									   // http://localhost:9099/begin/test/insert.action
	
	// Spring Boot 에서는 @RequestMapping 을 사용하지 말고 @GetMapping(오로지 GET방식만 허락하는 것임) 또는 @PostMapping(오로지 POST방식만 허락하는 것임) 을 사용하는 것을 권장함.!!
	// Spring Framework 에서는 @RequestMapping 을 사용해도 괜찮으며 사용하지 말라는 권장 메시지가 나오지 않음.
//	@GetMapping(value ="insert.action") // 오로지 GET방식만 허락하는 것임.
//	@PostMapping(value ="insert.action") // 오로지 POST방식만 허락하는 것임.
//	@GetMapping(path ="insert.action") // value 또는 path는 동일한 결과를 가져오므로 아무거나 사용해도 된다.
	@GetMapping("insert.action") // value 또는 path는 생략가능함.
	public String insert(HttpServletRequest request) {
		
		int n = service.insert();
		
		String message = "";
		
		if(n == 1) {
			message = "데이터 입력 성공!!";
		}
		else {
			message = "데이터 입력 실패!!";
		}
		
		request.setAttribute("message", message);
		request.setAttribute("n", n);
		
		return "begin/insert";
		
		///WEB-INF/views/begin/insert.jsp 페이지를 만들어야 한다
	}
	
	// 먼저 오라클에서 
	   /*
	      insert into spring_test(no, name, writeday)
	      values(102, '김태희', default);
	      
	      insert into spring_test(no, name, writeday)
	      values(103, '변우석', default);
	      
	      commit; 
	      
	      을 먼저 하고서 다음의 것을 한다. 
	   */
	
	@GetMapping("select.action") //http://localhost:9099/begin/test/select.action
	public String select(HttpServletRequest request) {
		
		List<BeginVO> beginvoList = service.select();
		
		request.setAttribute("beginvoList", beginvoList);
		
		return "begin/select";
		///WEB-INF/views/begin/select.jsp 페이지를 만들어야 한다
		
	}
	
	
	@GetMapping("select_datevo.action") //http://localhost:9099/begin/test/select_datevo.action
	public String select_datevo(HttpServletRequest request) {
		
		List<BeginDateVO> begindatevoList = service.select_datevo();
		
		request.setAttribute("begindatevoList", begindatevoList);
		
		return "begin/select_datevo";
		///WEB-INF/views/begin/select.jsp 페이지를 만들어야 한다
		
	}
	
	@GetMapping("select_map.action") //http://localhost:9099/begin/test/select_map.action
	public String select_mapo(HttpServletRequest request) {
		
		List<Map<String, String>> mapList= service.select_map();
		
		request.setAttribute("mapList", mapList);
		
		return "begin/select_map";
		///WEB-INF/views/begin/select.jsp 페이지를 만들어야 한다
		
	}
	
	
//	@RequestMapping(value ="form_request.action", method= {RequestMethod.GET})// 오로지 GET방식만 허락하는 것임.
//	@RequestMapping(value ="form_request.action", method= {RequestMethod.POST})// 오로지 POST방식만 허락하는 것임.
//	@RequestMapping(value ="form_request.action")// GET방식 또는 POST방식 둘 모두 허락하는 것임. 
	@RequestMapping(path ="form_request.action")// GET방식 또는 POST방식 둘 모두 허락하는 것임. 
	public String form_request(HttpServletRequest request) {
		String method = request.getMethod();
		if("GET".equalsIgnoreCase(method)) {	//GET방식이라면
			return "begin/form_request";	//view단 페이지를 띄워준다
			///WEB-INF/views/begin/form_request.jsp 페이지를 만들어 줘야 한다
		}
		else {
			String no = request.getParameter("no");
			String name = request.getParameter("name");
			
			BeginVO bvo = new BeginVO();
			bvo.setNo(no);
			bvo.setName(name);
			
			int n = service.insert_no(bvo);
			
			if(n==1) {
				return "redirect:/test/select.action";
				///test/select.action URL로 redirect(페이지이동) 하라는 말이다
			}else {
				return "redirect:/test/form_request.action";
			}
		}
	}
	
	
	@RequestMapping(path ="form_vo.action")// GET방식 또는 POST방식 둘 모두 허락하는 것임. 
	public String form_vo(HttpServletRequest request, BeginVO bvo) {
		// BeginVO bvo 의 setXXX() 은 from  태그의 value 값과 일치해야만 한다.
		
		
		String method = request.getMethod();
		if("GET".equalsIgnoreCase(method)) {	//GET방식이라면
			return "begin/form_vo";	//view단 페이지를 띄워준다
			///WEB-INF/views/begin/form_request.jsp 페이지를 만들어 줘야 한다
		}
		else {
			
			int n = service.insert_no(bvo);
			
			if(n==1) {
				return "redirect:/test/select.action";
				///test/select.action URL로 redirect(페이지이동) 하라는 말이다
			}else {
				return "redirect:/test/form_request.action";
			}
		}
	}
	
	
	@GetMapping("form_beginvo.action") //GET방식만 허락하는 것임.
	public String form_beginvo() {
		
		
		return "begin/form_beginvo"; //view 단 페이지를 띄어라
		///WEB-INF/views/begin/form_beginvo.jsp 페이지를 만들어 줘야 한다
	}
	@PostMapping("form_beginvo.action")
	public String form_beginvo(BeginVO bvo) {
		
		int n = service.insert_no(bvo);
		
		if(n==1) {
			return "redirect:/test/select.action";
			///test/select.action URL로 redirect(페이지이동) 하라는 말이다
		}else {
			return "redirect:/test/form_request.action";
			///test/form_beginvo.action URL로 redirect(페이지이동) 하라는 말이다
		}
	}
	
	
	@GetMapping("form_datevo.action")
	public String form_datevo() {
		
		
		return "begin/form_datevo";
	}
	
	@PostMapping("form_datevo.action")
	public String form_datevo(BeginDateVO bdatevo) {
		int n =service.insert_datevo(bdatevo);
		System.out.println(n);
		if(n==1) {
			return "redirect:/test/select_datevo.action";
		}
		else {
			return "redirect:/test/form_datevo.action";
		}
	}
	
	@GetMapping("form_request_map.action")
	public String form_request_map() {
		return "begin/form_request_map";
	}
	
	@PostMapping("form_request_map.action")
	public String form_request_map(HttpServletRequest request) {
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("no", no);
		paraMap.put("name", name);
		
		int n = service.insert_map(paraMap);
		
		if(n==1) {
			return "redirect:/test/select_map.action";
		}
		else {
			return "redirect:/test/form_request_map.action";
		}
	}
	
	@GetMapping("form_RequestParam_Map.action")
	public String form_RequestParam_Map() {
		return "begin/form_RequestParam_Map";
	}
	
	@PostMapping("form_RequestParam_Map.action")
	public String form_RequestParam_Map(@RequestParam Map<String, String> paraMap) {
		/* 폼에서 건네준 값들을 컨트롤러에서 Map 으로 받을수 있음. 
        반드시 @ReqestParam 을 Map 앞에 붙여 주어야 함. 안 붙이면 오류발생함!!!
        이때 Map 의 key 값은 자동적으로 폼의 name 명이 key 값이 되어짐. */
		
		int n = service.insert_map(paraMap);
		
		if(n==1) {
			return "redirect:/test/select_map.action";
		}
		else {
			return "redirect:/test/form_RequestParam_Map.action";
		}

	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@GetMapping("select_all_1.action")  // http://localhost:9099/begin/test/select_all.action
	   public String select_all_1(HttpServletRequest request) {
	      
	      List<BeginVO> beginvoList = service.select();
	      
	      request.setAttribute("beginvoList", beginvoList);
	      
	      return "begin/select_all_1";
	      //   /WEB-INF/views/begin/select_all.jsp 페이지를 만들어야 한다.
	   }
	
//		@GetMapping("select_one.action") // http://localhost:9099/begin/test/select_all.action
//		public String select_one(HttpServletRequest request) {
//
//			String no = request.getParameter("no");
//			BeginVO bvo = service.selectOne(no);
//
//			request.setAttribute("bvo", bvo);
//			request.setAttribute("name", bvo.getName());
//			request.setAttribute("writeday", bvo.getWriteday());
//			return "begin/select_one";
//			// /WEB-INF/views/begin/select_all.jsp 페이지를 만들어야 한다.
//		}
	
//		또는
		@GetMapping("select_one.action") // http://localhost:9099/begin/test/select_all.action
		public String select_one(/*@RequestParam(name="no") String num*/
								 /*@RequestParam(value="no") String num*/
								   @RequestParam(defaultValue = "101") String no, HttpServletRequest request) {
			
			BeginVO bvo = service.selectOne(no);
			request.setAttribute("bvo", bvo);
			return "begin/select_one";
		}
	
	
		// == @PathVariable 어노테이션에 대해서 알아보기 ==
		@GetMapping("select_all_2.action") // http://localhost:9099/begin/test/select_all.action
		public String select_all_PathVariable(HttpServletRequest request) {

			List<BeginVO> beginvoList = service.select();

			request.setAttribute("beginvoList", beginvoList);

			return "begin/select_all_2";
			// /WEB-INF/views/begin/select_all.jsp 페이지를 만들어야 한다.
		}
		
		@GetMapping("select_one/{no}.action")	//http://localhost:9099/begin/test/select_one/101.action
		public String select_one_PathVariable(@PathVariable Long no, HttpServletRequest request) {
			BeginVO bvo = service.select_one_vo_PathVariable(no);request.setAttribute("bvo", bvo);
			return "begin/select_one_PathVariable";
		}
}
