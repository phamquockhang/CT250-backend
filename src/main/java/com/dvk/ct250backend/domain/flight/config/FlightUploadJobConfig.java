package com.dvk.ct250backend.domain.flight.config;

import com.dvk.ct250backend.domain.flight.batch.FlightPricingReader;
import com.dvk.ct250backend.domain.flight.batch.FlightReader;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
import com.dvk.ct250backend.domain.flight.repository.FlightPricingRepository;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class FlightUploadJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FlightRepository flightRepository;
    private final FlightPricingRepository flightPricingRepository;

    @Bean
    @StepScope
    public FlightReader flightReader(@Value("#{jobParameters['flightFile']}") String flightFile) {
        return new FlightReader(new FileSystemResource(flightFile));
    }

    @Bean
    public ItemProcessor<Flight,Flight> flightProcessor() {
        return flight -> flight;
    }

    @Bean
    public RepositoryItemWriter<Flight> flightWriter() {
        RepositoryItemWriter<Flight> writer = new RepositoryItemWriter<>();
        writer.setRepository(flightRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    @StepScope
    public FlightPricingReader flightPricingReader(@Value("#{jobParameters['flightPricingFile']}") String flightPricingFile) {
        return new FlightPricingReader(new FileSystemResource(flightPricingFile));
    }

    @Bean
    public ItemProcessor<FlightPricing,FlightPricing> flightPricingProcessor() {
        return flightPricing -> flightPricing;
    }

    @Bean
    public RepositoryItemWriter<FlightPricing> flightPricingWriter() {
        RepositoryItemWriter<FlightPricing> writer = new RepositoryItemWriter<>();
        writer.setRepository(flightPricingRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step importFlightStep(
        FlightReader flightReader,
        ItemProcessor<Flight,Flight> flightProcessor,
        RepositoryItemWriter<Flight> flightWriter
    ) {
        return new StepBuilder("Import Flights Step", jobRepository)
            .<Flight, Flight>chunk(50, transactionManager)
            .reader(flightReader)
            .processor(flightProcessor)
            .writer(flightWriter)
            .build();
    }

    @Bean
    public Step importFlightPricingStep(
        FlightPricingReader flightPricingReader,
        ItemProcessor<FlightPricing,FlightPricing> flightPricingProcessor,
        RepositoryItemWriter<FlightPricing> flightPricingWriter
    ) {
        return new StepBuilder("Import Flight Pricing Step", jobRepository)
            .<FlightPricing, FlightPricing>chunk(50, transactionManager)
            .reader(flightPricingReader)
            .processor(flightPricingProcessor)
            .writer(flightPricingWriter)
            .build();
    }

    @Bean
    public Job flightUploadJob(
        Step importFlightStep,
        Step importFlightPricingStep,
        FlightUploadJobListener flightUploadJobListener
    ) {
        return new JobBuilder("Flight Upload Job", jobRepository)
            .start(importFlightStep)
            .next(importFlightPricingStep)
            .listener(flightUploadJobListener)
            .build();
    }
}
