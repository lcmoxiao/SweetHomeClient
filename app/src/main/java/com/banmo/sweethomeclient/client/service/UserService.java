package com.banmo.sweethomeclient.client.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import com.banmo.sweethomeclient.client.HeadImgCache;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.pojo.UpdateIMG;
import com.banmo.sweethomeclient.pojo.User;
import com.banmo.sweethomeclient.tool.GsonTools;
import com.banmo.sweethomeclient.tool.OkHttpTools;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

public class UserService {

    private static final String TAG = "UserService";


    public static User getUserByUserID(int userID) {
        String s = OkHttpTools.get("user/id?userid=" + userID);
        return GsonTools.getGson().fromJson(s, User.class);
    }

    public static void updateUserInfo(User user) {
        Log.e(TAG, "updateUserInfo: " + user);
        OkHttpTools.put("user", GsonTools.getGson().toJson(user));
    }

    public static void destroyUser(User user) {
        OkHttpTools.delete("user", GsonTools.getGson().toJson(user));
    }

    public static void postUserHeadImg(UpdateIMG updateIMG) {
        try {
            OkHttpTools.post("user/headImg", GsonTools.getGson().toJson(updateIMG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getHeadImgByUseridAndFlushCache(int userid) {
        Log.e(TAG, "getHeadImgByUseridAndFlushCache: userid" + userid);
        Bitmap bitmap;
        User user = UserInfos.getUserInfoByUserid(userid);
        String headimg = null;
        if (user != null) {
            headimg = user.getHeadimg();
        }
        bitmap = getHeadImg(headimg);
        HeadImgCache.put(userid, bitmap);
        return bitmap;
    }

    public static Bitmap getHeadImgByUserid(int userid) {
        Bitmap bitmap = HeadImgCache.get(userid);
        if (bitmap == null) {
            User user = UserInfos.getUserInfoByUserid(userid);
            String headimg = null;
            if (user != null) {
                headimg = user.getHeadimg();
            } else {
                Log.e(TAG, "getHeadImgByUserid: user is null");
            }
            bitmap = getHeadImg(headimg);
            HeadImgCache.put(userid, bitmap);
        }
        return bitmap;
    }

    private static Bitmap getHeadImg(String src) {
        Log.e(TAG, "getHeadImg: " + src);
        String s = OkHttpTools.get("user/headImg?headImg=" + src);
        Bitmap bmp;
        if (s == null || s.equals("null") || s.equals("")) {
            bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
        } else {
            byte[] bytes = s.getBytes(ISO_8859_1);
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return bmp;
    }
}
