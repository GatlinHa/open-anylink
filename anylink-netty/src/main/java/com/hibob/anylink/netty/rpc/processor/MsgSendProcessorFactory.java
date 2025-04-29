package com.hibob.anylink.netty.rpc.processor;

import com.hibob.anylink.common.protobuf.MsgType;
import com.hibob.anylink.netty.utils.SpringContextUtil;

public class MsgSendProcessorFactory {
    public static MsgSendProcessor getProcessor(MsgType msgType) {
        switch (msgType) {
            case SYS_GROUP_CREATE:
            case SYS_GROUP_ADD_MEMBER:
            case SYS_GROUP_DEL_MEMBER:
            case SYS_GROUP_SET_ADMIN:
            case SYS_GROUP_CANCEL_ADMIN:
            case SYS_GROUP_SET_ALL_MUTED:
            case SYS_GROUP_CANCEL_ALL_MUTED:
            case SYS_GROUP_SET_JOIN_APPROVAL:
            case SYS_GROUP_CANCEL_JOIN_APPROVAL:
            case SYS_GROUP_SET_HISTORY_BROWSE:
            case SYS_GROUP_CANCEL_HISTORY_BROWSE:
            case SYS_GROUP_UPDATE_ANNOUNCEMENT:
            case SYS_GROUP_UPDATE_NAME:
            case SYS_GROUP_UPDATE_AVATAR:
            case SYS_GROUP_LEAVE:
            case SYS_GROUP_DROP:
            case SYS_GROUP_OWNER_TRANSFER:
            case SYS_GROUP_UPDATE_MEMBER_MUTED:
                return SpringContextUtil.getBean(GroupSystemMsgSendProcessor.class);
            case REVOKE:
                return SpringContextUtil.getBean(RevokeMsgSendProcessor.class);
            default:
                return null;
        }
    }
}
