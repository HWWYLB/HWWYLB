package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Handle;
import model.Mysql;
import model.User;
import net.sf.json.JSONObject;

public class AuthLogin extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AuthLogin() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		String check = (String)request.getParameter("check");
		
		System.out.println(username + " " + password + " " + check);
		
		Mysql.getInstance();
		JSONObject json = new JSONObject();
		User user = null;
		
		Cookie cookies[]  = request.getCookies();
		if(cookies != null){
			for(int i = 0; i < cookies.length; i++){
				Cookie cookie = cookies[i];
				if(cookie.getName().equalsIgnoreCase("uid")){
					user = Handle.getUser(Integer.parseInt(cookie.getValue()));
					break;
				}
			}
		}
		
		user = user == null ? Handle.isLogin(username, password) : user;
		if(user != null){
			HttpSession session = request.getSession(true);
			session.setAttribute("uid", "" + user.getUid());
			session.setAttribute("username", user.getUsername());
			session.setAttribute("mid", "0");
			
			json.put("state", "success");
			json.put("uid", user.getUid());
			json.put("username", user.getUsername());
			json.put("info", "登录成功！");
			if(check == "true"){
				Cookie cookie = new Cookie("uid", "" + user.getUid());
				cookie.setMaxAge(60*60*24);
				response.addCookie(cookie);
			}			
		}
		else{
			json.put("info", "用户名或密码不正确！");
		}
		out.println(json);
		
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
