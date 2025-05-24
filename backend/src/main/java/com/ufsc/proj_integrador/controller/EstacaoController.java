package com.ufsc.proj_integrador.controller;

import com.ufsc.proj_integrador.dto.DadosEstacaoDto;
import com.ufsc.proj_integrador.dto.ResumoMensalDto;
import com.ufsc.proj_integrador.dto.ResumoMensalResponseDto;
import com.ufsc.proj_integrador.service.ResumoMensalService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estacoes")
@RequiredArgsConstructor
public class EstacaoController {

    private final ResumoMensalService resumoMensalService;

    @GetMapping("/{codigoEstacao}/resumos")
    public ResponseEntity<?> getResumoMensalByEstacao(
            @PathVariable Long codigoEstacao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        List<DadosEstacaoDto> dados = resumoMensalService.getDadosEstacao(codigoEstacao, inicio, fim);

        if (dados.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("Não existem dados para a estação no período específicado.");
        }

        return ResponseEntity.ok(dados);
    }

    @GetMapping("/{codigoEstacao}/resumo")
    public ResponseEntity<?> resumoMensalController(
            @PathVariable Long codigoEstacao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    ) {
        return ResponseEntity.ok(resumoMensalService.buildResumoMensalResponseDto(codigoEstacao, inicio, fim));
    }

    @PostMapping("/consulta-estacao")
    public ResponseEntity<?> resumoMensalController(@RequestBody Map<String, Object> body) {
        Long codEstacao = Long.valueOf(body.get("codEstacao").toString());

        String inicioStr = (String) body.get("dataInicio");
        LocalDateTime inicio = (inicioStr != null && !inicioStr.isBlank())
                ? LocalDate.parse(inicioStr).atStartOfDay()
                : null;

        String fimStr = (String) body.get("dataFim");
        LocalDateTime fim = (fimStr != null && !fimStr.isBlank())
                ? LocalDate.parse(fimStr).atStartOfDay()
                : null;

        return ResponseEntity.ok(
                resumoMensalService.buildResumoMensalResponseDto(codEstacao, inicio, fim)
        );
    }
}
