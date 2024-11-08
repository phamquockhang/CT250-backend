package com.dvk.ct250backend.infrastructure.kafka.mail;

import com.dvk.ct250backend.infrastructure.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RetriableException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaConsumer {
    private final EmailServiceImpl emailService;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2.0),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            autoCreateTopics = "true",
            include = {RetriableException.class}
    )
    @KafkaListener(topics = "${kafka.email.topic}", concurrency = "${kafka.email.concurrency}", properties = {"spring.json.value.default.type=com.dvk.ct250backend.infrastructure.kafka.mail.EmailMessageKafka"})
    @SneakyThrows
    public void listenEmailNotifications(@Payload EmailMessageKafka emailMessageKafka) {
        log.info("Received an email notification: {}", emailMessageKafka);
        if (emailMessageKafka != null) {
            Map<String, Object> contextMap = emailMessageKafka.getPayload().getContext();

            emailService.sendEmailAsync(
                    emailMessageKafka.getPayload().getToAddress(),
                    emailMessageKafka.getPayload().getSubject(),
                    emailMessageKafka.getPayload().getTemplateName(),
                    contextMap
            ).exceptionally(e -> {
                log.error("Failed to send email", e);
                return null;
            });
        } else {
            log.error("Received null or invalid Kafka message payload");
        }
    }

    @DltHandler
    public void retryMails(@Payload EmailMessageKafka message) {
        log.warn("Retry email message: {}", message);

    }
}