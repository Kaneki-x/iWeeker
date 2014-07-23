
package com.bobo.iweeker.Service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.bobo.iweeker.Model.Comment;
import com.bobo.iweeker.Model.Statuse;
import com.bobo.iweeker.Model.User;

public class CommentService {

    private static String created_at;
    private static int id;
    private static String text;
    private static String source;
    private static User user;
    private static String mid;
    private static String idstr;
    private static Statuse status;
    private static Comment reply_comment;

    public static List<Comment> getCommentsFromJSON(JSONArray jsonArray) {

        List<Comment> comments = new ArrayList<Comment>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Comment comment;
                created_at = jsonArray.getJSONObject(i).getString("created_at");
                id = jsonArray.getJSONObject(i).getInt("id");
                text = jsonArray.getJSONObject(i).getString("text");
                source = jsonArray.getJSONObject(i).getString("source");
                user = UserService
                        .getUserFromJSON(jsonArray.getJSONObject(i).getJSONObject("user"));
                mid = jsonArray.getJSONObject(i).getString("mid");
                idstr = jsonArray.getJSONObject(i).getString("idstr");

                if (jsonArray.getJSONObject(i).has("status")) {
                    status = StatuseService.getStatuseFromJSON(jsonArray.getJSONObject(i)
                            .getJSONObject("status"));
                } else {
                    status = null;
                }

                if (jsonArray.getJSONObject(i).has("reply_comment")) {
                    reply_comment = CommentService.getComentFromJSON(jsonArray.getJSONObject(i)
                            .getJSONObject("reply_comment"));
                } else {
                    reply_comment = null;
                }

                comment = new Comment(created_at, id, text, source, user, mid, idstr, status,
                        reply_comment);

                comments.add(comment);

            } catch (Exception e) {
                // TODO: handle exception
                Log.i("bobo", "getCommentsFromJSON--->" + e.getMessage());
            }
        }
        return comments;
    }

    public static Comment getComentFromJSON(JSONObject jsonObject) {
        try {
            Comment comment;
            created_at = jsonObject.getString("created_at");
            id = jsonObject.getInt("id");
            text = jsonObject.getString("text");
            source = jsonObject.getString("source");
            user = UserService.getUserFromJSON(jsonObject.getJSONObject("user"));
            mid = jsonObject.getString("mid");
            idstr = jsonObject.getString("idstr");

            if (jsonObject.has("status")) {
                status = StatuseService.getStatuseFromJSON(jsonObject.getJSONObject("status"));
            } else {
                status = null;
            }

            if (jsonObject.has("reply_comment")) {
                reply_comment = CommentService.getComentFromJSON(jsonObject
                        .getJSONObject("reply_comment"));
            } else {
                reply_comment = null;
            }
            comment = new Comment(created_at, id, text, source, user, mid, idstr, status,
                    reply_comment);

            return comment;
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("bobo", "getCommentFromJSON--->" + e.getMessage());
        }
        return null;
    }

    public static List<Comment> getCommentFromComments(List<Comment> comments, Statuse statuse) {
        List<Comment> statuse_comments = new ArrayList<Comment>();
        for (int i = 0; i < comments.size(); i++) {
            if(!comments.get(i).getText().contains(statuse.getText())) {
                statuse_comments.add(comments.get(i));
            }
        }

        return statuse_comments;
    }

    public static List<Comment> getRequestFromComments(List<Comment> comments, Statuse statuse){
        List<Comment> statuse_requests = new ArrayList<Comment>();
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getText().contains(statuse.getUser().getName())) {
                comments.get(i).setText("转发微博");
                statuse_requests.add(comments.get(i));
            }
        }

        return statuse_requests;
    }
}
