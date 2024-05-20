package com.example.Spring_security_access_configuration.web.handler;

import com.example.Spring_security_access_configuration.exception.AlreadyExistsException;
import com.example.Spring_security_access_configuration.exception.RefreshTokenException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException e, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, e, request);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistsExceptionHandler(AlreadyExistsException e, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e, request);
    }
    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> entityNotFoundExceptionHandler(EntityNotFoundException e, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, e, request);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus forbidden, Exception e, WebRequest request) {
        return ResponseEntity.status(forbidden)
                .body(ErrorResponseBody.builder()
                        .message(e.getMessage())
                        .description(request.getDescription(false))
                        .build());
    }
}
