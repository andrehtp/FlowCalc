package com.ufsc.proj_integrador.repository.query;

import com.ufsc.proj_integrador.dto.CabecalhoEstacaoDto;
import com.ufsc.proj_integrador.dto.DadosEstacaoDto;
import com.ufsc.proj_integrador.dto.ResumoMensalDto;
import com.ufsc.proj_integrador.dto.ResumoMensalResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ResumoMensalQueryRepository {
    List<DadosEstacaoDto> getResumoMensalCompleto(
            Long codigoEstacao,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    CabecalhoEstacaoDto getCabecalhoEstacaoByCodigoEstacao(Long codigoEstacao);

    List<ResumoMensalDto> getResumoMensalByCodigoEstacao(
            Long codigoEstacao,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}
