
package com.bobo.iweeker.Model;

public class UserInfo implements Cloneable {

    private long id;
    private String uid;
    private String screen_name;
    private String access_token;
    private String expires_in;
    private String isDefault;
    private String user_icon;

    public static final String TABLE_NAME = "tb_user";
    public static final String ID = "_id";
    public static final String USER_NAME = "screen_name";
    public static final String USER_ID = "uid";
    public static final String TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String IS_DEFAULT = "isDefault";
    public static final String USER_ICON = "user_icon";

    public UserInfo(String uid, String screen_name, String access_token,
            String expires_in, String isDefault, String user_icon) {
        super();
        this.uid = uid;
        this.screen_name = screen_name;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.isDefault = isDefault;
        this.user_icon = user_icon;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public Object clone() {
        try {
            super.clone();
            return super.clone();
        } catch (Exception e) {
            return null;
        }
    }
}
