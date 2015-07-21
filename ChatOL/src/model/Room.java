package model;

public class Room {
	
	private String roomname = "";
	
	private String roominfo = "";
	
	private String password = "";

	private boolean isEncrypt = false;
	
	private int rid = -1;
	
	private int ownerid = -1;
		
	public Room(){}
	
	public String getRoomname(){
		return roomname;
	}
	
	public void setRoomname(String name){
		roomname = name;
	}
	
	public String getRoominfo(){
		return roominfo;
	}
	
	public void setRoominfo(String info){
		roominfo = info;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String pwd){
		password = pwd;
	}
	
	public boolean getIsEncrypt(){
		return isEncrypt;
	}
	
	public void setIsEncrypt(boolean ie){
		isEncrypt = ie;
	}
	
	public int getRid(){
		return rid;
	}
	
	public void setRid(int roomid){
		rid = roomid;
	}
	
	public int getOwnerid(){
		return ownerid;
	}
	
	public void setOwnerid(int oid){
		ownerid = oid;
	}
}
