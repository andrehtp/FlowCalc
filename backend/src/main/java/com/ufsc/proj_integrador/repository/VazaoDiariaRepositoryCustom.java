package com.ufsc.proj_integrador.repository;

import com.ufsc.proj_integrador.dto.VazaoDiariaDto;

import java.util.List;
import java.util.Map;

public interface VazaoDiariaRepositoryCustom {
    public Map<Long, List<VazaoDiariaDto>> getVazoesByResumoMensalIds(List<Long> resumoMensalIds);
}