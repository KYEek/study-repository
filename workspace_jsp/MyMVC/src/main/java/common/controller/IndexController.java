package common.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.domain.ImageVO;
import myshop.model.ProductDAO;
import myshop.model.ProductDAO_imple;

public class IndexController extends AbstractController {

	private ProductDAO pdao;
	
	public IndexController() {
		pdao = new ProductDAO_imple();
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse respone) throws Exception {
		System.out.println("IndexController 실행됨");
		try {
			List<ImageVO> imgList = pdao.imageSelectAll();
			request.setAttribute("imgList", imgList); 
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/index.jsp");
		} catch(SQLException e) {
			e.printStackTrace();
			
			super.setRedirect(true);
			super.setViewPage(request.getContextPath()+"/error.up");
		}

	}

}
