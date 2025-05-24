package com.ufsc.proj_integrador.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EstacaoExceptionHandler {
    @ExceptionHandler(EstacaoNotFoundException.class)
    public ResponseEntity<String> handleEstacaoNotFound(EstacaoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NaoExistemDadosNoPeriodoException.class)
    public ResponseEntity<String> handleSemDados(NaoExistemDadosNoPeriodoException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ex.getMessage());
    }
}
