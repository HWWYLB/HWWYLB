package model;

public class User {
	
	private String username = "";
	
	private String password = "";
	
	private int uid = -1;
	
	public User(){}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String name){
		username = name;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String pwd){
		password=  pwd;
	}
	
	public int getUid(){
		return uid;
	}
	
	public void setUid(int userid){
		uid = userid;
	}
}
