package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Handle;
import model.Mysql;
import net.sf.json.JSONObject;

public class Register extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public Register() {
		super();
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
		
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		request.setCharacterEncoding("utf-8");
		
		String username = (String)request.getParameter("username");
		String password = (String)request.getParameter("password");
		String password2 = (String)request.getParameter("password2");
		
		System.out.println(username + " " + password + " " + password2);
		
		Mysql.getInstance();
		
		JSONObject json = new JSONObject();
		if(password.compareTo(password2) != 0){
			json.put("state", "unsame");
			json.put("info", "两次输入密码不一致!");
		}else if(Handle.isExistUser(username) > 0){
			json.put("state", "exist");
			json.put("info", "用户名已存在!");
		}else{
			Handle.register(username, password);
			json.put("state", "success");
			json.put("info", "注册成功!请登录...");
		}
		System.out.println(json);
		out.println(json);
		
		out.flush();
		out.close();
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

		doGet(request, response);
	}

}
