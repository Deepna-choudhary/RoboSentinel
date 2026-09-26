package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    // =========================
    // VALIDATION ERROR
    // =========================

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<String>
    handleValidationException(

            MethodArgumentNotValidException ex) {


        String errorMessage =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();


        return new ResponseEntity<>(

                errorMessage,

                HttpStatus.BAD_REQUEST

        );
    }


    // =========================
    // RESOURCE NOT FOUND
    // =========================

    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<String>
    handleResourceNotFoundException(

            ResourceNotFoundException ex) {


        return new ResponseEntity<>(

                ex.getMessage(),

                HttpStatus.NOT_FOUND

        );
    }


    // =========================
    // DUPLICATE RESOURCE
    // =========================

    @ExceptionHandler(
            DuplicateResourceException.class
    )
    public ResponseEntity<String>
    handleDuplicateResourceException(

            DuplicateResourceException ex) {


        return new ResponseEntity<>(

                ex.getMessage(),

                HttpStatus.CONFLICT

        );
    }


    // =========================
    // GENERAL ERROR
    // =========================

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<String>
    handleGeneralException(

            Exception ex) {


        return new ResponseEntity<>(

                "Something went wrong: "
                        + ex.getMessage(),

                HttpStatus.INTERNAL_SERVER_ERROR

        );
    }

}