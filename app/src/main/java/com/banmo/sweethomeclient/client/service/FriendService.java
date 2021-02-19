package com.banmo.sweethomeclient.client.service;

import android.util.Log;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.pojo.ConnectorMsg;
import com.banmo.sweethomeclient.pojo.FriendRelation;
import com.banmo.sweethomeclient.pojo.User;
import com.banmo.sweethomeclient.tool.GsonTools;
import com.banmo.sweethomeclient.tool.MsgGenerateTools;
import com.banmo.sweethomeclient.tool.OkHttpTools;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FriendService {

    private static final String TAG = "FriendService";


    public static List<User> getFriends(int userid) {
        List<User> users = parseUsers(OkHttpTools.get("friendrelation/toUser?userid=" + userid));
        Log.e(TAG, "getFriends: size=" + users.size());
        return users;
    }

    public static int getFriendSize(int userid) {
        String s = OkHttpTools.get("friendrelation/size?userid=" + userid);
        if (s == null) return 0;
        int size = Integer.parseInt(s);
        Log.e(TAG, "getFriendSize: size=" + size);
        return size;
    }

    public static List<FriendRelation> getFriendRelations(int userid) {
        return parseFriendRelation(OkHttpTools.get("friendrelation?userid=" + userid));
    }

    public static void deleteFriendRelation(FriendRelation friendRelation) {
        Log.e("TAG", "deleteFriendRelation: " + friendRelation);
        OkHttpTools.delete("friendrelation", GsonTools.getGson().toJson(friendRelation));
    }

    public static void wantToBeFriend(int userid1, int userid2) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                userid1,
                userid2,
                0,
                -4,
                0,
                0);
        ConnectorClient.getChannel().write(cMsgInfo);
    }

    public static void agreeToBeFriend(int userid1, int userid2) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                userid1,
                userid2,
                0,
                -4,
                -1,
                0);
        ConnectorClient.getChannel().write(cMsgInfo);
    }

    public static void disagreeToBeFriend(int userid1, int userid2) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateFindMatchMessage(
                userid1,
                userid2,
                0,
                -4,
                -2,
                0);
        ConnectorClient.getChannel().write(cMsgInfo);
    }


    public static List<User> parseUsers(String json) {
        Type type = new TypeToken<ArrayList<User>>() {
        }.getType();
        return GsonTools.getGson().fromJson(json, type);
    }

    public static List<FriendRelation> parseFriendRelation(String json) {
        Type type = new TypeToken<ArrayList<FriendRelation>>() {
        }.getType();
        return GsonTools.getGson().fromJson(json, type);
    }


}
