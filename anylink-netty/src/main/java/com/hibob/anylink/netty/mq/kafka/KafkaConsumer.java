package com.hibob.anylink.netty.mq.kafka;

import com.hibob.anylink.common.constants.Const;
import com.hibob.anylink.common.constants.RedisKey;
import com.hibob.anylink.common.utils.CommonUtil;
import com.hibob.anylink.common.protobuf.Msg;
import com.hibob.anylink.netty.server.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "${websocket.consumer.topic}")
    public void onMessage(ConsumerRecord<String, Msg> record) {
        Msg msg = record.value();
        if (msg.getHeader().getMagic() != Const.MAGIC) {
            log.error("magic is not correct, the message is: \n{}", msg);
            return;
        }

        String toId = msg.getBody().getToId();
        String toClient = msg.getBody().getToClient();
        String routeKey = RedisKey.NETTY_GLOBAL_ROUTE + CommonUtil.conUniqueId(toId, toClient);
        WebSocketServer.getLocalRoute().get(routeKey).writeAndFlush(msg);
    }

}
