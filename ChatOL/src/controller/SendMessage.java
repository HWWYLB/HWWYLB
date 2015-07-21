package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.*;
import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SendMessage extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public SendMessage() {
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

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		JSONArray json = new JSONArray();
		
		HttpSession session = request.getSession();
		String userid = (String)session.getAttribute("uid");
		String username = (String)session.getAttribute("username");
		
		if(userid != null){
			int rid = Integer.parseInt((String)request.getParameter("rid"));
			int mid = Integer.parseInt((String)request.getParameter("mid"));
			
			Mysql.getInstance();
			ArrayList<Message> msgs = Handle.getMessages(username, rid, mid);
			for(Message m:msgs){
				JSONObject jo = new JSONObject();
				jo.put("mid", m.getMid() + "");
				jo.put("from", m.getFrom());
				jo.put("to", m.getTo());
				jo.put("content", m.getContent());
				jo.put("type", m.getType());
				jo.put("time", m.getTime());
				json.add(jo);
			}
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

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
