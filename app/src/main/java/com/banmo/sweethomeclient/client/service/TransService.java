package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.pojo.ConnectorMsg;
import com.banmo.sweethomeclient.tool.MsgGenerateTools;

public class TransService {

    public static void sendTextMsg(int msgID, int srcUserid, int dstUserid, int dstGroupid, byte[] content) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateTransMessage
                (
                        msgID,
                        srcUserid,
                        dstUserid,
                        dstGroupid, //为0说明是单体消息，不为0说明是群组消息
                        ConnectorMsg.Trans.MsgType.WORD,
                        content,
                        "无"
                );
        ConnectorClient.getChannel().write(cMsgInfo);
    }

    public static void sendImgMsg(int msgID, int srcUserid, int dstUserid, int dstGroupid, byte[] content) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateTransMessage
                (
                        msgID,
                        srcUserid,
                        dstUserid,
                        dstGroupid, //为0说明是单体消息，不为0说明是群组消息
                        ConnectorMsg.Trans.MsgType.PHOTO,
                        content,
                        "无"
                );
        ConnectorClient.getChannel().write(cMsgInfo);
    }


    public static void sendVoiceMsg(int msgID, int srcUserid, int dstUserid, int dstGroupid, byte[] content, String recordTime) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateTransMessage
                (
                        msgID,
                        srcUserid,
                        dstUserid,
                        dstGroupid, //为0说明是单体消息，不为0说明是群组消息
                        ConnectorMsg.Trans.MsgType.VOICE,
                        content,
                        recordTime
                );
        ConnectorClient.getChannel().write(cMsgInfo);
    }


}
