package com.dvk.ct250backend.infrastructure.kafka.pdf;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfMessage {
    private String bookingId;
    private Map<String, Object> context;
}
