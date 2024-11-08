package com.dvk.ct250backend.infrastructure.kafka.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage{
    private String toAddress;
    private String subject;
    private String templateName;
    private Map<String, Object> context;
    private String pdfFilePath;
}
