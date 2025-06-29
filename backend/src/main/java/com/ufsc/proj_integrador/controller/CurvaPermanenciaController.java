package com.ufsc.proj_integrador.controller;

import com.ufsc.proj_integrador.dto.*;
import com.ufsc.proj_integrador.service.*;
import com.ufsc.proj_integrador.calculations.CurvaPermanenciaCalculator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CurvaPermanenciaController {

	private final ResumoMensalService resumoMensalService;
	private final VazaoDiariaService vazaoDiariaService;

	@PostMapping("/curva-permanencia")
	public ResponseEntity<CurvaPermanenciaFullDto> curva(@RequestBody Map<String, Object> body) {
		Long codigo = Long.valueOf(body.get("codigoEstacao").toString());

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

		List<Double> vazoesMensais = resumoMensalService.getVazoesSomenteValores(codigo, inicio, fim, nivelConsistencia);
		List<Double> vazoesDiarias = vazaoDiariaService.getVazoesSomenteValores(codigo, inicio, fim, nivelConsistencia);

		CurvaPermanenciaDto resultadoMensal = CurvaPermanenciaCalculator.calcular(vazoesMensais);
		CurvaPermanenciaDto resultadoDiario = CurvaPermanenciaCalculator.calcular(vazoesDiarias);

		CurvaPermanenciaFullDto resposta = new CurvaPermanenciaFullDto();
		resposta.setResumoMensal(resultadoMensal);
		resposta.setVazaoDiaria(resultadoDiario);

		return ResponseEntity.ok(resposta);
	}
}
