package com.dvk.ct250backend.infrastructure.kafka.pdf;

import dev.nqvinh.kafka.common.kafka_lib.constant.EventType;
import dev.nqvinh.kafka.common.kafka_lib.util.MessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfKafkaProducer {

    @Value("${application-name}")
    private String serviceId;

    @Value("${kafka.pdf.topic}")
    private String pdfTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPdfEvent(PdfMessage pdfMessage) {
        try {
            var message = MessageBuilder.build(
                    serviceId,
                    EventType.EVENT,
                    "SEND_PDF",
                    pdfMessage
            );
            kafkaTemplate.send(pdfTopic, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
