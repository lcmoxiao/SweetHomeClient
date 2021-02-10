package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.client.tool.GsonTools;
import com.banmo.sweethomeclient.client.tool.OkHttpTools;
import com.banmo.sweethomeclient.proto.User;
import com.google.gson.Gson;

import java.io.IOException;




public class LoginService {

    public static User login(String usermail, String userpassword) {
        Gson gson = GsonTools.getGson();
        User user = new User();
        user.setUsermail(usermail);
        user.setUserpassword(userpassword);
        String resp = null;
        try {
            resp = OkHttpTools.post("login", gson.toJson(user));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        User user1 = gson.fromJson(resp, User.class);
        System.out.println(resp);
        if (user1.getUserid() != null) return user1;
        else return null;
    }


}
