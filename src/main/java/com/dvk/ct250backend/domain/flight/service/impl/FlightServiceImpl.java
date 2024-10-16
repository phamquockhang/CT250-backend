package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.flight.config.FlightUploadJobListener;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.FlightOverview;
import com.dvk.ct250backend.domain.flight.dto.request.FlightSearchRequest;
import com.dvk.ct250backend.domain.flight.entity.Flight;
import com.dvk.ct250backend.domain.flight.entity.FlightPricing;
import com.dvk.ct250backend.domain.flight.mapper.FlightMapper;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    RequestParamUtils requestParamUtils;

    @Override
    public List<FlightDTO> getAllFlights() {
        return flightRepository.findAll().stream()
                .map(flightMapper::toFlightDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightDTO updateFlight(String id, FlightDTO flightDTO) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + id));
        flightMapper.updateFlightFromDTO(flight, flightDTO);
        return flightMapper.toFlightDTO(flightRepository.save(flight));
    }

    @Override
    public void uploadFlights(List<MultipartFile> files) throws IOException {
        String flightFilePath = "";
        String flightPricingFilePath = "";
        String seatAvailabilityFilePath = "";
        for (MultipartFile file : files) {
            if (Objects.equals(file.getOriginalFilename(), "flights.csv")) {
                flightFilePath = fileUtils.saveTempFile(file);
            } else if (Objects.equals(file.getOriginalFilename(), "flight_pricing.csv")) {
                flightPricingFilePath = fileUtils.saveTempFile(file);
            } else if (Objects.equals(file.getOriginalFilename(), "seat_availability.csv")) {
                seatAvailabilityFilePath = fileUtils.saveTempFile(file);
            }
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("flightFile", flightFilePath)
                .addString("flightPricingFile", flightPricingFilePath)
                .addString("seatAvailabilityFile", seatAvailabilityFilePath)
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();
        flightUploadJobListener.setFlightFilePath(flightFilePath);
        flightUploadJobListener.setFlightPricingFilePath(flightPricingFilePath);
        flightUploadJobListener.setSeatAvailabilityFilePath(seatAvailabilityFilePath);

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

    @Override
    public List<FlightOverview> getFlightOverview(FlightSearchRequest flightSearchRequest) {
        LocalDate start = parseDate(flightSearchRequest.getDepartureDate());
        LocalDate end = parseDate(flightSearchRequest.getArrivalDate());
        Specification<Flight> spec = getFlightRangeSpec(flightSearchRequest, start, end);
        List<Flight> flights = flightRepository.findAll(spec);
        TreeMap<String, List<Flight>> flightMap = new TreeMap<>();
        flights.forEach(flight -> {
            String date = formatDate(flight.getDepartureDateTime().toLocalDate());
            if (flightMap.containsKey(date)) {
                flightMap.get(date).add(flight);
            } else {
                flightMap.put(date, new ArrayList<>(List.of(flight)));
            }
        });
        LocalDate currentDate = start;
        while (currentDate.isBefore(end) || currentDate.isEqual(end)) {
            String date = formatDate(currentDate);
//            if (!flightMap.containsKey(date)) {
//                flightMap.put(date, List.of());
//            }
            flightMap.putIfAbsent(date, new ArrayList<>());
            currentDate = currentDate.plusDays(1);
        }
        return flightMap.entrySet().stream()
                .map(entry -> {
                    FlightOverview flightOverview = new FlightOverview();
                    flightOverview.setDate(entry.getKey());
                    if(entry.getValue().isEmpty()) {
                        flightOverview.setMinPriceOfDay(0.0);
                        flightOverview.setHasFlight(false);
                    } else {
                        flightOverview.setHasFlight(true);
                        double minPrice = entry.getValue().stream()
                                .map(flight -> flight.getFlightPricing().stream()
                                        .map(FlightPricing::getTicketPrice)
                                        .min(Double::compareTo)
                                        .orElse(0.0))
                                .min(Double::compareTo)
                                .orElse(0.0);
                        flightOverview.setMinPriceOfDay(minPrice);
                    }
                    return flightOverview;
                }).toList();

    }

    @Override
    public Page<FlightDTO> getFlights(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Flight> flightPage = flightRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(flightPage.getTotalPages())
                .total(flightPage.getTotalElements())
                .build();
        return Page.<FlightDTO>builder()
                .meta(meta)
                .content(flightPage.getContent().stream()
                        .map(flightMapper::toFlightDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public FlightDTO getFlightById(String id) throws ResourceNotFoundException {
        return flightRepository.findById(id)
                .map(flightMapper::toFlightDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found for this id :: " + id));
    }

    private Specification<Flight> getFlightSpec(FlightSearchRequest flightSearchRequest, LocalDate departureDate, LocalDate arrivalDate) {
        return Specification.where(getDateRangeSpec("departureDateTime", departureDate))
                .and(arrivalDate != null ? getDateRangeSpec("arrivalDateTime", arrivalDate)
                        .and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("departureDateTime"), departureDate.atStartOfDay())) : null)
                .and(flightSearchRequest.getDepartureLocation() != null ? getLocationSpec("route.departureAirport.airportId", flightSearchRequest.getDepartureLocation()) : null)
                .and(flightSearchRequest.getArrivalLocation() != null ? getLocationSpec("route.arrivalAirport.airportId", flightSearchRequest.getArrivalLocation()) : null)
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("flightStatus"), "SCHEDULED"))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("airplane").get("status"), "ACTIVE"));
    }

    private Specification<Flight> getFlightRangeSpec(FlightSearchRequest flightSearchRequest, LocalDate startDate, LocalDate endDate) {
        Specification<Flight> spec = Specification.where(null);
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("departureDateTime"), startDate.atStartOfDay()))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("departureDateTime"), endDate.atTime(23, 59, 59)))
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

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
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
