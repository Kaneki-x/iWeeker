
package com.bobo.iweeker.Service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bobo.iweeker.Model.User;

public class UserService {

    public static User getUserFromJSON(JSONObject jsonObject) {
        try {
            User user;
            int id = jsonObject.getInt("id");
            String screen_name = jsonObject.getString("screen_name");
            String name = jsonObject.getString("name");
            int province = jsonObject.getInt("province");
            int city = jsonObject.getInt("city");
            String location = jsonObject.getString("location");
            String description = jsonObject.getString("description");
            String url = jsonObject.getString("url");
            String profile_image_url = jsonObject.getString("profile_image_url");
            String domain = jsonObject.getString("domain");
            String gender = jsonObject.getString("gender");
            int followers_count = jsonObject.getInt("followers_count");
            int friends_count = jsonObject.getInt("friends_count");
            int statuses_count = jsonObject.getInt("statuses_count");
            int favourites_count = jsonObject.getInt("favourites_count");
            int verified_type = jsonObject.getInt("verified_type");
            String created_at = jsonObject.getString("created_at");
            boolean following = jsonObject.getBoolean("following");
            boolean allow_all_act_msg = jsonObject.getBoolean("allow_all_act_msg");
            boolean geo_enabled = jsonObject.getBoolean("geo_enabled");
            boolean verified = jsonObject.getBoolean("verified");
            String remark = jsonObject.getString("remark");
            boolean allow_all_comment = jsonObject.getBoolean("allow_all_comment");
            String avatar_large = jsonObject.getString("avatar_large");
            String verified_reason = jsonObject.getString("verified_reason");
            boolean follow_me = jsonObject.getBoolean("follow_me");
            int online_status = jsonObject.getInt("online_status");
            int bi_followers_count = jsonObject.getInt("bi_followers_count");

            user = new User(id, screen_name, name, province, city, location, description, url,
                    profile_image_url, domain, gender, followers_count, friends_count,
                    statuses_count, favourites_count, created_at, following, allow_all_act_msg,
                    geo_enabled, verified, verified_type, remark, allow_all_comment, avatar_large,
                    verified_reason, follow_me, online_status, bi_followers_count);

            return user;
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("bobo", "getUserFromJSON--->" + e.getMessage());
        }
        return null;
    }
    
    public static List<User> getUsersFromJSON(JSONArray jsonArray) {

        List<User> users = new ArrayList<User>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                User user;
                int id = jsonArray.getJSONObject(i).getInt("id");
                String screen_name = jsonArray.getJSONObject(i).getString("screen_name");
                String name = jsonArray.getJSONObject(i).getString("name");
                int province = jsonArray.getJSONObject(i).getInt("province");
                int city = jsonArray.getJSONObject(i).getInt("city");
                String location = jsonArray.getJSONObject(i).getString("location");
                String description = jsonArray.getJSONObject(i).getString("description");
                String url = jsonArray.getJSONObject(i).getString("url");
                String profile_image_url = jsonArray.getJSONObject(i).getString("profile_image_url");
                String domain = jsonArray.getJSONObject(i).getString("domain");
                String gender = jsonArray.getJSONObject(i).getString("gender");
                int followers_count = jsonArray.getJSONObject(i).getInt("followers_count");
                int friends_count = jsonArray.getJSONObject(i).getInt("friends_count");
                int statuses_count = jsonArray.getJSONObject(i).getInt("statuses_count");
                int favourites_count = jsonArray.getJSONObject(i).getInt("favourites_count");
                int verified_type = jsonArray.getJSONObject(i).getInt("verified_type");
                String created_at = jsonArray.getJSONObject(i).getString("created_at");
                boolean following = jsonArray.getJSONObject(i).getBoolean("following");
                boolean allow_all_act_msg = jsonArray.getJSONObject(i).getBoolean("allow_all_act_msg");
                boolean geo_enabled = jsonArray.getJSONObject(i).getBoolean("geo_enabled");
                boolean verified = jsonArray.getJSONObject(i).getBoolean("verified");
                String remark = jsonArray.getJSONObject(i).getString("remark");
                boolean allow_all_comment = jsonArray.getJSONObject(i).getBoolean("allow_all_comment");
                String avatar_large = jsonArray.getJSONObject(i).getString("avatar_large");
                String verified_reason = jsonArray.getJSONObject(i).getString("verified_reason");
                boolean follow_me = jsonArray.getJSONObject(i).getBoolean("follow_me");
                int online_status = jsonArray.getJSONObject(i).getInt("online_status");
                int bi_followers_count = jsonArray.getJSONObject(i).getInt("bi_followers_count");

                user = new User(id, screen_name, name, province, city, location, description, url,
                        profile_image_url, domain, gender, followers_count, friends_count,
                        statuses_count, favourites_count, created_at, following, allow_all_act_msg,
                        geo_enabled, verified, verified_type, remark, allow_all_comment, avatar_large,
                        verified_reason, follow_me, online_status, bi_followers_count);

                users.add(user);
            } catch (Exception e) {
                // TODO: handle exception
                Log.i("bobo", "getUsersFromJSON--->" + e.getMessage());
            }
        }
        return users;
    }
}
