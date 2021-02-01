package com.banmo.sweethomeclient.mvp.singletalk;

import android.graphics.Bitmap;

class MsgDateBean {

    private final Bitmap headImg;
    private final String msg;
    private final String time;
    private final int msgType;
    private final boolean isMyMsg;//是谁的信息
    private final Bitmap imgContent;

    public MsgDateBean(Bitmap headImg, String msg, String time, int msgType, boolean isMyMsg, Bitmap bitmap) {
        this.headImg = headImg;
        this.msg = msg;
        this.time = time;
        this.msgType = msgType;
        this.isMyMsg = isMyMsg;
        this.imgContent = bitmap;
    }

    public Bitmap getHeadImg() {
        return headImg;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public int getMsgType() {
        return msgType;
    }

    public Bitmap getImgContent() {
        return imgContent;
    }

    public boolean isMyMsg() {
        return isMyMsg;
    }
}
