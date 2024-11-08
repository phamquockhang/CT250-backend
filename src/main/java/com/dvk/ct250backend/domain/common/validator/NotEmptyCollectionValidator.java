package com.dvk.ct250backend.domain.common.validator;

import com.dvk.ct250backend.domain.common.annotation.NotEmptyCollection;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;

public class NotEmptyCollectionValidator implements ConstraintValidator<NotEmptyCollection, Collection<?>> {
    @Override
    public boolean isValid(Collection<?> collection,
                           ConstraintValidatorContext constraintValidatorContext) {
        return collection != null && !collection.isEmpty();
    }
}
