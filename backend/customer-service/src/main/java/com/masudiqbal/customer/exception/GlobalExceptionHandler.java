package com.masudiqbal.customer.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateCustomer(
            DuplicateCustomerException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                validationErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains invalid JSON",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "The request conflicts with existing database data",
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        // Log the actual exception in a production application.
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }
}

