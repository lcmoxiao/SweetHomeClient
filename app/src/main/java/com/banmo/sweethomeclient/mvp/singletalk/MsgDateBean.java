package com.banmo.sweethomeclient.mvp.singletalk;

import android.graphics.Bitmap;

public class MsgDateBean {

    private final Bitmap headImg;
    private final byte[] content;
    private final String time;
    private final Integer srcUserID;
    private final int msgType; // 双数 0 2 4 分别为对方的文字图片语音信息， 奇数 1 3 5 分别为自己的信息

    public MsgDateBean(Bitmap headImg, byte[] content, String time, Integer srcUserID, int msgType) {
        this.headImg = headImg;
        this.content = content;

        this.time = time;
        this.srcUserID = srcUserID;
        this.msgType = msgType;

    }

    public Integer getSrcUserID() {
        return srcUserID;
    }

    public Bitmap getHeadImg() {
        return headImg;
    }

    public byte[] getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getMsgType() {
        return msgType;
    }


}
