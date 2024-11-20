package com.dvk.ct250backend.domain.flight.service.impl;

import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.entity.Coupon;
import com.dvk.ct250backend.domain.booking.enums.PassengerTypeEnum;
import com.dvk.ct250backend.domain.booking.repository.CouponRepository;
import com.dvk.ct250backend.domain.booking.service.CouponService;
import com.dvk.ct250backend.domain.flight.config.FlightUploadJobListener;
import com.dvk.ct250backend.domain.flight.constants.FeeConstants;
import com.dvk.ct250backend.domain.flight.dto.FlightDTO;
import com.dvk.ct250backend.domain.flight.dto.FlightOverview;
import com.dvk.ct250backend.domain.flight.dto.request.FlightSearchRequest;
import com.dvk.ct250backend.domain.flight.dto.request.PassengerTypeQuantityRequest;
import com.dvk.ct250backend.domain.flight.entity.*;
import com.dvk.ct250backend.domain.flight.enums.RouteTypeEnum;
import com.dvk.ct250backend.domain.flight.enums.SeatAvailabilityStatus;
import com.dvk.ct250backend.domain.flight.enums.TicketClassEnum;
import com.dvk.ct250backend.domain.flight.mapper.FlightMapper;
import com.dvk.ct250backend.domain.flight.repository.FeeRepository;
import com.dvk.ct250backend.domain.flight.repository.FlightRepository;
import com.dvk.ct250backend.domain.flight.service.FlightService;
import com.dvk.ct250backend.infrastructure.utils.DateUtils;
import com.dvk.ct250backend.infrastructure.utils.FileUtils;
import com.dvk.ct250backend.infrastructure.utils.NumberUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    FeeRepository feeRepository;
    FlightMapper flightMapper;
    JobLauncher jobLauncher;
    Job flightUploadJob;
    FlightUploadJobListener flightUploadJobListener;
    FileUtils fileUtils;
    RequestParamUtils requestParamUtils;
    NumberUtils numberUtils;
    DateUtils dateUtils;
    CouponService couponService;
    CouponRepository couponRepository;

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
        List<Flight> filteredFlights = flights.stream()
                .filter(flight -> {
                    int requiredSeats = Optional.ofNullable(flightSearchRequest.getPassengerTypeQuantityRequests())
                            .orElse(Collections.emptyList())
                            .stream()
                            .mapToInt(PassengerTypeQuantityRequest::getQuantity)
                            .sum();
                    int availableSeats = flight.getSeatAvailability().stream()
                            .filter(seatAvailability -> seatAvailability.getStatus() == SeatAvailabilityStatus.AVAILABLE)
                            .mapToInt(SeatAvailability::getAvailableSeats)
                            .sum();
                    return availableSeats >= requiredSeats;
                })
                .collect(Collectors.toList());

        return filteredFlights.stream()
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
        int totalPassenger = flightSearchRequest.getPassengerTypeQuantityRequests().stream()
                .mapToInt(PassengerTypeQuantityRequest::getQuantity)
                .sum();
        Coupon coupon = couponRepository.findByCouponCode(flightSearchRequest.getCouponCode()).orElse(null);
        if (coupon != null && coupon.getMaxUsage() <= 0) {
            coupon = null;
        }
        flights.forEach(flight -> {
            String date = formatDate(flight.getDepartureDateTime().toLocalDate());
            int availableBusinessSeats = flight.getSeatAvailability().stream()
                    .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(TicketClassEnum.BUSINESS)
                            && seatAvailability.getStatus().equals(SeatAvailabilityStatus.AVAILABLE))
                    .toList().size();
            int availableEconomySeats = flight.getSeatAvailability().stream()
                    .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(TicketClassEnum.ECONOMY)
                            && seatAvailability.getStatus().equals(SeatAvailabilityStatus.AVAILABLE))
                    .toList().size();
            //Cả 2 hạng vé đều hết chỗ => không thêm vào flightMap tính toán overview
            if (!(availableBusinessSeats == 0 && availableEconomySeats == 0)) {
                if(flightMap.containsKey(date)){
                    flightMap.get(date).add(flight);
                } else {
                    flightMap.put(date, new ArrayList<>());
                    flightMap.get(date).add(flight);
                }
            } else {
                flightMap.put(date, new ArrayList<>());
            }
        });
        LocalDate currentDate = start;
        //Thêm các ngày không có chuyến bay vào flightMap để tính toán overview
        while (currentDate.isBefore(end) || currentDate.isEqual(end)) {
            String date = formatDate(currentDate);
            flightMap.putIfAbsent(date, new ArrayList<>());
            currentDate = currentDate.plusDays(1);
        }
        Coupon finalCoupon = coupon;
        return flightMap.entrySet().stream()
                .map(entry -> {
                    FlightOverview flightOverview = new FlightOverview();
                    flightOverview.setDate(entry.getKey());
                    if (entry.getValue().isEmpty()) {
                        flightOverview.setMinPriceOfDay(BigDecimal.valueOf(0.0));
                        flightOverview.setHasFlight(false);
                    } else {
                        flightOverview.setHasFlight(true);
                        BigDecimal minPrice = entry.getValue().stream()
                                .map(flight -> flight.getFlightPricing().stream()
                                        //Lọc ra các hạng vé còn chỗ trống đủ cho tổng số hành khách
                                        .filter(flightPricing -> {
                                            int availableSeats = flight.getSeatAvailability().stream()
                                                    .filter(seatAvailability -> seatAvailability.getSeat().getTicketClass().equals(flightPricing.getTicketClass().getTicketClassName())
                                                            && seatAvailability.getStatus().equals(SeatAvailabilityStatus.AVAILABLE))
                                                    .toList().size();
                                            return availableSeats > 0 && availableSeats >= totalPassenger;
                                        })
                                        //Tính giá vé thấp nhất của các hạng vé còn chỗ trống của chuyến bay
                                        .map(flightPricing -> getTotalTicketPrice(flight,
                                                flightSearchRequest.getPassengerTypeQuantityRequests(),
                                                flightPricing.getTicketClass(),
                                                finalCoupon))
                                        .min((a, b) -> {
                                            if (a == null) {
                                                return 1;
                                            } else if (b == null) {
                                                return -1;
                                            } else {
                                                return a.compareTo(b);
                                            }
                                        })
                                        .orElse(null))
                                //Tìm giá vé thấp nhất của tất cả cả chuyến bay trong ngày
                                .min((a, b) -> {
                                    if (a == null) {
                                        return 1;
                                    } else if (b == null) {
                                        return -1;
                                    } else {
                                        return a.compareTo(b);
                                    }
                                })
                                .orElse(BigDecimal.valueOf(0.0));
                        flightOverview.setMinPriceOfDay(minPrice);
                    }
                    return flightOverview;
                }).toList();

    }

    private BigDecimal getTotalTicketPrice(Flight flight,
                                           List<PassengerTypeQuantityRequest> passengerTypeQuantityRequests,
                                           TicketClass ticketClass,
                                           Coupon coupon) {
        //Tính tổng giá vé cho mỗi loại hành khách
        return passengerTypeQuantityRequests.stream()
                .map(passengerTypeQuantityRequest ->
                        getPassengerTotalFee(flight,
                                PassengerTypeEnum.valueOf(passengerTypeQuantityRequest.getPassengerType()),
                                ticketClass,
                                coupon)
                                .multiply(BigDecimal.valueOf(passengerTypeQuantityRequest.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getPassengerTotalFee(Flight flight,
                                            PassengerTypeEnum passengerType,
                                            TicketClass ticketClass,
                                            Coupon coupon) {
        BigDecimal basePrice = flight.getFlightPricing().stream()
                .filter(flightPricing -> Objects.equals(flightPricing.getTicketClass().getTicketClassId(), ticketClass.getTicketClassId())
                        && dateUtils.isInDateRange(LocalDate.now(), flightPricing.getValidFrom(), flightPricing.getValidTo()))
                .map(FlightPricing::getTicketPrice)
                .findFirst()
                .orElse(BigDecimal.valueOf(0.0));

        List<Fee> flightFees = feeRepository.findAll();
        //Tính tổng phí cho mỗi hành khách (vé + phí)
        return flightFees.stream()
                .map(fee -> {

                            //VAT
                            if (fee.getFeeId() == FeeConstants.VAT_ID) {
                                Fee ticketPriceFee = feeRepository.findById(1).orElse(null);
                                assert ticketPriceFee != null;
                                FeePricing ticketFeePricing = ticketPriceFee.getFeePricing().stream()
                                        .filter(feePricing -> feePricing.getPassengerType().equals(passengerType)
                                                && feePricing.getRouteType().equals(flight.getRoute().getRouteType())
                                                && dateUtils.isInDateRange(LocalDate.now(), feePricing.getValidFrom(), feePricing.getValidTo()))
                                        .findFirst()
                                        .orElse(null);
                                assert ticketFeePricing != null;
                                if (ticketFeePricing.getIsPercentage().equals(Boolean.TRUE)) {
                                    return getFee(fee, passengerType, flight.getRoute().getRouteType(),
                                            numberUtils.roundToThousand(basePrice.multiply(ticketFeePricing.getFeeAmount())
                                                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)), null);
                                } else {
                                    return getFee(fee, passengerType, flight.getRoute().getRouteType(), ticketFeePricing.getFeeAmount(), null);
                                }
                            }
                            return getFee(fee, passengerType, flight.getRoute().getRouteType(), basePrice, coupon);
                        }
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getFee(Fee fee,
                              PassengerTypeEnum passengerType,
                              RouteTypeEnum routeType,
                              BigDecimal basePrice,
                              Coupon coupon) {

        return fee.getFeePricing().stream()
                .filter(feePricing -> feePricing.getPassengerType().equals(passengerType)
                        && feePricing.getRouteType().equals(routeType)
                        && dateUtils.isInDateRange(LocalDate.now(), feePricing.getValidFrom(), feePricing.getValidTo()))
                .map(feePricing -> {
                    if (feePricing.getIsPercentage().equals(Boolean.TRUE)) {
                        //Gia ve co ban
                        if (fee.getFeeId() == FeeConstants.BASE_PRICE_ID) {
                            return numberUtils.roundToThousand(
                                    couponService.getActualPrice(basePrice.multiply(feePricing.getFeeAmount())
                                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP), coupon)
                            );
                        }

                        return numberUtils.roundToThousand(basePrice.multiply(feePricing.getFeeAmount())
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
                    }
                    if(fee.getFeeId() == 1){
                        return numberUtils.roundToThousand(
                                couponService.getActualPrice(feePricing.getFeeAmount(), coupon)
                        );
                    }
                    return numberUtils.roundToThousand(feePricing.getFeeAmount());

                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public Page<FlightDTO> getFlights(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Flight.class);
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
