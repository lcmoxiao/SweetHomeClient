package com.banmo.sweethomeclient.mvp.singletalk;

public class MsgDateBean {

    private final byte[] content;
    private final String createTime;
    private final Integer friendid;
    private final int msgType; // 双数 0 2 4 分别为对方的文字图片语音信息， 奇数 1 3 5 分别为自己的信息
    private final String  recordTime;

    public MsgDateBean(byte[] content, String createTime, Integer friendid, int msgType, String recordTime) {
        this.content = content;
        this.createTime = createTime;
        this.friendid = friendid;
        this.msgType = msgType;
        this.recordTime = recordTime;
    }

    public Integer getFriendid() {
        return friendid;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public byte[] getContent() {
        return content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getMsgType() {
        return msgType;
    }


}
