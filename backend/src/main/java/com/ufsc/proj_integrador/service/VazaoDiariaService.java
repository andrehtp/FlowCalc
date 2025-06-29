package com.ufsc.proj_integrador.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.ufsc.proj_integrador.dto.VazaoDiariaDto;
import com.ufsc.proj_integrador.repository.query.VazaoDiariaQueryRepository;

@Service
@RequiredArgsConstructor
public class VazaoDiariaService {

	private final VazaoDiariaQueryRepository vazaoDiariaRepository;

	public List<VazaoDiariaDto> getVazoesDiarias(Long resumoMensalId) {
		return vazaoDiariaRepository.getVazoesDiarias(resumoMensalId);
	}
}
