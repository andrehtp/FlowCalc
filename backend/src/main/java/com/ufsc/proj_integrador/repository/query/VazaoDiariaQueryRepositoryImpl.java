package com.ufsc.proj_integrador.repository.query;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.dto.VazaoDiariaDto;
import com.ufsc.proj_integrador.model.QVazaoDiaria;

@RequiredArgsConstructor
@Repository
public class VazaoDiariaQueryRepositoryImpl implements VazaoDiariaQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<VazaoDiariaDto> getVazoesDiarias(Long resumoMensalId) {
		QVazaoDiaria vazaoDiaria = QVazaoDiaria.vazaoDiaria;

		JPAQuery<VazaoDiariaDto> query = queryFactory
				.select(Projections.bean(VazaoDiariaDto.class,
						vazaoDiaria.resumoMensal.id.as("resumoMensalId"),
						vazaoDiaria.vazao.as("vazao"),
						vazaoDiaria.dataVazao.as("dataVazao")
						))
				.from(vazaoDiaria)
				.where(vazaoDiaria.resumoMensal.id.eq(resumoMensalId));

		return query.fetch();
	}
}
