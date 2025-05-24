package com.ufsc.proj_integrador.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstacaoRepositoryCustom {
    Page<Long> getCodigosEstacoesPaginado(Pageable pageable);
}
