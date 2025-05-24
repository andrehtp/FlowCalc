package com.ufsc.proj_integrador.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.dto.VazaoDiariaDto;
import com.ufsc.proj_integrador.model.QVazaoDiaria;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VazaoDiariaRepositoryImpl implements VazaoDiariaRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<VazaoDiariaDto>> getVazoesByResumoMensalIds(List<Long> resumoMensalIds) {
        QVazaoDiaria vazaoDiaria = QVazaoDiaria.vazaoDiaria;

        return queryFactory
                .select(Projections.constructor(VazaoDiariaDto.class,
                        vazaoDiaria.resumoMensal.id,
                        vazaoDiaria.dataVazao,
                        vazaoDiaria.vazao,
                        vazaoDiaria.vazaoStatus
                ))
                .from(vazaoDiaria)
                .where(vazaoDiaria.resumoMensal.id.in(resumoMensalIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(VazaoDiariaDto::getResumoMensalId));
    }
}