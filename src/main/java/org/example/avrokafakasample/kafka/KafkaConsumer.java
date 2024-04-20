package org.example.avrokafakasample.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "avro")
    public void listen(ConsumerRecord<String, Object> consumerRecord) {
        System.out.println("Received: " + consumerRecord);
        log.info("from avro : {}", consumerRecord.value());
    }
}
