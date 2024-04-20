package org.example.avrokafakasample.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object object) {
        Message<Object> message = MessageBuilder
                .withPayload(object)
                .setHeader(KafkaHeaders.TOPIC, "avro")
                .setHeader("AvroClassName", object.getClass().getName())
                .build();
        kafkaTemplate.send(message);
    }

    @PostConstruct
    public void init() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendTestMessage();
            }
        }, 0, 3000);
    }

    public void sendTestMessage() {
        TestDto name = new TestDto("avro-test", new Random().nextInt(101));
        sendMessage(name);
    }

}
