package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
	
	private static final String JDBC_DRIVER="com.mysql.jdbc.Driver"; 
	
	private static final String DB_URL="jdbc:mysql://localhost/ChatOL";
	
	private static final String USER = "root";
	
	private static final String PASS = "lyf900116";
	
	private static Connection connection = null;

	public Mysql(){
		if(connection == null){
			try{
				Class.forName(JDBC_DRIVER);
				connection = DriverManager.getConnection(DB_URL,USER,PASS);
				System.out.println(connection.isReadOnly());
			}
			catch(SQLException se){
				se.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}			
		}
	}
	
	public static void getInstance(){
		if(connection == null){
			new Mysql();
		}
	}
	
	public static void close()
		throws SQLException
	{
		try{
			if(connection != null){
				connection.close();
			}			
		}
		catch(SQLException se){
			se.printStackTrace();
		}
	}
	
	protected static ResultSet select(String table,String column,String where,String other){
		String sql = "SELECT " + column + " FROM " + table + " " + where + " " + other;
		System.out.println(sql);
		
		Statement stmt = null;
		ResultSet rs = null;
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	protected static int delete(String table,String where,String other){
		String sql = "DELETE " + " FROM " + table + " " + where + " " + other;
		return excute(sql);
	}
	
	protected static int update(String table,String column,String where,String newColumn){
		String sql = "UPDATE SET " + column + "=" + newColumn + " FROM " + table + " " + where;
		return excute(sql);
	}
	
	protected static int insert(String table,String key,String value){
		String sql = "INSERT INTO " + table + " (" + key + ") VALUES(" + value + ")";
		return excute(sql);
	}
	
	private static int excute(String sql)
	{
		System.out.println(sql);
		
		Statement stmt = null;
		int ret = -1;
		try{
			stmt = connection.createStatement();
			stmt.execute(sql);
			ret = stmt.getUpdateCount();
		}
		catch(SQLException se){
			se.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
}
