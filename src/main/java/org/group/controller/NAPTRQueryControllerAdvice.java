package org.group.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group.exception.ResErrorCodeEnum;
import org.group.dto.ResErrorQuery;
import org.group.exception.NAPTRQueryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class NAPTRQueryControllerAdvice {
    private static final Logger LOGGER = LogManager.getLogger(NAPTRQueryControllerAdvice.class);

    @ExceptionHandler(NAPTRQueryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNAPTRQueryException(NAPTRQueryException naptrQueryException) {
        LOGGER.info("Error when sending enum query : [{}]", naptrQueryException.getResErrorQuery());
        return new ResponseEntity<>(naptrQueryException.getResErrorQuery(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ResErrorQuery resErrorQuery = new ResErrorQuery(ResErrorCodeEnum.INTERNAL_ERROR_CODE.getCode(), ex.getMessage());
        LOGGER.info("Error when sending enum query : [{}]", resErrorQuery);
        return new ResponseEntity<>(resErrorQuery, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

