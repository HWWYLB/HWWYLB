package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Handle;
import model.Mysql;
import model.User;
import net.sf.json.JSONObject;

public class AcceptMessage extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AcceptMessage() {
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
		JSONObject json = new JSONObject();
		
		request.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession();
		String from = (String)session.getAttribute("username");
		
		if(from != null){
			String content = (String)request.getParameter("content");
			String type = (String)request.getParameter("type");
			String to = (String)request.getParameter("to");
			int rid = Integer.parseInt((String)request.getParameter("rid"));
			
			System.out.println(from + " " + to + " " + rid + " " + type + " " + content);
			
			Mysql.getInstance();
			
			int toUserid = 0;
			ArrayList<Integer> rids = new ArrayList<Integer>();
			if(type.equals("private")){
				ArrayList<User> users = Handle.getUserList("WHERE username='" + to + "'", "");
				if(users.size() > 0){
					toUserid = users.get(0).getUid();
					rids = Handle.joinedRoom(toUserid);
				}
			}
			
			if(type.equals("private") == false || rids.contains(rid)){
				int count = Handle.addMessage(from, to, rid, type, content);
				
				if(count > 0){
					System.out.println("success");
					json.put("state", "success");
					json.put("info", "消息发送成功！");
				}else{
					System.out.println("fail");
					json.put("state", "fail");
					json.put("info", "消息发送失败！");
				}
			}else{
				json.put("state", "fail");
				json.put("info", "私聊对象不存在！");	
			}			
		}else{
			json.put("state", "fail");
			json.put("info", "未登录！");	
		}

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
