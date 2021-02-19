package com.banmo.sweethomeclient.pojo;

import java.util.Date;

public class User {
    private Integer userid;

    private String username;

    private String userpassword;

    private Date usercreatetime;

    private Date userbirth;

    private Byte usersex;

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", userpassword='" + userpassword + '\'' +
                ", usercreatetime=" + usercreatetime +
                ", userbirth=" + userbirth +
                ", usersex=" + usersex +
                ", userphone='" + userphone + '\'' +
                ", usermail='" + usermail + '\'' +
                ", userfriendsize=" + userfriendsize +
                ", userstate=" + userstate +
                ", headimg='" + headimg + '\'' +
                '}';
    }

    private String userphone;

    private String usermail;

    private Integer userfriendsize;

    private Integer userstate;

    private String headimg;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public Date getUsercreatetime() {
        return usercreatetime;
    }

    public void setUsercreatetime(Date usercreatetime) {
        this.usercreatetime = usercreatetime;
    }

    public Date getUserbirth() {
        return userbirth;
    }

    public void setUserbirth(Date userbirth) {
        this.userbirth = userbirth;
    }

    public Byte getUsersex() {
        return usersex;
    }

    public void setUsersex(Byte usersex) {
        this.usersex = usersex;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUsermail() {
        return usermail;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public Integer getUserfriendsize() {
        return userfriendsize;
    }

    public void setUserfriendsize(Integer userfriendsize) {
        this.userfriendsize = userfriendsize;
    }

    public Integer getUserstate() {
        return userstate;
    }

    public void setUserstate(Integer userstate) {
        this.userstate = userstate;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }
}