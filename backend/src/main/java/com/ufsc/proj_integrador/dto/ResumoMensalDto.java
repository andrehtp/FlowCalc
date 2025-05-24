package com.ufsc.proj_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoMensalDto {
    private LocalDateTime dataInicial;
    private Long resumoMensalId;
    private Float vazaoMedia;
    private Float vazaoMaxima;
    private Float vazaoMinima;
    private Float vazaoMediaReal;
    private Float vazaoMaximaReal;
    private Float vazaoMinimaReal;
    private Integer nivelConsistencia;
    private Integer metodoObtencao;
}
