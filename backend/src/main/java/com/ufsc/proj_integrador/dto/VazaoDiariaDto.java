package com.ufsc.proj_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VazaoDiariaDto {
    private Long resumoMensalId;
    private LocalDate dataVazao;
    private Float vazao;
    private Integer status;
}