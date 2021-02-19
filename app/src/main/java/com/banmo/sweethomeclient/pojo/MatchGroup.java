package com.banmo.sweethomeclient.pojo;

import java.io.Serializable;
import java.util.Date;

public class MatchGroup implements Serializable {
    private Integer matchgroupid;

    private String matchgroupname;

    private Date matchgroupcreatetime;

    public Integer getMatchgroupid() {
        return matchgroupid;
    }

    public void setMatchgroupid(Integer matchgroupid) {
        this.matchgroupid = matchgroupid;
    }

    public String getMatchgroupname() {
        return matchgroupname;
    }

    public void setMatchgroupname(String matchgroupname) {
        this.matchgroupname = matchgroupname;
    }

    public Date getMatchgroupcreatetime() {
        return matchgroupcreatetime;
    }

    public void setMatchgroupcreatetime(Date matchgroupcreatetime) {
        this.matchgroupcreatetime = matchgroupcreatetime;
    }
}