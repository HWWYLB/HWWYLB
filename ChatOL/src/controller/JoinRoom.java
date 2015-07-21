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
import net.sf.json.JSONObject;

public class JoinRoom extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public JoinRoom() {
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
		String username = (String)session.getAttribute("username");
		String roomid = (String)request.getParameter("rid");

		if(userid != null && roomid != null){
			int uid = Integer.parseInt(userid);
			int rid = Integer.parseInt(roomid);
			String password = (String)request.getParameter("password");
			
			Mysql.getInstance();
			Room room = Handle.getRoomList("WHERE rid=" + rid, "LIMIT 0,1").get(0);
			ArrayList<Integer> rids = Handle.joinedRoom(uid);
			boolean isIn = false;
			if(room != null){
				if(room.getIsEncrypt()){
					if(room.getPassword().equals(password) || rids.contains(rid)){
						isIn = true;
					}else{
						json.put("state", "fail");
						json.put("info", "密码错误!");
					}
				}else{
					isIn = true;
				}
			}else{
				json.put("state", "fail");
				json.put("info", "房间不存在！");			
			}	
			if(isIn){
				if(rids.contains(rid) == false){
					int count = Handle.joinRoom(rid, uid);
					if(count > 0){
						Handle.addMessage(uid + "", "*", rid, "info", "用户" + username + "进入房间");
						json.put("state", "success");
						json.put("info", "进入房间成功!");
					}else{
						json.put("state", "fail");
						json.put("info", "进入房间失败!");
					}	
				}else{
					json.put("state", "success");
					json.put("info", "进入房间成功!");
				}
			}			
		}else{
			json.put("state", "fail");
			json.put("info", "未登录或房间不存在!");		
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
