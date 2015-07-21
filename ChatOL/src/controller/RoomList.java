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
import model.Room;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RoomList extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public RoomList() {
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
			
			json.put("state", "success");
			
			ArrayList<Room> rooms = Handle.getRoomList("WHERE ownerid!=" + uid, "");
			JSONArray ja = new JSONArray();
			for(Room r:rooms){
				JSONObject jo = new JSONObject();
				jo.put("roomname", r.getRoomname());
				jo.put("roominfo", r.getRoominfo());
				jo.put("isEncrypt", r.getIsEncrypt() ? "true" : "false");
				jo.put("ownerid", r.getOwnerid());
				jo.put("rid", r.getRid());
				ja.add(jo);
			}
			json.put("notmine", ja);

			rooms = Handle.getRoomList("WHERE ownerid=" + uid, "");
			ja = new JSONArray();
			for(Room r:rooms){
				JSONObject jo = new JSONObject();
				jo.put("roomname", r.getRoomname());
				jo.put("roominfo", r.getRoominfo());
				jo.put("isEncrypt", r.getIsEncrypt() ? "true" : "false");
				jo.put("ownerid", r.getOwnerid());
				jo.put("rid", r.getRid());
				ja.add(jo);
			}
			json.put("mine", ja);			
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
