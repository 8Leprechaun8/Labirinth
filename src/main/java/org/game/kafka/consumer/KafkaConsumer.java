package org.game.kafka.consumer;

import org.game.dto.ActionData;
import org.game.service.LabirinthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(id = "multiGroup", topics = "actions")
public class KafkaConsumer {

    @Autowired
    private LabirinthService service;

    @KafkaHandler
    void listener(ActionData data) {
        data.setUsername("w");
        service.setAction(data);
    }
}
