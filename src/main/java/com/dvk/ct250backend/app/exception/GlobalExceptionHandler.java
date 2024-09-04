package com.dvk.ct250backend.app.exception;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse<Void> handleGenericException(HttpServletRequest request, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<Void> handleBadRequestException(HttpServletRequest request, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .message(ex.getMessage())
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse<Void> handleConstraintViolationException(HttpServletRequest request, Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        ConstraintViolationException violationException = (ConstraintViolationException) ex;
        List<String> errors = violationException.getConstraintViolations().stream()
                .map(constraint -> constraint.getPropertyPath() + ": " + constraint.getMessage())
                .collect(Collectors.toList());
        return ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(String.join(", ", errors))
                .message(ex.getMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(String.join(", ", errors))
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(apiResponse, headers, status);
    }
}