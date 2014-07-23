package com.bobo.iweeker.App;

public class DBInfo {
	
	public static class DB{
		//数据库名称
		public static final String DB_NAME = "iWeeker";
		//数据版本
		public static final int VERSION = 1;
	}
	
	public static class Table{
		
		public static final String USER_INFO_TABLE_NAME = "tb_user";
		public static final String USER_INFO_CREATE = "CREATE TABLE " + USER_INFO_TABLE_NAME + " (" 
					+ "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "uid" + " TEXT, "
					+ "screen_name" + " TEXT, " 
					+ "access_token" + " TEXT, "
					+ "expires_in" + " TEXT, "
					+ "isDefault" + " TEXT, " 
					+ "user_icon" + " TEXT "  
					+");";
	}
}
