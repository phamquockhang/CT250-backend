package com.dvk.ct250backend.domain.common.annotation;

import com.dvk.ct250backend.domain.common.validator.NotEmptyCollectionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotEmptyCollectionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyCollection {
    String message() default "The collection must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
