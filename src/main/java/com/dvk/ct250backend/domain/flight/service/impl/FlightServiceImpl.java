package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.config.FlightUploadJobListener;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.request.FlightSearchRequest;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        for (MultipartFile file : files) {
            if (Objects.equals(file.getOriginalFilename(), "flights.csv")) {
                flightFilePath = fileUtils.saveTempFile(file);
            } else if (Objects.equals(file.getOriginalFilename(), "flight_pricing.csv")) {
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

    @Override
    public List<FlightDTO> searchFlights(FlightSearchRequest flightSearchRequest) {
        LocalDate departureDate = parseDate(flightSearchRequest.getDepartureDate());
        LocalDate arrivalDate = flightSearchRequest.getArrivalDate() != null ? parseDate(flightSearchRequest.getArrivalDate()) : null;
        Specification<Flight> spec = getFlightSpec(flightSearchRequest, departureDate, arrivalDate);
        List<Flight> flights = flightRepository.findAll(spec);
        return flights.stream()
                .map(flightMapper::toFlightDTO)
                .collect(Collectors.toList());
    }

//    private Specification<Flight> getFlightSpec(FlightSearchRequest flightSearchRequest, LocalDate departureDate, LocalDate arrivalDate) {
//        Specification<Flight> spec = Specification.where(null);
//
//        spec = spec.and(getDateRangeSpec("departureDateTime", departureDate));
//
//        if (arrivalDate != null) {
//            spec = spec.and(getDateRangeSpec("arrivalDateTime", arrivalDate));
//            spec = spec.and((root, query, criteriaBuilder) ->
//                    criteriaBuilder.greaterThan(root.get("departureDateTime"), departureDate.atStartOfDay()));
//        }
//
//        if (flightSearchRequest.getDepartureLocation() != null) {
//            spec = spec.and(getLocationSpec("route.departureAirport.airportId", flightSearchRequest.getDepartureLocation()));
//        }
//
//        if (flightSearchRequest.getArrivalLocation() != null) {
//            spec = spec.and(getLocationSpec("route.arrivalAirport.airportId", flightSearchRequest.getArrivalLocation()));
//        }
//
//        spec = spec.and((root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("flightStatus"), "SCHEDULED"));
//        spec = spec.and((root, query, criteriaBuilder) ->
//                criteriaBuilder.isTrue(root.get("airplane").get("ACTIVE")));
//
//        return spec;
//    }

    private Specification<Flight> getFlightSpec(FlightSearchRequest flightSearchRequest, LocalDate departureDate, LocalDate arrivalDate) {
        Specification<Flight> spec = Specification.where(getDateRangeSpec("departureDateTime", departureDate))
                .and(arrivalDate != null ? getDateRangeSpec("arrivalDateTime", arrivalDate)
                        .and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("departureDateTime"), departureDate.atStartOfDay())) : null)
                .and(flightSearchRequest.getDepartureLocation() != null ? getLocationSpec("route.departureAirport.airportId", flightSearchRequest.getDepartureLocation()) : null)
                .and(flightSearchRequest.getArrivalLocation() != null ? getLocationSpec("route.arrivalAirport.airportId", flightSearchRequest.getArrivalLocation()) : null)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("flightStatus"), "SCHEDULED"))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("airplane").get("status"), "ACTIVE"));

        return spec;
    }

    private Specification<Flight> getDateRangeSpec(String field, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(field), start, end);
    }

    private LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    private Specification<Flight> getLocationSpec(String field, String location) {
        return (root, query, criteriaBuilder) -> {
            if (field.equals("route.departureAirport.airportId")) {
                return criteriaBuilder.equal(root.get("route").get("departureAirport").get("airportId"), location);
            } else if (field.equals("route.arrivalAirport.airportId")) {
                return criteriaBuilder.equal(root.get("route").get("arrivalAirport").get("airportId"), location);
            }
            return null;
        };
    }
}
