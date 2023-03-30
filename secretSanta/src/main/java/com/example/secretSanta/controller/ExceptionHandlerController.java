package com.example.secretSanta.controller;

import com.example.secretSanta.exception.EntityNotExistException;
import com.example.secretSanta.exception.InvalidTossException;
import com.example.secretSanta.model.ExceptionDTO;
import org.hibernate.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({TypeMismatchException.class, BindException.class})
    public ResponseEntity<?> badRequestHandler(Exception e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionDTO(e.getMessage()));
    }

    @ExceptionHandler(EntityNotExistException.class)
    public ResponseEntity<?> notFoundHandler(Exception e) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidTossException.class)
    public ResponseEntity<?> conflictHandler(Exception e) {
        return ResponseEntity.status(CONFLICT).body(new ExceptionDTO(e.getMessage()));
    }
}
