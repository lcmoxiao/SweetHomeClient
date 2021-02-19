package com.banmo.sweethomeclient.pojo;

import java.io.Serializable;
import java.util.List;

public class MatchGroupInfo implements Serializable {
    public MatchGroupInfo(List<Integer> userids, MatchGroup matchGroup) {
        this.userids = userids;
        this.matchGroup = matchGroup;
    }

    List<Integer> userids;
    MatchGroup matchGroup;

    public List<Integer> getUserids() {
        return userids;
    }

    public void setUserids(List<Integer> userids) {
        this.userids = userids;
    }

    public MatchGroup getMatchGroup() {
        return matchGroup;
    }

    public void setMatchGroup(MatchGroup matchGroup) {
        this.matchGroup = matchGroup;
    }

    public int getUserNum(){
        return userids.size();
    }

}