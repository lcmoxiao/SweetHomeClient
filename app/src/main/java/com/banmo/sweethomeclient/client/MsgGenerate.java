package com.banmo.sweethomeclient.client;

import com.banmo.sweethomeclient.proto.ConnectorMsg;
import com.google.protobuf.ByteString;


public class MsgGenerate {


    public static ConnectorMsg.cMsgInfo generateBeatMessage() {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.BEAT);

        ConnectorMsg.Beat beatMsg = ConnectorMsg.Beat.newBuilder()
                .setRequesttime(System.currentTimeMillis())
                .setResponsetime(1)
                .build();

        builder.setBeat(beatMsg);

        return builder.build();
    }

    public static ConnectorMsg.cMsgInfo generateTransMessage() {
        ConnectorMsg.cMsgInfo.Builder builder = ConnectorMsg.cMsgInfo.newBuilder();
        builder.setCMsgType(ConnectorMsg.cMsgInfo.CMsgType.TRANS);

        ConnectorMsg.Trans.Builder transMsg = ConnectorMsg.Trans.newBuilder();
        transMsg.setMsgID(1).setMsgMark(1)
                .setMsgType(ConnectorMsg.Trans.MsgType.WORD).
                setMsgContent(ByteString.copyFrom("wocao".getBytes()));
        builder.setTrans(transMsg);
        System.out.println(builder.build());
        return builder.build();
    }


}
