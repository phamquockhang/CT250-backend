package com.dvk.ct250backend.infrastructure.kafka.pdf;

import com.dvk.ct250backend.domain.booking.service.TicketService;
import com.dvk.ct250backend.infrastructure.service.EmailServiceImpl;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class PdfKafkaConsumer {
    private final EmailServiceImpl emailService;
    private final FileUtils fileUtils;
    private final TicketService ticketService;

    @KafkaListener(topics = "${kafka.pdf.topic}", concurrency = "${kafka.pdf.concurrency}", properties = {"spring.json.value.default.type=com.dvk.ct250backend.infrastructure.kafka.pdf.PdfMessageKafka"})
    @SneakyThrows
    public void listenPdfNotifications(@Payload PdfMessageKafka pdfMessage) {
        log.info("Received a PDF notification: {}", pdfMessage);

        if (pdfMessage != null) {
            String toAddress = (String) pdfMessage.getContext().get("toAddress");
            String subject = (String) pdfMessage.getContext().get("subject");
            String content = (String) pdfMessage.getContext().get("content");
            Object pdfDataObj = pdfMessage.getContext().get("pdfData");

            if (pdfDataObj instanceof String) {
                byte[] pdfData = Base64.getDecoder().decode((String) pdfDataObj);
                File tempFile = fileUtils.saveTempFile(pdfData, "temp_ticket.pdf");
                String cloudinaryUrl = fileUtils.uploadFileToCloudinary(tempFile);
                log.info("Uploaded PDF to Cloudinary: {}", cloudinaryUrl);
                emailService.sendEmailWithAttachmentAsync(toAddress, subject, content, tempFile)
                        .thenRun(() -> {
                            if (!tempFile.delete()) {
                                log.error("Failed to delete temporary PDF file: {}", tempFile.getAbsolutePath());
                            }
                        })
                        .exceptionally(ex -> {
                            log.error("Failed to send email with attachment", ex);
                            return null;
                        });
                ticketService.exportPdfForPassengersAndUploadCloudinary(Integer.parseInt(pdfMessage.getPayload().getBookingId()));
            } else {
                log.error("Invalid pdfData type: {}", pdfDataObj.getClass().getName());
            }
        } else {
            log.error("Received null or invalid Kafka message payload");
        }
    }

    @DltHandler
    public void retryPDFs(@Payload PdfMessageKafka message) {
        log.warn("Retry PDF message: {}", message);
    }
}