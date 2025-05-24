package com.ufsc.proj_integrador.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.model.QEstacao;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class EstacaoRepositoryImpl implements EstacaoRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<Long> getCodigosEstacoesPaginado(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QEstacao estacao = QEstacao.estacao;

        List<Long> codigos = queryFactory
                .select(estacao.codigoEstacao)
                .from(estacao)
                .orderBy(estacao.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(estacao.count())
                .from(estacao)
                .fetchOne();

        return new PageImpl<>(codigos, pageable, total != null ? total : 0L);
    }
}