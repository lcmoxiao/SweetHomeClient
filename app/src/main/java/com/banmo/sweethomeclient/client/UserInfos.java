package com.banmo.sweethomeclient.client;

import android.util.Log;

import com.banmo.sweethomeclient.client.service.FriendService;
import com.banmo.sweethomeclient.proto.FriendRelation;
import com.banmo.sweethomeclient.proto.User;

import java.util.List;

public class UserInfos {

    private static final String TAG = "UserInfos";
    public static User user;
    public static UsingState usingState = UsingState.NULL;
    public static User matchUser;
    public static int groupid;
    // 好友信息和好友关系
    public static List<User> friends;
    public static List<FriendRelation> friendRelations;

    static {
        new Thread(ConnectorClient::connect).start();
    }

    public static void clearAllInfos() {
        user = null;
        usingState = UsingState.NULL;
        matchUser = null;
        groupid = 0;
        friends = null;
        friendRelations = null;
    }

    public static void flushFriendsInfo() {
        if (user != null) {
            List<User> friends = FriendService.getFriends(UserInfos.user.getUserid());
            List<FriendRelation> friendRelations = FriendService.getFriendRelations(UserInfos.user.getUserid());
            UserInfos.friends = friends;
            UserInfos.friendRelations = friendRelations;
        }
    }

    public static FriendRelation getFriendRelation(int friendID) {
        for (int i = 0; i < friends.size(); i++) {
            Log.e(TAG, "getFriendRelation: " + friends.get(i).getUserid());
            if (friends.get(i).getUserid() == friendID) return friendRelations.get(i);
        }
        return null;
    }

    public static void deleteFriendRelation(int friendID) {
        int i = 0;
        for (; i < friends.size(); i++) {
            if (friends.get(i).getUserid() == friendID) break;
        }
        Log.e(TAG, "deleteFriendRelation: index = " + i);
        Log.e(TAG, "deleteFriendRelation: index = " + friends.remove(i));
        Log.e(TAG, "deleteFriendRelation: index = " +   friendRelations.remove(i));
        ;
      ;
    }

    public static boolean isMatching() {
        return usingState == UsingState.SINGLE_MATCH || usingState == UsingState.GROUP_MATCH;
    }

    public static boolean isOnMatching() {
        return usingState == UsingState.ON_SINGLE_MATCH || usingState == UsingState.ON_GROUP_MATCH;
    }

    public static boolean isGroupMatching() {
        return usingState == UsingState.GROUP_MATCH || usingState == UsingState.ON_GROUP_MATCH;
    }

    public static User getUserInfoByUsernbame(String username) {
        for (User user : friends
        ) {
            if (user.getUsername().equals(username)) return user;
        }
        if (matchUser.getUsername().equals(username)) return matchUser;
        return null;
    }

    public static int getUserid() {
        return user.getUserid();
    }

    public static int getMatcherID() {
        return matchUser.getUserid();
    }

    public enum UsingState {
        NULL,
        SINGLE_MATCH,
        GROUP_MATCH,
        ON_SINGLE_MATCH,
        ON_GROUP_MATCH,
        FRIEND_TALK
    }
}
