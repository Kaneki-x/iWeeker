
package com.bobo.iweeker.Utils;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WeiboSharedPreferencesUtils {

    private static final String WEIBO_SAVE = "weibo_save";

    /**
     * 保存上一次微博信息
     * 
     * @param context
     * @param userInfo
     */
    public static void saveWeiboStatuses(Context context, JSONArray last_statuses) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putString("weibo", last_statuses.toString());
        editor.commit();
    }
    
    /**
     * 保存上一次提及微博信息
     * 
     * @param context
     * @param userInfo
     */
    public static void saveMentionWeiboStatuses(Context context, JSONArray last_statuses) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putString("weibo_mention", last_statuses.toString());
        editor.commit();
    }
    
    /**
     * 保存上一次提及微博信息
     * 
     * @param context
     * @param userInfo
     */
    public static void saveCommentMentionWeiboStatuses(Context context, JSONArray last_comments) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putString("comment_mention", last_comments.toString());
        editor.commit();
    }
    
    /**
     * 保存上一次提及微博信息
     * 
     * @param context
     * @param userInfo
     */
    public static void saveCollectWeiboStatuses(Context context, JSONArray last_statuses) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();

        editor.putString("weibo_collect", last_statuses.toString());
        editor.commit();
    }
    
    /**
     * 获取上一次微博信息
     * 
     * @param context
     * @return
     */
    public static JSONArray getMentionComment(Context context) {

        JSONArray jsonArray;
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        String last_comments = sharedPreferences.getString("comment_mention", "");

        if (last_comments.equals("")) {
            return null;
        } else {
            try {
                jsonArray = new JSONArray(last_comments);
                return jsonArray;
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }
    
    /**
     * 获取上一次微博信息
     * 
     * @param context
     * @return
     */
    public static JSONArray getMentionWeiboStatuses(Context context) {

        JSONArray jsonArray;
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        String last_statuses = sharedPreferences.getString("weibo_mention", "");

        if (last_statuses.equals("")) {
            return null;
        } else {
            try {
                jsonArray = new JSONArray(last_statuses);
                return jsonArray;
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }

    /**
     * 获取上一次微博信息
     * 
     * @param context
     * @return
     */
    public static JSONArray getWeiboStatuses(Context context) {

        JSONArray jsonArray;
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        String last_statuses = sharedPreferences.getString("weibo", "");

        if (last_statuses.equals("")) {
            return null;
        } else {
            try {
                jsonArray = new JSONArray(last_statuses);
                return jsonArray;
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }
    
    /**
     * 获取上一次微博信息
     * 
     * @param context
     * @return
     */
    public static JSONArray getCollectWeiboStatuses(Context context) {

        JSONArray jsonArray;
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEIBO_SAVE,
                Context.MODE_PRIVATE);

        String last_statuses = sharedPreferences.getString("weibo_collect", "");

        if (last_statuses.equals("")) {
            return null;
        } else {
            try {
                jsonArray = new JSONArray(last_statuses);
                return jsonArray;
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

    }
}
