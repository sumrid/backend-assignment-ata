package com.klinkanha.assignment.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> requestInvalid(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getFieldErrors();
        var message = ex.getMessage();
        if (CollectionUtils.isNotEmpty(errors)) {
            message = "%s: %s".formatted(errors.getFirst().getField(), errors.getFirst().getDefaultMessage());
        }
        log.error(message);
        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> requestInvalid(InvalidInputException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }
}
