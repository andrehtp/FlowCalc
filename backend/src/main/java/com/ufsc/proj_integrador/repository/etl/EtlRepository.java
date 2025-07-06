package com.ufsc.proj_integrador.repository.etl;

import java.time.LocalDateTime;

public interface EtlRepository {

	LocalDateTime getUltimaDataInsercaoByCodigoEstacao(Long codigoEstacao);
}
