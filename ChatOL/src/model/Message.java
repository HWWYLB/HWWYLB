package model;

public class Message {
	
	private String content;
	
	private String from;
	
	private String to;
	
	private String type;
	
	private String time;
	
	private int mid;
	
	private int rid;
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String cnt){
		content = cnt;
	}
	
	public String getFrom(){
		return from;
	}
	
	public void setFrom(String f){
		from = f;
	}
	
	public String getTo(){
		return to;
	}
	
	public void setTo(String t){
		to = t;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String t){
		type = t;
	}
	
	public String getTime(){
		return time;
	}
	
	public void setTime(String t){
		time = t;
	}
	
	public int getMid(){
		return mid;
	}
	
	public void setMid(int messageid){
		mid = messageid;
	}
	
	public int getRid(){
		return rid;
	}
	
	public void setRid(int roomid){
		rid = roomid;
	}
}
