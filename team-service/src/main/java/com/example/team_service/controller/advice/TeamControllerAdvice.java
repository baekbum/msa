package com.example.team_service.controller.advice;

import com.example.team_service.exception.TeamDuplicateException;
import com.example.team_service.exception.TeamNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TeamControllerAdvice {
    @ExceptionHandler(TeamDuplicateException.class)
    public ResponseEntity<String> TeamDuplicateException(TeamDuplicateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TeamNotExistException.class)
    public ResponseEntity<String> TeamNotExistException(TeamNotExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
