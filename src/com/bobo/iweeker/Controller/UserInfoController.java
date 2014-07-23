package com.bobo.iweeker.Controller;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bobo.iweeker.App.DBInfo;
import com.bobo.iweeker.App.WeiboApplication;
import com.bobo.iweeker.Model.UserInfo;

public class UserInfoController {
	
	public static void insertUserInfo(UserInfo userInfo){
		SQLiteDatabase db = WeiboApplication.dbHelper.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(UserInfo.USER_ID, userInfo.getUid());
		cv.put(UserInfo.USER_NAME, userInfo.getScreen_name());
		cv.put(UserInfo.EXPIRES_IN, userInfo.getExpires_in());
		cv.put(UserInfo.TOKEN, userInfo.getAccess_token());
		cv.put(UserInfo.IS_DEFAULT, userInfo.getIsDefault());
		cv.put(UserInfo.USER_ICON, userInfo.getUser_icon());

		db.insert(UserInfo.TABLE_NAME, null, cv);
		db.close();
	}
	
	public static UserInfo getUserInfoByUID(String user_id) {
		SQLiteDatabase db = WeiboApplication.dbHelper.getReadableDatabase();
		UserInfo userInfo = null;
		try{
			Cursor cursor = db.query(UserInfo.TABLE_NAME, new String[]{UserInfo.ID, UserInfo.USER_ID, UserInfo.USER_NAME, UserInfo.TOKEN, UserInfo.EXPIRES_IN, UserInfo.IS_DEFAULT, UserInfo.USER_ICON}, UserInfo.USER_ID+"="+user_id, null, null, null, null);
		
			if(null != cursor){
				if(cursor.getCount() > 0)
				{
					cursor.moveToNext();
					String screen_name = cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME));
					String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
					String expires_in = cursor.getString(cursor.getColumnIndex(UserInfo.EXPIRES_IN));
					String isDefault = cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT));
					String user_icon = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ICON));
					
					userInfo = new UserInfo(user_id, screen_name, token, expires_in, isDefault, user_icon);
					return userInfo;
				}
			}
		}catch (Exception e) {
				// TODO: handle exception
			Log.i("bobo", e.getMessage());
		}
		return null;
	}
	
	public static UserInfo getUserInfoByDefault() {
		SQLiteDatabase db = WeiboApplication.dbHelper.getReadableDatabase();
		UserInfo userInfo = null;
		try{
			Cursor cursor = db.query(UserInfo.TABLE_NAME, new String[]{UserInfo.ID, UserInfo.USER_ID, UserInfo.USER_NAME, UserInfo.TOKEN, UserInfo.EXPIRES_IN, UserInfo.IS_DEFAULT, UserInfo.USER_ICON}, UserInfo.IS_DEFAULT+"="+1, null, null, null, null);
		
			if(null != cursor){
				if(cursor.getCount() > 0)
				{
					cursor.moveToNext();
					String user_id = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ID));
					String screen_name = cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME));
					String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
					String expires_in = cursor.getString(cursor.getColumnIndex(UserInfo.EXPIRES_IN));
					String isDefault = cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT));
					String user_icon = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ICON));
					
					userInfo = new UserInfo(user_id, screen_name, token, expires_in, isDefault, user_icon);
					return userInfo;
				}
			}
		}catch (Exception e) {
				// TODO: handle exception
			Log.i("bobo", e.getMessage());
		}
		return null;
	}
	
	public static ArrayList<UserInfo> getAllUserInfo() {
		SQLiteDatabase db = WeiboApplication.dbHelper.getReadableDatabase();
		ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
		
		Cursor cursor = db.query(UserInfo.TABLE_NAME, new String[]{UserInfo.ID, UserInfo.USER_ID, UserInfo.USER_NAME, UserInfo.TOKEN, UserInfo.EXPIRES_IN, UserInfo.IS_DEFAULT, UserInfo.USER_ICON}, null, null, null, null, null);
		
		if(null != cursor) {
			while(cursor.moveToNext()) {
				String user_id = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ID));
				String screen_name = cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME));
				String token = cursor.getString(cursor.getColumnIndex(UserInfo.TOKEN));
				String expires_in = cursor.getString(cursor.getColumnIndex(UserInfo.EXPIRES_IN));
				String isDefault = cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT));
				String user_icon = cursor.getString(cursor.getColumnIndex(UserInfo.USER_ICON));
				
				UserInfo userInfo = new UserInfo(user_id, screen_name, token, expires_in, isDefault, user_icon);
				
				userInfos.add(userInfo);
			}
		}
		return userInfos;
	}
	
	public static void updateUserInfo(UserInfo userInfo) {
		SQLiteDatabase db = WeiboApplication.dbHelper.getWritableDatabase();
		
		 ContentValues cv = new ContentValues();
		 cv.put(UserInfo.USER_NAME, userInfo.getScreen_name());
		 cv.put(UserInfo.TOKEN, userInfo.getAccess_token());
		 cv.put(UserInfo.EXPIRES_IN, userInfo.getExpires_in());
		 cv.put(UserInfo.IS_DEFAULT, userInfo.getIsDefault());
		 cv.put(UserInfo.USER_ICON, userInfo.getUser_icon());
		 
		 String str = "uid=" + userInfo.getUid();
		 db.update(UserInfo.TABLE_NAME, cv, str, null);
		 db.close();
	}
	
	public static void deleteUserInfo(String uid) {
		SQLiteDatabase db = WeiboApplication.dbHelper.getWritableDatabase();
		
		 db.delete(DBInfo.Table.USER_INFO_TABLE_NAME, "uid=?", new String[]{uid});
		 
		 db.close();
	}
}
