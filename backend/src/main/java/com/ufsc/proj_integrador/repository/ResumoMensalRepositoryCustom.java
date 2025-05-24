package com.ufsc.proj_integrador.repository;

import com.ufsc.proj_integrador.model.ResumoMensal;

import java.time.LocalDateTime;
import java.util.List;

public interface ResumoMensalRepositoryCustom {
    List<ResumoMensal> get();
}