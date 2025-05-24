package com.ufsc.proj_integrador.repository;

import com.ufsc.proj_integrador.model.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacaoRepository extends JpaRepository<Estacao, Long>, EstacaoRepositoryCustom {
}
