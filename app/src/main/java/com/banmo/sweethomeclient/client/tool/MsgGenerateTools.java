package com.banmo.sweethomeclient.client.tool;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.proto.ConnectorMsg;
import com.google.protobuf.ByteString;

import io.netty.channel.ChannelFuture;

public class MsgGenerateTools {

    public static int generateAgeRange(int min, int max) {
        return (min << 16) | max;
    }

    public static ChannelFuture sendTestTransMessage(int srcUserid, int dstUserid) {
        return ConnectorClient.getChannel()
                .writeAndFlush(generateTransMessage(1, srcUserid, dstUserid, 0, ConnectorMsg.Trans.MsgType.WORD, "1to2".getBytes()));
    }

    public static ChannelFuture sendTestConnectMessage(int userid) {
        return ConnectorClient.getChannel()
                .writeAndFlush(generateConnectMessage(userid));
    }

    public static ConnectorMsg.cMsgInfo generateFindMatchMessage(int userid, int sex, int age, int dstAgeRange, int dstSex, int dstHobby) {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.MATCH);
        ConnectorMsg.FindMatch.Builder findMatchBuilder = ConnectorMsg.FindMatch.newBuilder();
        ConnectorMsg.UserInfo.Builder userInfoBuilder = ConnectorMsg.UserInfo.newBuilder();
        userInfoBuilder.setUserid(userid).setAge(age).setSex(sex);
        findMatchBuilder.setUserInfo(userInfoBuilder.build())
                .setDstAgeRange(dstAgeRange)
                .setDstHobby(dstHobby)
                .setDstSex(dstSex);
        builder.setFindmatch(findMatchBuilder.build());
        return builder.build();
    }


    public static ConnectorMsg.cMsgInfo generateTransMessage(int msgID, int srcUserid, int dstUserid, int dstGroupid, ConnectorMsg.Trans.MsgType msgType, byte[] msgContent) {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.TRANS);
        ConnectorMsg.Trans.Builder transMsg = ConnectorMsg.Trans.newBuilder();
        transMsg.setMsgID(msgID)
                .setMsgMark(0)
                .setRetryTimes(0)
                .setSrcUserid(srcUserid)
                .setDstUserid(dstUserid)
                .setDstGroupid(dstGroupid)
                .setMsgType(msgType)
                .setMsgContent(ByteString.copyFrom(msgContent));
        builder.setTrans(transMsg);
        return builder.build();
    }

    public static ConnectorMsg.cMsgInfo generateConnectMessage(int userid) {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.CONNECT);
        ConnectorMsg.Connect.Builder connectMsg = ConnectorMsg.Connect.newBuilder();
        connectMsg.setUserid(userid)
                .setResponsetime(System.currentTimeMillis())
                .setMsgType(ConnectorMsg.Connect.MsgType.ONLINE);
        builder.setConnect(connectMsg);
        return builder.build();
    }

    public static ConnectorMsg.cMsgInfo generateBeatMessage() {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.BEAT);
        ConnectorMsg.Beat beatMsg = ConnectorMsg.Beat.newBuilder()
                .setRequesttime(System.currentTimeMillis())
                .setResponsetime(0)
                .build();
        builder.setBeat(beatMsg);
        return builder.build();
    }


}
