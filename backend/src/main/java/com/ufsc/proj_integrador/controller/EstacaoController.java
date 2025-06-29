package com.ufsc.proj_integrador.controller;

import com.ufsc.proj_integrador.service.CaptchaService;
import com.ufsc.proj_integrador.service.ResumoMensalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/estacoes")
@RequiredArgsConstructor
public class EstacaoController {

    private final ResumoMensalService resumoMensalService;
    private final CaptchaService captchaService;

    @PostMapping("/consulta-estacao")
    public ResponseEntity<?> resumoMensalController(@RequestBody Map<String, Object> body) {
        String captchaToken = (String) body.get("captchaToken");
        /*if (captchaToken == null || !captchaService.isCaptchaValid(captchaToken)) {
            return ResponseEntity.status(403).body("Verificação de CAPTCHA falhou.");
        }*/

        Long codEstacao = Long.valueOf(body.get("codEstacao").toString());

        String inicioStr = (String) body.get("dataInicio");
        LocalDateTime inicio = (inicioStr != null && !inicioStr.isBlank())
                ? LocalDate.parse(inicioStr).atStartOfDay()
                : null;

        String fimStr = (String) body.get("dataFim");
        LocalDateTime fim = (fimStr != null && !fimStr.isBlank())
                ? LocalDate.parse(fimStr).atStartOfDay()
                : null;

        String consistenciaStr = (String) body.get("nivelConsistencia");
        Integer nivelConsistencia = (consistenciaStr != null && !consistenciaStr.isBlank())
                ? Integer.parseInt(consistenciaStr)
                : null;

        return ResponseEntity.ok(
                resumoMensalService.buildResumoMensalResponseDto(codEstacao, inicio, fim, nivelConsistencia)
        );
    }
}
