package com.ufsc.proj_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosEstacaoDto {

    private Long codigoEstacao;
    private String nomeEstacao;
    private String latitudeEstacao;
    private String longitudeEstacao;
    private String altitudeEstacao;
    private String nomeRio;
    private String nomeCidade;
    private String nomeEstado;

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

    private List<VazaoDiariaDto> vazoesDiarias;
}
