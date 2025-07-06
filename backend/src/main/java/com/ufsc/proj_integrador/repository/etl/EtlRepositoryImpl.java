package com.ufsc.proj_integrador.repository.etl;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.model.QEstacao;
import com.ufsc.proj_integrador.model.QResumoMensal;

@RequiredArgsConstructor
@Repository
public class EtlRepositoryImpl implements EtlRepository {

	private final JPAQueryFactory queryFactory;

	public LocalDateTime getUltimaDataInsercaoByCodigoEstacao(Long codigoEstacao) {
		QResumoMensal resumoMensal = QResumoMensal.resumoMensal;
		QEstacao estacao = QEstacao.estacao;

		return queryFactory
				.select(resumoMensal.dataInicial.max())
				.from(resumoMensal)
				.innerJoin(estacao)
				.on(resumoMensal.estacao.id.eq(estacao.id))
				.where(estacao.codigoEstacao.eq(codigoEstacao))
				.fetchFirst();
	}
}
