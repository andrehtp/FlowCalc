package com.ufsc.proj_integrador.repository.query;

import java.util.List;

import com.ufsc.proj_integrador.dto.VazaoDiariaDto;

public interface VazaoDiariaQueryRepository {
	public List<VazaoDiariaDto> getVazoesDiarias(Long codigoEstacao);
}
