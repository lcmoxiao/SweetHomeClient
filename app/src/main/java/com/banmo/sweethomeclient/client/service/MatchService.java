package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.tool.GsonTools;
import com.banmo.sweethomeclient.client.tool.MsgGenerateTools;
import com.banmo.sweethomeclient.client.tool.OkHttpTools;
import com.banmo.sweethomeclient.proto.ConnectorMsg;
import com.banmo.sweethomeclient.proto.MatchGroup;
import com.banmo.sweethomeclient.proto.MatchGroupRelation;
import com.banmo.sweethomeclient.proto.MatchRelation;
import com.banmo.sweethomeclient.proto.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import client.tool.BirthdayUtils;

public class MatchService {

    public static MatchRelation getMatchRelationByUserid(int userid) {
        return parseMatchRelation(OkHttpTools.get("matchrelation?userid=" + userid)).get(0);
    }

    public static void deleteMatchRelation(MatchRelation matchRelation) {
        OkHttpTools.delete("matchrelation", GsonTools.getGson().toJson(matchRelation));
    }

    public static int getMatchGroupID(int userid) {
        return parseMatchGroupRelation(OkHttpTools.get("matchgroup/allgroup?userid=" + userid)).get(0).getMatchgroupid();
    }

    //获取userid匹配到的User的详细信息
    public static User getUserMatchedUserInfo(int userid) {
        MatchRelation matchRelation = getMatchRelationByUserid(userid);
        return UserService.getUserByUserID(matchRelation.getUserid2());
    }


    public static void deleteMatchGroup(int groupID) {
        MatchGroup matchGroup = new MatchGroup();
        matchGroup.setMatchgroupid(groupID);
        OkHttpTools.delete("matchgroup", GsonTools.getGson().toJson(matchGroup));
    }

    public static void rejectMatch(boolean isGroupMatching, int userid1, int userid2, int groupID) {
        if (!isGroupMatching) {
            //清除数据库中的matchRelation
            deleteMatchRelation(getMatchRelationByUserid(userid1));
            deleteMatchRelation(getMatchRelationByUserid(userid2));
            //告诉对方已经取消匹配
            ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage
                    (
                            userid2,
                            0,
                            0,
                            -3,
                            -2,
                            0);
            ConnectorClient.getChannel().write(cMsgInfo);
        } else {
            deleteMatchGroup(groupID);
            ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage
                    (
                            groupID,
                            0,
                            0,
                            -3,
                            -1,
                            0);
            ConnectorClient.getChannel().write(cMsgInfo);
        }
    }

    public static void cancelMatch(User user, int hobby, boolean isGroup) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                user.getUserid(),
                user.getUsersex(),
                BirthdayUtils.getAge(user.getUserbirth()),
                -2,
                isGroup ? -1 : -2,
                hobby);
        ConnectorClient.getChannel().write(cMsgInfo);
    }

    public static void singleMatch(User user, int minAge, int maxAge, int dstSex, int dstHobby) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                user.getUserid(),
                user.getUsersex(),
                BirthdayUtils.getAge(user.getUserbirth()),
                MsgGenerateTools.generateAgeRange(minAge, maxAge),
                dstSex,
                dstHobby);
        ConnectorClient.getChannel().write(cMsgInfo);
    }

    public static void groupMatch(User user, int dstHobby) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                user.getUserid(),
                user.getUsersex(),
                BirthdayUtils.getAge(user.getUserbirth()),
                -1,
                -1,
                dstHobby);
        ConnectorClient.getChannel().write(cMsgInfo);
    }


    public static List<MatchRelation> parseMatchRelation(String json) {
        Type type = new TypeToken<ArrayList<MatchRelation>>() {
        }.getType();
        return GsonTools.getGson().fromJson(json, type);
    }

    public static List<MatchGroupRelation> parseMatchGroupRelation(String json) {
        Type type = new TypeToken<ArrayList<MatchGroupRelation>>() {
        }.getType();
        return GsonTools.getGson().fromJson(json, type);
    }
}
