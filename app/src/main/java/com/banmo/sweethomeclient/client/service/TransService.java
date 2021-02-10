package com.banmo.sweethomeclient.client.service;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.tool.MsgGenerateTools;
import com.banmo.sweethomeclient.proto.ConnectorMsg;

public class TransService {

    public static void sendTextMsg(int msgID, int srcUserid, int dstUserid, int dstGroupid, byte[] content) {
        ConnectorMsg.cMsgInfo cMsgInfo = MsgGenerateTools.generateTransMessage
                (
                        msgID,
                        srcUserid,
                        dstUserid,
                        dstGroupid, //为0说明是单体消息，不为0说明是群组消息
                        ConnectorMsg.Trans.MsgType.WORD,
                        content
                );
        ConnectorClient.getChannel().write(cMsgInfo);
    }




}
