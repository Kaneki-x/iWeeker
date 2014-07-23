
package com.bobo.iweeker.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bobo.iweeker.Model.UserInfo;

public class UserSharedPreferencesUtils {

    public static final String LOGIN_USER = "login_user";

    /**
     * 保存用户登录信息
     * 
     * @param context
     * @param userInfo
     */
    public static void saveLoginUser(Context context, UserInfo userInfo) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_USER,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putString("uid", userInfo.getUid());
        editor.putString("user_name", userInfo.getScreen_name());
        editor.putString("token", userInfo.getAccess_token());
        editor.putString("expires_in", userInfo.getExpires_in());
        editor.putString("is_default", userInfo.getIsDefault());
        editor.putString("expires_in", userInfo.getExpires_in());
        editor.putString("user_icon", userInfo.getUser_icon());
        editor.commit();
    }

    /**
     * 获取登录用户信息
     * 
     * @param context
     * @return
     */
    public static UserInfo getUserInfo(Context context) {

        UserInfo userInfo;
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_USER,
                Context.MODE_PRIVATE);

        String uid = sharedPreferences.getString("uid", "");
        String user_name = sharedPreferences.getString("user_name", "");
        String token = sharedPreferences.getString("token", "");
        String expires_in = sharedPreferences.getString("expires_in", "");
        String isDefault = sharedPreferences.getString("is_default", "1");
        String user_icon = sharedPreferences.getString("user_icon", "");

        if (uid.equals("")) {
            return null;
        } else {
            userInfo = new UserInfo(uid, user_name, token, expires_in, isDefault, user_icon);
            return userInfo;
        }

    }
}
