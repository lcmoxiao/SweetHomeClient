package com.banmo.sweethomeclient.pojo;

import java.util.Date;

public class FriendRelation {
    private Integer friendrelationid;

    private Integer userid1;

    private Integer userid2;

    private Date friendrelationcreatetime;

    public Integer getFriendrelationid() {
        return friendrelationid;
    }

    public void setFriendrelationid(Integer friendrelationid) {
        this.friendrelationid = friendrelationid;
    }

    public Integer getUserid1() {
        return userid1;
    }

    public void setUserid1(Integer userid1) {
        this.userid1 = userid1;
    }

    public Integer getUserid2() {
        return userid2;
    }

    public void setUserid2(Integer userid2) {
        this.userid2 = userid2;
    }

    public Date getFriendrelationcreatetime() {
        return friendrelationcreatetime;
    }

    public void setFriendrelationcreatetime(Date friendrelationcreatetime) {
        this.friendrelationcreatetime = friendrelationcreatetime;
    }
}