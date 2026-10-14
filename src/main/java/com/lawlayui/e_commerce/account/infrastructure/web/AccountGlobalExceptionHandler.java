package com.lawlayui.e_commerce.account.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lawlayui.e_commerce.account.domain.exception.*;
import com.lawlayui.e_commerce.account.application.exception.*;

@RestControllerAdvice(basePackages = "com.lawlayui.e_commerce.account")
public class AccountGlobalExceptionHandler {
    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<String> handleInvalidAddressException(InvalidAddressException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmailException(InvalidEmailException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(InvalidHashedPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidHashedPasswordException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<String> handleInvalidNameException(InvalidPhoneNumberException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(UserAlredyRegistedException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlredyRegistedException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class) 
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<String> handleUserProfileNotFoundException(UserProfileNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(500).body("internal server error");
    }
}
