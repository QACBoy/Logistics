package com.example.scandemo5.Utils;

import android.content.SharedPreferences;

import com.example.scandemo5.MyApp;

/**
 * Created by JC on 2017/10/19.
 */

public class User {

    private static User user;

    private SharedPreferences save;
    private String username;
    private String password;
    private String as_user;
    private String group_node_id;

    private User() {
        save = Global.getSharedPreferences();
        username = save.getString("username",null);
        password = save.getString("password",null);
        as_user = save.getString("as_user",null);
        group_node_id = save.getString("group_node_id",null);
    }

    public static User getUser() {
        if(user == null){
            synchronized (MyApp.getContext()) {
                user = new User();
            }
        }
        return user;
    }

    public void setUsername(String username){
        this.username = username;
        save.edit().putString("username",this.username).commit();
    }
    public String getUsername() {
        return username;
    }

    public void setPassword(String password){
        this.password = Encryption.md5(password);
        save.edit().putString("password",this.password).commit();
    }

    public String getPassword() {
        return password;
    }

    public void setAs_user(String as_user){
        this.as_user = as_user;
        save.edit().putString("as_user",this.as_user).commit();
    }
    public String getAs_user() {
        return as_user;
    }

    public void setGroup_node_id(String group_node_id){
        this.group_node_id = group_node_id;
        save.edit().putString("group_node_id",this.group_node_id).commit();
    }
    public String getGroup_node_id() {
        return group_node_id;
    }
}
