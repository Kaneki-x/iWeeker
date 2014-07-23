
package com.bobo.iweeker.Model;

public class Comment {
    private String created_at; // 评论创建时间
    private int id; // 评论的ID
    private String text; // 评论的内容
    private String source; // 评论的来源
    private User user; // 评论作者的用户信息字段 详细
    private String mid; // 评论的MID
    private String idstr; // 字符串型的评论ID
    private Statuse status; // 评论的微博信息字段 详细
    private Comment reply_comment; // 评论来源评论，当本评论属于对另一评论的回复时返回此字段

    public Comment(String created_at, int id, String text, String source,
            User user, String mid, String idstr, Statuse status,
            Comment reply_comment) {
        super();
        this.created_at = created_at;
        this.id = id;
        this.text = text;
        this.source = source;
        this.user = user;
        this.mid = mid;
        this.idstr = idstr;
        this.status = status;
        this.reply_comment = reply_comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Statuse getStatus() {
        return status;
    }

    public void setStatus(Statuse status) {
        this.status = status;
    }

    public Comment getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment) {
        this.reply_comment = reply_comment;
    }
}
