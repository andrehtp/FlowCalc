package com.ufsc.proj_integrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumoMensalResponseDto {
    private CabecalhoEstacaoDto cabecalho;
    private List<ResumoMensalDto> resumosMensais;
}
