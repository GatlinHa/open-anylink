package com.hibob.anylink.netty.mq.kafka;

import com.hibob.anylink.common.protobuf.Msg;
import org.apache.kafka.common.serialization.Serializer;

public class SerializerMsg implements Serializer<Msg> {
    @Override
    public byte[] serialize(String s, Msg msg) {
        return msg.toByteArray();
    }
}
