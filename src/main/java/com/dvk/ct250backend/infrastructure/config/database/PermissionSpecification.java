package com.dvk.ct250backend.infrastructure.config.database;

import com.dvk.ct250backend.app.dto.request.SearchCriteria;
import com.dvk.ct250backend.domain.auth.entity.Permission;
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
public class PermissionSpecification implements Specification<Permission> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<Permission> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        if(criteria.getOperation().equalsIgnoreCase("=")) {
            return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
        return null;
    }
}
