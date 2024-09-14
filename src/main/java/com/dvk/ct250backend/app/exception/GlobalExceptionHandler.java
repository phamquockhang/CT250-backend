package com.dvk.ct250backend.app.exception;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInValidException.class,
    })
    public ResponseEntity<ApiResponse<Object>> handleIdException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }


    @ExceptionHandler(value = {
            PermissionException.class,
    })
    public ResponseEntity<ApiResponse<Object>> handlePermissionException(Exception ex) {
        ApiResponse<Object> res = new ApiResponse<Object>();
        res.setStatus(HttpStatus.FORBIDDEN.value());
        res.setMessage("Forbidden");
        res.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }


//    @ExceptionHandler(value = {
//            NoResourceFoundException.class,
//    })
//    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(Exception ex) {
//        ApiResponse<Object> res = new ApiResponse<Object>();
//        res.setStatus(HttpStatus.NOT_FOUND.value());
//        res.setError(ex.getMessage());
//        res.setMessage("404 Not Found. URL may not exist...");
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
//    }
}