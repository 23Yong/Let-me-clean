package com.letmeclean.global.exception;

import com.letmeclean.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LetMeCleanException.class)
    protected ResponseEntity<?> applicationExceptionHandler(LetMeCleanException ex) {
        log.error("Error occurs = {}", ex.toString());

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(Response.error(ex.getErrorCode().name()));
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> applicationExceptionHandler(RuntimeException ex) {
        log.error("Error occurs = {}", ex.toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorCode.INTERNAL_SERVER_ERROR.name());
    }
}
