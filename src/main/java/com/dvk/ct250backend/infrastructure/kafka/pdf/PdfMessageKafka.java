package com.dvk.ct250backend.infrastructure.kafka.pdf;

import dev.nqvinh.kafka.common.kafka_lib.dto.KafkaMessage;

import java.util.Map;

public class PdfMessageKafka extends KafkaMessage<PdfMessage> {
    public Map<String, Object> getContext() {
        return getPayload().getContext();
    }
}
