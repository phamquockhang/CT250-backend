package com.dvk.ct250backend.infrastructure.kafka.mail;

import dev.nqvinh.kafka.common.kafka_lib.constant.EventType;
import dev.nqvinh.kafka.common.kafka_lib.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailKafkaProducer {

    @Value("${application-name}")
    private String serviceId;

    @Value("${kafka.email.topic}")
    private String emailTopic;


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmailEvent(EmailMessage emailMessage) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    "SEND_MAIL",
                    emailMessage
            );
            kafkaTemplate.send(emailTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}