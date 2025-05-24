package com.ufsc.proj_integrador.controller.exception;

public class EstacaoNotFoundException extends RuntimeException {
    public EstacaoNotFoundException(String message) {
        super(message);
    }
}