package com.ufsc.proj_integrador.service;

import com.ufsc.proj_integrador.controller.exception.EstacaoNotFoundException;
import com.ufsc.proj_integrador.controller.exception.NaoExistemDadosNoPeriodoException;
import com.ufsc.proj_integrador.dto.*;
import com.ufsc.proj_integrador.model.ResumoMensal;
import com.ufsc.proj_integrador.repository.VazaoDiariaRepository;
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
    private final VazaoDiariaRepository vazaoDiariaRepository;

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

    public List<DadosEstacaoDto> getDadosEstacao(
            Long codigoEstacao,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        List<DadosEstacaoDto> resultados = resumoMensalQueryRepository.getResumoMensalCompleto(codigoEstacao, inicio, fim);

        List<Long> resumoMensaisIds = resultados.stream()
                .map(DadosEstacaoDto::getResumoMensalId)
                .toList();

        Map<Long, List<VazaoDiariaDto>> vazoesMap = vazaoDiariaRepository.getVazoesByResumoMensalIds(resumoMensaisIds);

        for (DadosEstacaoDto dto : resultados) {
            dto.setVazoesDiarias(vazoesMap.getOrDefault(dto.getResumoMensalId(), List.of()));
        }

        return resultados;
    }
}
