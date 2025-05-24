package com.ufsc.proj_integrador.repository;

import com.ufsc.proj_integrador.model.ResumoMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ResumoMensalRepository extends JpaRepository<ResumoMensal, Long>,
        QuerydslPredicateExecutor<ResumoMensal>,
        ResumoMensalRepositoryCustom {
}