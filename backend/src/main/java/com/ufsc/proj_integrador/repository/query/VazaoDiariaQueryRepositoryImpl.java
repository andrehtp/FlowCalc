package com.ufsc.proj_integrador.repository.query;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.dto.VazaoDiariaDto;
import com.ufsc.proj_integrador.model.QEstacao;
import com.ufsc.proj_integrador.model.QResumoMensal;
import com.ufsc.proj_integrador.model.QVazaoDiaria;

@RequiredArgsConstructor
@Repository
public class VazaoDiariaQueryRepositoryImpl implements VazaoDiariaQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<VazaoDiariaDto> getVazoesDiarias(Long codigoEstacao, LocalDateTime inicio, LocalDateTime fim) {
		QVazaoDiaria vazaoDiaria = QVazaoDiaria.vazaoDiaria;
		QResumoMensal resumoMensal = QResumoMensal.resumoMensal;

		JPAQuery<VazaoDiariaDto> query = queryFactory
				.select(Projections.bean(VazaoDiariaDto.class,
						vazaoDiaria.resumoMensal.id.as("resumoMensalId"),
						vazaoDiaria.vazao.as("vazao"),
						vazaoDiaria.dataVazao.as("dataVazao")
						))
				.from(vazaoDiaria)
				.where(vazaoDiaria.resumoMensal.estacao.codigoEstacao.eq(codigoEstacao));

		if (inicio != null) {
			query.where(resumoMensal.dataInicial.goe(inicio));
		}
		if (fim != null) {
			query.where(resumoMensal.dataInicial.loe(fim));
		}

		return query.fetch();
	}

	public List<Double> getVazoesSomenteValores(Long codigoEstacao, LocalDateTime inicio, LocalDateTime fim) {
		QVazaoDiaria vd = QVazaoDiaria.vazaoDiaria;
		QResumoMensal rm = QResumoMensal.resumoMensal;

		JPAQuery<Double> query = queryFactory
				.select(vd.vazao.castToNum(Double.class))
				.from(vd)
				.join(rm).on(vd.resumoMensal.id.eq(rm.id))
				.where(rm.estacao.codigoEstacao.eq(codigoEstacao));

		if (inicio != null) {
			query.where(rm.dataInicial.goe(inicio));
		}
		if (fim != null) {
			query.where(rm.dataInicial.loe(fim));
		}

		return query.fetch();
	}
}
