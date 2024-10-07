package com.dvk.ct250backend.domain.flight.config;

import lombok.Setter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;

@Setter
@Component
public class FlightUploadJobListener implements JobExecutionListener {

    private String flightFilePath;
    private String flightPricingFilePath;

    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        System.out.println("FLIGHT UPLOAD JOB STARTED");
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        File flightFile = new File(flightFilePath);
        File flightPricingFile = new File(flightPricingFilePath);
        System.out.println("Deleting flight file: " + flightFilePath);
        if (flightFile.delete()) {
            System.out.println("Flight file deleted successfully");
        } else {
            System.out.println("Failed to delete flight file");
        }
        System.out.println("Deleting flight pricing file: " + flightPricingFilePath);
        if (flightPricingFile.delete()) {
            System.out.println("Flight pricing file deleted successfully");
        } else {
            System.out.println("Failed to delete flight pricing file");
        }
        System.out.println("FLIGHT UPLOAD JOB COMPLETED");
    }
}
