package com.banmo.sweethomeclient.client.handler;


import android.util.Log;

import com.banmo.sweethomeclient.client.ConnectorClient;
import com.banmo.sweethomeclient.client.UserInfos;
import com.banmo.sweethomeclient.client.service.UserService;
import com.banmo.sweethomeclient.mvp.home.match.MatchFragment;
import com.banmo.sweethomeclient.mvp.singletalk.SingleTalkActivity;
import com.banmo.sweethomeclient.proto.ConnectorMsg;

import java.lang.ref.WeakReference;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class MsgHandler extends ChannelHandlerAdapter {

    public static WeakReference<MatchFragment> matchFragment;
    public static WeakReference<SingleTalkActivity> singleTalkActivity;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ConnectorMsg.cMsgInfo cmsg = (ConnectorMsg.cMsgInfo) msg;

        ConnectorMsg.Trans trans = cmsg.getTrans();
        if (cmsg.getCMsgType() == ConnectorMsg.cMsgInfo.CMsgType.TRANS) {
            int msgMark = trans.getMsgMark();

            //Todo 信息同步，聊天同步。需要区分匹配 和好友。
            if (msgMark == 1) {
                Log.e("TAG", "消息成功发出");
            } else if (msgMark == 2) {
                Log.e("TAG", "接受到消息");
                ConnectorClient.getChannel().writeAndFlush(
                        cmsg.toBuilder().setTrans(
                                cmsg.getTrans().toBuilder().setMsgMark(3)
                        ).build());
            } else if (msgMark == 4) {
                Log.e("TAG", "消息成功被接受");
            }


        } else if (cmsg.getCMsgType() == ConnectorMsg.cMsgInfo.CMsgType.MATCH) {
            ConnectorMsg.FindMatch findmatch = cmsg.getFindmatch();
            int dstAgeRange = findmatch.getDstAgeRange();
            int dstSex = findmatch.getDstSex();
            int userid1 = findmatch.getUserInfo().getUserid();
            int userid2 = findmatch.getUserInfo().getSex();


            // DstSex == -3 代表匹配被对方断开
            if (dstAgeRange == -3) {
                singleTalkActivity.get().onMatchReject();
            } else if (dstAgeRange == -4) {
                // 好友请求处理
                // dstSex 0表示申请，-1表示同意，-2表示拒绝
                if (dstSex == 0) {
                    singleTalkActivity.get().onFriendWant(userid1, userid2);
                } else if (dstSex == -1) {
                    singleTalkActivity.get().onFriendAgree(userid1, userid2);
                } else if (dstSex == -2) {
                    singleTalkActivity.get().onFriendReject(userid1, userid2);
                }
            } else if (dstAgeRange == 0) {
                if (UserInfos.usingState == UserInfos.UsingState.SINGLE_MATCH) {
                    Log.e("MsgHandler", "匹配成功");
                    int dstUserId = trans.getDstUserid();
                    UserInfos.groupid = trans.getDstGroupid();
                    UserInfos.matchUser = UserService.getUserByUserID(dstUserId);
                    matchFragment.get().onMatchSuccess();
                }
            } else {
                Log.e("sdf", "未知MATCH信息" + cmsg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
