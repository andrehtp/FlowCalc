package com.ufsc.proj_integrador.service;

import com.ufsc.proj_integrador.controller.exception.EstacaoNotFoundException;
import com.ufsc.proj_integrador.controller.exception.NaoExistemDadosNoPeriodoException;
import com.ufsc.proj_integrador.dto.*;

import com.ufsc.proj_integrador.repository.query.ResumoMensalQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumoMensalService {

    private final ResumoMensalQueryRepository resumoMensalQueryRepository;

    public ResumoMensalResponseDto buildResumoMensalResponseDto(
            Long codigoEstacao,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        CabecalhoEstacaoDto cabecalho = resumoMensalQueryRepository.getCabecalhoEstacaoByCodigoEstacao(codigoEstacao);
        if (cabecalho == null) {
            throw new EstacaoNotFoundException("Estação com código " + codigoEstacao + " não encontrada.");
        }

        List<ResumoMensalDto> resumoMensal = resumoMensalQueryRepository.getResumoMensalByCodigoEstacao(codigoEstacao, inicio, fim);
        if (resumoMensal.isEmpty()) {
            throw new NaoExistemDadosNoPeriodoException("Não existem dados para o período especificado.");
        }

        return ResumoMensalResponseDto
                .builder()
                .cabecalho(cabecalho)
                .resumosMensais(resumoMensal)
                .build();
    }
}
