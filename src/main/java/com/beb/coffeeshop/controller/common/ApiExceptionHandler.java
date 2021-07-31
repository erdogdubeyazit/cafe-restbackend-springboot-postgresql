package com.beb.coffeeshop.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

import com.beb.coffeeshop.presentation.result.common.Result;

/**
 * Last chance to handle exceptions occured in Controllers. Encapsulates raw
 * error info into an error ApiResult
 * 
 * @author Beyazit
 * @category Controller
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * 
     * @param ex : unhandled runtime exception
     * @return ResponseEntity
     */
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<?> handle(RuntimeException ex) {
        if (ex instanceof AccessDeniedException) {
            return Result.forbidden();
        } else {
            String errorReferenceCode = UUID.randomUUID().toString();
            log.error("Unhandled exception error [code=" + errorReferenceCode + "]", ex);
            return Result.serverError("Sorry, there is an error on the server side.", errorReferenceCode);
        }
    }
}
