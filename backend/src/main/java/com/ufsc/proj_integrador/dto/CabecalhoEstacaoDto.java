package com.ufsc.proj_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CabecalhoEstacaoDto {
    private Long codigoEstacao;
    private String nomeEstacao;
    private String latitudeEstacao;
    private String longitudeEstacao;
    private String altitudeEstacao;
    private Long codigoBacia;
    private Long codigoSubBacia;
    private String nomeRio;
    private String nomeCidade;
    private String nomeEstado;
}
