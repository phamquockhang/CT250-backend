package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.config.FlightUploadJobListener;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.mapper.FlightMapper;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    FlightMapper flightMapper;
    JobLauncher jobLauncher;
    Job flightUploadJob;
    FlightUploadJobListener flightUploadJobListener;
    FileUtils fileUtils;

    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flightMapper::toFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightDTO updateFlight(Integer id, FlightDTO flightDTO) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + id));
        flightMapper.updateFlightFromDTO(flight, flightDTO);
        return flightMapper.toFlightDTO(flightRepository.save(flight));
    }

    @Override
    public void uploadFlights(List<MultipartFile> files) throws IOException {
        String flightFilePath = "", flightPricingFilePath = "";
        for(MultipartFile file: files){
            if(Objects.equals(file.getOriginalFilename(), "flights.csv")){
                flightFilePath = fileUtils.saveTempFile(file);
            }else if(Objects.equals(file.getOriginalFilename(), "flight_pricing.csv")){
                flightPricingFilePath = fileUtils.saveTempFile(file);
            }
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("flightFile", flightFilePath)
                .addString("flightPricingFile", flightPricingFilePath)
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        flightUploadJobListener.setFlightFilePath(flightFilePath);
        flightUploadJobListener.setFlightPricingFilePath(flightPricingFilePath);

        try {
            jobLauncher.run(flightUploadJob, jobParameters);

        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
