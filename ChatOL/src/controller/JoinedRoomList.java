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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JoinedRoomList extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public JoinedRoomList() {
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
		
		HttpSession session = request.getSession();
		String userid = (String)session.getAttribute("uid");

		if(userid != null){
			int uid = Integer.parseInt(userid);
			
			Mysql.getInstance();
			
			ArrayList<Integer> rids = Handle.joinedRoom(uid);
			JSONArray ja = new JSONArray();
			for(Integer rid:rids){
				ja.add(rid);
			}
			
			json.put("state", "success");
			json.put("info", "获取已进入的房间列表成功");
			json.put("rids", ja);	
		}else{
			json.put("state", "fail");
			json.put("info", "请先登录!");		
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
