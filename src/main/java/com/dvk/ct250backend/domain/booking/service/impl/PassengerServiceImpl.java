package com.dvk.ct250backend.domain.booking.service.impl;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.app.dto.response.Meta;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.PassengerDTO;
import com.dvk.ct250backend.domain.booking.entity.BookingPassenger;
import com.dvk.ct250backend.domain.booking.entity.Passenger;
import com.dvk.ct250backend.domain.booking.enums.BookingStatusEnum;
import com.dvk.ct250backend.domain.booking.mapper.PassengerMapper;
import com.dvk.ct250backend.domain.booking.repository.PassengerRepository;
import com.dvk.ct250backend.domain.booking.service.PassengerService;
import com.dvk.ct250backend.infrastructure.utils.DateUtils;
import com.dvk.ct250backend.infrastructure.utils.RequestParamUtils;
import com.dvk.ct250backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PassengerServiceImpl implements PassengerService {

    PassengerRepository passengerRepository;
    PassengerMapper passengerMapper;
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    DateUtils dateUtils;

    @Override
    @Transactional
    public Passenger handlePassenger(BookingPassenger bookingPassenger, Map<String, Passenger> passengerMap) {
        Passenger passenger = bookingPassenger.getPassenger();
        String uniqueIdentifier = getUniqueIdentifier(passenger, bookingPassenger.getPassengerGroup());

        Passenger existingPassenger = passengerMap.get(uniqueIdentifier);
       if (existingPassenger == null) {
            existingPassenger = passengerRepository.findByEmailAndIsPrimaryContactAndBookingStatusNot(passenger.getEmail(), BookingStatusEnum.INIT)
                    .orElseGet(() -> passengerRepository.save(passenger));
            passengerMap.put(uniqueIdentifier, existingPassenger);
        }
        return existingPassenger;
    }

    private String getUniqueIdentifier(Passenger passenger, String passengerGroup) {
        if (passenger.getEmail() != null && !passenger.getEmail().isEmpty()) {
            return passenger.getEmail();
        }
        return passenger.getPassengerType().toString() + "-" +passenger.getFirstName() + "_" + passenger.getLastName() + "_" + passenger.getDateOfBirth() + passengerGroup;
    }

    @Override
    public Page<PassengerDTO> getPassengers(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Passenger> spec = getPassengerSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Passenger.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Passenger> passengerPage = passengerRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(passengerPage.getTotalPages())
                .total(passengerPage.getTotalElements())
                .build();
        return Page.<PassengerDTO>builder()
                .meta(meta)
                .content(passengerPage.getContent().stream()
                        .map(
                            passenger -> {PassengerDTO passengerDTO = passengerMapper.toPassengerDTO(passenger);
                            passengerDTO.setPassengerGroup(passenger.getBookingPassengers().getFirst().getPassengerGroup());
                            return passengerDTO;
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private Specification<Passenger> getPassengerSpec(Map<String, String> params) {
        Specification<Passenger> spec = Specification.where(null);

        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("phoneNumber"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("email"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("bookingPassengers").get("passengerGroup"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                criteriaBuilder.concat(root.get("lastName"), " "),
                                                root.get("firstName")
                                        )
                                )),
                                likePattern
                        )
                );
            });
        }

        if (params.containsKey("gender")) {
            List<SearchCriteria> genderCriteria = requestParamUtils.getSearchCriteria(params, "gender");
            if (!genderCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = genderCriteria.stream()
                            .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                            .toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
        }

        if (params.containsKey("passengerType")) {
            List<SearchCriteria> passengerTypeCriteria = requestParamUtils.getSearchCriteria(params, "passengerType");
            if (!passengerTypeCriteria.isEmpty()) {
                spec = spec.and((root, query, cb) -> {
                    List<Predicate> predicates = passengerTypeCriteria.stream()
                            .map(criteria -> cb.equal(root.get(criteria.getKey()), criteria.getValue()))
                            .toList();
                    return cb.or(predicates.toArray(new Predicate[0]));
                });
            }
        }

        if (params.containsKey("startDate") && params.containsKey("type")) {
            String startDateStr = params.get("startDate");
            String type = params.get("type");
            LocalDate startDate = dateUtils.parseDate(startDateStr, type);
            spec = spec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = startDate.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = startDate.withDayOfYear(startDate.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atStartOfDay());
            });
        }

        if(params.containsKey("startDate") && params.containsKey("endDate")) {
            String startDateStr = params.get("startDate");
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            String endDateStr = params.get("endDate");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date").plusDays(1);
            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.atStartOfDay()));
        }
        return spec;
    }

    @Override
    public void deletePassenger(Integer id) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found for this id: " + id));
        passengerRepository.delete(passenger);
    }

    @Override
    public PassengerDTO updatePassenger(Integer id, PassengerDTO passengerDTO) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found for this id: " + id));
        passengerMapper.updatePassengerFromDTO(passenger, passengerDTO);
        return passengerMapper.toPassengerDTO(passengerRepository.save(passenger));
    }

}

