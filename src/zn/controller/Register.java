package zn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zn.model.UserService;

/**
 * @author 	Andy Hung
 * @version v1.0
 * 註冊
 */

@WebServlet(
		urlPatterns = {"/register.do"},
		initParams = {
				@WebInitParam(name = "SUCCESS_VIEW", value = "success.jsp"),
				@WebInitParam(name = "ERROR_VIEW", value = "register.jsp")
		}
)

public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String SUCCESS_VIEW;
	private String ERROR_VIEW;
	
	@Override
	public void init() throws ServletException{
		SUCCESS_VIEW = getServletConfig().getInitParameter("SUCCESS_VIEW");
		ERROR_VIEW = getServletConfig().getInitParameter("ERROR_VIEW");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 取得請求參數
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPasswd = request.getParameter("confirmPasswd");
		
		//取得UserService物件
		UserService userService = (UserService)getServletContext().getAttribute("userService");
		
		// 驗證
		List<String> errors = new ArrayList<String>();
		if (isInvalidEmail(email)) {
			errors.add("未填寫郵件或郵件格是不正確");
		}
		if(userService.isInvalidUsername(username)) {
			errors.add("使用者名稱為空或已存在");
		}
		if (isInvalidPassword(password, confirmPasswd)) {
			errors.add("請確認密碼格式及確認密碼");
		}

		String resultPage = ERROR_VIEW;
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
		} else {
			resultPage = SUCCESS_VIEW;
			userService.createUserData(email, username, password);
		}

		request.getRequestDispatcher(resultPage).forward(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("register.jsp");
	}

	private boolean isInvalidEmail(String email) {
		return email == null || !email.matches("^[_a-z0-9-]+([.]" + "[_a-z0-9-]+)*@[a-z0-9-]+([.][a-z0-9-]+)*$");

	}

	private boolean isInvalidPassword(String password, String confirmedPasswd) {

		return password == null || password.length() < 6 || password.length() > 16 || !password.equals(confirmedPasswd);

	}
	
}
