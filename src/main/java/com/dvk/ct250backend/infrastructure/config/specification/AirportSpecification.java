package com.dvk.ct250backend.infrastructure.config.specification;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.domain.flight.entity.Airport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
public class AirportSpecification implements Specification<Airport> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<Airport> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        if(criteria.getKey().equalsIgnoreCase("query")){
            return criteriaBuilder.or(
                    criteriaBuilder.like(root.get("airportName"), "%" + criteria.getValue() + "%"),
                    criteriaBuilder.like(root.get("airportCode"), "%" + criteria.getValue().toString().toUpperCase() + "%"),
                    criteriaBuilder.like(root.get("cityName"), "%" + criteria.getValue() + "%"),
                    criteriaBuilder.like(root.get("cityCode"), "%" + criteria.getValue().toString().toUpperCase() + "%")
            );
        }
        return null;
    }
}
