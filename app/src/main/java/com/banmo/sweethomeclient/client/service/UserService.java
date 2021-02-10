package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.client.tool.GsonTools;
import com.banmo.sweethomeclient.client.tool.OkHttpTools;
import com.banmo.sweethomeclient.proto.User;

public class UserService {

    public static User getUserByUserID(int userID) {
        String s = OkHttpTools.get("user/id?userid=" + userID);
        User user = GsonTools.getGson().fromJson(s, User.class);
        return user;
    }

}
