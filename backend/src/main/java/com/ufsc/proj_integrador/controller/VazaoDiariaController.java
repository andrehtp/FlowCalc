package com.ufsc.proj_integrador.controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufsc.proj_integrador.service.VazaoDiariaService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VazaoDiariaController {

	private final VazaoDiariaService vazaoDiariaService;

	@PostMapping("/vazoesDiarias")
	public ResponseEntity<?> vazoesDiarias(@RequestBody Map<String, Object> body) {
		Long codigoEstacao = Long.valueOf(body.get("codigoEstacao").toString());

		if(codigoEstacao != null) {
			return ResponseEntity.ok(vazaoDiariaService.getVazoesDiarias(codigoEstacao));
		}

		return ResponseEntity.status(404).body("error");
	}
}
