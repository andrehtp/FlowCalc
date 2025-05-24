package com.ufsc.proj_integrador.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.model.QResumoMensal;
import com.ufsc.proj_integrador.model.ResumoMensal;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResumoMensalRepositoryImpl implements ResumoMensalRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public ResumoMensalRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ResumoMensal> get() {
        QResumoMensal resumo = QResumoMensal.resumoMensal;

        return queryFactory
                .selectFrom(resumo)
                .fetch();
    }
}