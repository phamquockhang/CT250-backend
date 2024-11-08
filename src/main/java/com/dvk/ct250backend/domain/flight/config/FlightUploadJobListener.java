package com.dvk.ct250backend.domain.flight.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;

@Setter
@Component
@Slf4j
public class FlightUploadJobListener implements JobExecutionListener {

    private String flightFilePath;
    private String flightPricingFilePath;
    private String seatAvailabilityFilePath;

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        log.info("FLIGHT UPLOAD JOB STARTED");
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        File flightFile = new File(flightFilePath);
        File flightPricingFile = new File(flightPricingFilePath);
        File seatAvailabilityFile = new File(seatAvailabilityFilePath);


        if (flightFile.delete()) {
            log.info("Flight file deleted successfully");
        } else {
            log.error("Failed to delete flight file");
        }

        if (flightPricingFile.delete()) {
            log.info("Flight pricing file deleted successfully");
        } else {
            log.error("Failed to delete flight pricing file");
        }

        if (seatAvailabilityFile.delete()) {
            log.info("Seat availability file deleted successfully");
        } else {
            log.error("Failed to delete seat availability file");
        }
        log.info("FLIGHT UPLOAD JOB COMPLETED");
    }
}
