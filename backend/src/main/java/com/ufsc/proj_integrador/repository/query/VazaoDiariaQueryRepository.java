package com.ufsc.proj_integrador.repository.query;

import java.time.LocalDateTime;
import java.util.List;

import com.ufsc.proj_integrador.dto.VazaoDiariaDto;

public interface VazaoDiariaQueryRepository {
	List<VazaoDiariaDto> getVazoesDiarias(Long codigoEstacao, LocalDateTime inicio, LocalDateTime fim);

	List<Double> getVazoesSomenteValores(Long codigoEstacao, LocalDateTime inicio, LocalDateTime fim, Integer nivelConsistencia);
}
