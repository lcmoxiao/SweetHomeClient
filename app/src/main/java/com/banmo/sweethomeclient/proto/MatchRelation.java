package com.banmo.sweethomeclient.proto;

import java.io.Serializable;
import java.util.Date;

public class MatchRelation implements Serializable {
    private Integer matchrelationid;

    private Integer userid1;

    private Integer userid2;

    private Date matchrelationcreatetime;

    public Integer getMatchrelationid() {
        return matchrelationid;
    }

    public void setMatchrelationid(Integer matchrelationid) {
        this.matchrelationid = matchrelationid;
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

    public Date getMatchrelationcreatetime() {
        return matchrelationcreatetime;
    }

    public void setMatchrelationcreatetime(Date matchrelationcreatetime) {
        this.matchrelationcreatetime = matchrelationcreatetime;
    }

    @Override
    public String toString() {
        return "MatchRelation{" +
                "matchrelationid=" + matchrelationid +
                ", userid1=" + userid1 +
                ", userid2=" + userid2 +
                ", matchrelationcreatetime=" + matchrelationcreatetime +
                '}';
    }
}