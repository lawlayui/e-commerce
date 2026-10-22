package com.lawlayui.e_commerce.catalog.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lawlayui.e_commerce.catalog.application.exception.CatalogProductNotFoundException;
import com.lawlayui.e_commerce.catalog.domain.exception.RatingInvalidException;
import com.lawlayui.e_commerce.catalog.domain.exception.MoneyInvalidException;

@RestControllerAdvice(basePackages = "com.lawlayui.e_commerce.catalog") 
public class CatalogProductGlobalExceptionHandler {
    @ExceptionHandler(CatalogProductNotFoundException.class)
    public ResponseEntity<String> handleCatalogProductNotFoundException(CatalogProductNotFoundException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(RatingInvalidException.class)
    public ResponseEntity<String> handleRatingInvalidException(RatingInvalidException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(MoneyInvalidException.class)
    public ResponseEntity<String> handleMoneyInvalidException(MoneyInvalidException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

}
