package model;

import java.sql.*;
import java.util.*;

public class Handle extends Mysql {
	
	/**
	 * 检查用户是否已注册
	 * @param username
	 * @return
	 */
	public static int isExistUser(String username){
		ArrayList<User> user = getUserList("WHERE username='" + username + "'", "");
		return user.size();
	}
	
	/**
	 * 是否登录成功
	 * @param username
	 * @param password
	 * @return
	 */
	public static User isLogin(String username, String password){
		ArrayList<User> user = getUserList("WHERE username='" + username + "' AND password='" + password + "'", "");
		if(user.size() > 0) return user.get(0);
		return null;
	}
	
	/**
	 * 注册一个用户
	 * @param username
	 * @param password
	 * @return
	 */
	public static int register(String username, String password){
		int result = insert("user", 
				"username,password",
				"'" + username + "','" + password + "'"
				);
		return result;
	}
	
	/**
	 * 获取指定信息的用户列表
	 * @param where
	 * @param other
	 * @return
	 */
	public static ArrayList<User> getUserList(String where, String other){
		ResultSet rs = select("user", "*", where, other);
		ArrayList<User> users = new ArrayList<User>();
		try{
			while(rs.next()){
				User u = new User();
				u.setUid(rs.getInt("uid"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				users.add(u);
			}
			rs.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		return users;		
	}
	
	/**
	 * 获取单个用户信息
	 * @param uid
	 * @return
	 */
	public static User getUser(int uid){
		ArrayList<User> user = getUserList(" WHERE uid=" + uid, "");
		return user.get(0);
	}
	
	/**
	 * 创建一个聊天室
	 * @param roomname
	 * @param roominfo
	 * @param password
	 * @param isEncrypt
	 * @return
	 */
	public static Room createRoom(String roomname, String roominfo, String password, boolean isEncrypt, int ownerid){
		insert("room", 
			"roomname,roominfo,password,isEncrypt,ownerid",
			"'" + roomname + "','" + roominfo + "','" + password + "','" + (isEncrypt ? "T" : "F") + "'," + ownerid
			);
		
		ArrayList<Room> room = getRoomList("WHERE roomname='" + roomname + "' AND ownerid=" + ownerid, "ORDER BY rid DESC LIMIT 0,1");
		int rid = room.get(0).getRid();
		joinRoom(rid, ownerid);
		return room.get(0);
	}
	
	/**
	 * 获取指定房间列表
	 * @param where
	 * @param other
	 * @return
	 */
	public static ArrayList<Room> getRoomList(String where, String other){
		ResultSet rs = select("room", "*", where, other);
		ArrayList<Room> rooms = new ArrayList<Room>();
		try{
			while(rs.next()){
				Room r = new Room();
				r.setRid(rs.getInt("rid"));
				r.setRoomname(rs.getString("roomname"));
				r.setRoominfo(rs.getString("roominfo"));
				r.setIsEncrypt(rs.getString("isEncrypt").equals("T") ? true : false);
				r.setPassword(rs.getString("password"));
				r.setOwnerid(rs.getInt("ownerid"));
				rooms.add(r);
			}
			rs.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		return rooms;
	}
	
	/**
	 * 删除聊天室
	 * @param rid
	 */
	public static void deleteRoom(int rid){
		delete("userroom", "WHERE rid=" + rid, "");
		delete("room", "WHERE rid=" + rid, "");
		delete("message", "WHERE rid=" + rid, "");
	}
	
	/**
	 * 加入聊天室
	 * @param rid
	 * @param uid
	 */
	public static int joinRoom(int rid, int uid){
		int ret = insert("useroom",
				"rid,uid",
				rid + "," + uid
				);
		return ret;
	}
	
	
	/**
	 * 获取加入过的房间
	 * @param uid
	 * @return
	 */
	public static ArrayList<Integer> joinedRoom(int uid){
		ResultSet rs = select("useroom", "rid", "WHERE uid=" + uid, "");
		ArrayList<Integer> rids = new ArrayList<Integer>();
		try{
			while(rs.next()){
				Integer rid = rs.getInt("rid");
				rids.add(rid);
			}
			rs.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		//System.out.println(rids);
		return rids;
	}
	
	/**
	 * 退出房间
	 * @param rid
	 * @param uid
	 */
	public static int leaveRoom(int rid, int uid){
		int ret = delete("useroom",
				"WHERE rid=" + rid + " AND uid=" + uid,
				"");
		Room room = getRoomList("WHERE rid=" + rid, "").get(0);
		if(room.getOwnerid() == uid){
			deleteRoom(rid);
		}
		return ret;
	}
	
	public static void leaveAllRoom(int uid){
		delete("useroom", "WHERE uid=" + uid, "");
	}
	
	public static ArrayList<User> getUserListInRoom(String where, String other){
		ResultSet rs = select("useroom", "*", where, other);
		ArrayList<User> users = new ArrayList<User>();
		try{
			while(rs.next()){
				User u = new User();
				u.setUid(rs.getInt("uid"));
				String username = getUser(u.getUid()).getUsername();
				u.setUsername(username);
				users.add(u);
			}
			rs.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		return users;		
	}
	
	/**
	 * 读取消息
	 * @param uid
	 * @param rid
	 * @param mid
	 * @return
	 */
	public static ArrayList<Message> getMessages(String username, int rid, int mid){
		ArrayList<Message> messages = new ArrayList<Message>();
		
		ResultSet rs = select("message", "*" ,"WHERE (dest='" + username + "' OR dest='*' OR (src='" + username + "' AND mtype='private')) AND rid=" + rid + " AND mid>" + mid, "ORDER BY mtime ASC");
		
		try{
			while(rs.next()){
				Message m = new Message();
				m.setMid(rs.getInt("mid"));
				m.setContent(rs.getString("content"));
				m.setFrom(rs.getString("src"));
				m.setTo(rs.getString("dest"));
				m.setType(rs.getString("mtype"));
				m.setTime(rs.getString("mtime"));
				messages.add(m);
			}
			rs.close();
		}
		catch(SQLException se){
			se.printStackTrace();
		}		
		
		return messages;
	}
	
	/**
	 * 接收消息
	 * @param from
	 * @param to
	 * @param rid
	 * @param type
	 * @param content
	 */
	public static int addMessage(String from, String to, int rid, String type, String content){
		int count = insert("message",
				"src,dest,rid,content,mtype",
				"'" + from + "','" + to + "'," + rid + ",'" + content + "','" + type + "'" 
				);
		return count;
	}
		
}
