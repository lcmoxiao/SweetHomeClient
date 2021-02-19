package com.banmo.sweethomeclient.client;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.HashMap;

public class HeadImgCache {


    private static final HashMap<Integer, Bitmap> headImgs = new HashMap<>();
    private static final String TAG = "HeadImgCache";

    public static void put(int userid, Bitmap bitmap) {
        Log.i(TAG, "put: userid" + userid);
        headImgs.put(userid, bitmap);
    }

    public static Bitmap get(int userid) {
        Bitmap bitmap = headImgs.get(userid);
        if (bitmap != null) Log.i(TAG, "get: userid" + userid + bitmap.toString());
        else Log.e(TAG, "get: userid bitmap is null");
        return bitmap;
    }


}
