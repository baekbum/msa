package com.example.user_service.controller.advice;

import com.example.user_service.exception.TeamNotExistException;
import com.example.user_service.exception.UserDuplicateException;
import com.example.user_service.exception.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<String> userDuplicateException(UserDuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<String> UserNotExistException(UserNotExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TeamNotExistException.class)
    public ResponseEntity<String> TeamNotExistException(TeamNotExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
