
package com.bobo.iweeker.Service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.bobo.iweeker.Model.Geo;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.User;

public class StatuseService {

    public static List<Statuse> getStatusesFromJSON(JSONArray jsonArray) {
        String created_at;
        int id;
        int mid;
        String idstr;
        String text;
        String source;
        boolean favorited;
        boolean truncated;
        String in_reply_to_status_id;
        String in_reply_to_user_id;
        String in_reply_to_screen_name;
        String thumbnail_pic;
        String bmiddle_pic;
        String original_pic;
        String[] pic_urls;
        Geo geo;
        User user;
        Statuse retweeted_status;
        int reposts_count;
        int comments_count;
        int attitudes_count;
        int mlevel;

        List<Statuse> statuses = new ArrayList<Statuse>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Statuse statuse;
                created_at = jsonArray.getJSONObject(i).getString("created_at");
                id = jsonArray.getJSONObject(i).getInt("id");
                mid = jsonArray.getJSONObject(i).getInt("mid");
                idstr = jsonArray.getJSONObject(i).getString("idstr");
                text = jsonArray.getJSONObject(i).getString("text");
                source = jsonArray.getJSONObject(i).getString("source");
                favorited = jsonArray.getJSONObject(i).getBoolean("favorited");
                truncated = jsonArray.getJSONObject(i).getBoolean("truncated");

                in_reply_to_status_id = jsonArray.getJSONObject(i).getString(
                        "in_reply_to_status_id");
                in_reply_to_user_id = jsonArray.getJSONObject(i).getString("in_reply_to_user_id");
                in_reply_to_screen_name = jsonArray.getJSONObject(i).getString(
                        "in_reply_to_screen_name");
                if (jsonArray.getJSONObject(i).has("thumbnail_pic")) {
                    thumbnail_pic = jsonArray.getJSONObject(i).getString("thumbnail_pic");
                    JSONArray picArray = jsonArray.getJSONObject(i).getJSONArray("pic_urls");
                    pic_urls = new String[picArray.length()];
                    for (int j = 0; j < pic_urls.length; j++) {
                        pic_urls[j] = picArray.getJSONObject(j).getString("thumbnail_pic");
                        pic_urls[j] = pic_urls[j].replace("thumbnail", "bmiddle");
                    }
                } else {
                    thumbnail_pic = null;
                    pic_urls = null;
                }

                if (jsonArray.getJSONObject(i).has("bmiddle_pic")) {
                    bmiddle_pic = jsonArray.getJSONObject(i).getString("bmiddle_pic");
                } else {
                    bmiddle_pic = null;
                }

                if (jsonArray.getJSONObject(i).has("original_pic")) {
                    original_pic = jsonArray.getJSONObject(i).getString("original_pic");
                } else {
                    original_pic = null;
                }

                geo = null;
                user = UserService
                        .getUserFromJSON(jsonArray.getJSONObject(i).getJSONObject("user"));
                if (jsonArray.getJSONObject(i).has("retweeted_status")) {
                    retweeted_status = getStatuseFromJSON(jsonArray.getJSONObject(i).getJSONObject(
                            "retweeted_status"));
                } else {
                    retweeted_status = null;
                }
                reposts_count = jsonArray.getJSONObject(i).getInt("reposts_count");
                comments_count = jsonArray.getJSONObject(i).getInt("comments_count");
                attitudes_count = jsonArray.getJSONObject(i).getInt("attitudes_count");
                mlevel = jsonArray.getJSONObject(i).getInt("mlevel");
                statuse = new Statuse(created_at, id, mid, idstr, text, source, favorited,
                        truncated, in_reply_to_status_id, in_reply_to_user_id,
                        in_reply_to_screen_name, thumbnail_pic, bmiddle_pic, original_pic, pic_urls, geo,
                        user, retweeted_status, reposts_count, comments_count, attitudes_count,
                        mlevel);

                statuses.add(statuse);

            } catch (Exception e) {
                // TODO: handle exception
                Log.i("bobo", "getStatusesFromJSON--->" + e.getMessage());
            }
        }
        return statuses;
    }

    public static Statuse getStatuseFromJSON(JSONObject jsonObject) {
        Statuse statuse;
        String created_at;
        int id;
        int mid;
        String idstr;
        String text;
        String source;
        boolean favorited;
        boolean truncated;
        String in_reply_to_status_id;
        String in_reply_to_user_id;
        String in_reply_to_screen_name;
        String thumbnail_pic;
        String bmiddle_pic;
        String original_pic;
        String[] pic_urls;
        Geo geo;
        User user;
        Statuse retweeted_status;
        int reposts_count;
        int comments_count;
        int attitudes_count;
        int mlevel;

        try {
        	if(jsonObject.has("deleted")) {
	            return null;
        	} else {
	            created_at = jsonObject.getString("created_at");
	            id = jsonObject.getInt("id");
	            mid = jsonObject.getInt("mid");
	            idstr = jsonObject.getString("idstr");
	            text = jsonObject.getString("text");
	            source = jsonObject.getString("source");
	            favorited = jsonObject.getBoolean("favorited");
	            truncated = jsonObject.getBoolean("truncated");
	            in_reply_to_status_id = jsonObject.getString("in_reply_to_status_id");
	            in_reply_to_user_id = jsonObject.getString("in_reply_to_user_id");
	            in_reply_to_screen_name = jsonObject.getString("in_reply_to_screen_name");
	
	            if (jsonObject.has("thumbnail_pic")) {
	                thumbnail_pic = jsonObject.getString("thumbnail_pic");
	                JSONArray picArray = jsonObject.getJSONArray("pic_urls");
	                pic_urls = new String[picArray.length()];
	                for (int j = 0; j < pic_urls.length; j++) {
	                    pic_urls[j] = picArray.getJSONObject(j).getString("thumbnail_pic");
	                    pic_urls[j] = pic_urls[j].replace("thumbnail", "bmiddle");
	                }
	            } else {
	                thumbnail_pic = null;
	                pic_urls = null;
	            }
	
	            if (jsonObject.has("bmiddle_pic")) {
	                bmiddle_pic = jsonObject.getString("bmiddle_pic");
	            } else {
	                bmiddle_pic = null;
	            }
	
	            if (jsonObject.has("original_pic")) {
	                original_pic = jsonObject.getString("original_pic");
	            } else {
	                original_pic = null;
	            }
	            geo = null;
	            user = UserService.getUserFromJSON(jsonObject.getJSONObject("user"));
	            retweeted_status = null;
	            reposts_count = jsonObject.getInt("reposts_count");
	            comments_count = jsonObject.getInt("comments_count");
	            attitudes_count = jsonObject.getInt("attitudes_count");
	            mlevel = jsonObject.getInt("mlevel");
	
	            statuse = new Statuse(created_at, id, mid, idstr, text, source, favorited, truncated,
	                    in_reply_to_status_id, in_reply_to_user_id, in_reply_to_screen_name,
	                    thumbnail_pic, bmiddle_pic, original_pic, pic_urls, geo, user, retweeted_status,
	                    reposts_count, comments_count, attitudes_count, mlevel);
	
	            return statuse;
        	}
        } catch (JSONException e) {
            // TODO: handle exception
            Log.i("bobo", "getStatuseFromJSON--->" + e.toString());
        }
        return null;
    }
}
