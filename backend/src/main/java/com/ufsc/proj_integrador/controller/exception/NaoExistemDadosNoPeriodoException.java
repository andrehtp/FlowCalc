package com.ufsc.proj_integrador.controller.exception;

public class NaoExistemDadosNoPeriodoException extends RuntimeException {
    public NaoExistemDadosNoPeriodoException(String message) {
        super(message);
    }
}
