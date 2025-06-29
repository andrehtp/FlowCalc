package com.ufsc.proj_integrador.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.dto.CabecalhoEstacaoDto;
import com.ufsc.proj_integrador.dto.DadosEstacaoDto;
import com.ufsc.proj_integrador.dto.ResumoMensalDto;
import com.ufsc.proj_integrador.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ResumoMensalQueryRepositoryImpl implements ResumoMensalQueryRepository {
    
    private final JPAQueryFactory queryFactory;

    public CabecalhoEstacaoDto getCabecalhoEstacaoByCodigoEstacao(Long codigoEstacao) {
        QEstacao estacao = QEstacao.estacao;
        QRio rio = QRio.rio;
        QCidade cidade = QCidade.cidade;
        QEstado estado = QEstado.estado;

        JPAQuery<CabecalhoEstacaoDto> query = queryFactory
                .select(Projections.bean(CabecalhoEstacaoDto.class,
                        estacao.codigoEstacao.as("codigoEstacao"),
                        estacao.nome.as("nomeEstacao"),
                        estacao.latitude.as("latitudeEstacao"),
                        estacao.longitude.as("longitudeEstacao"),
                        estacao.altitude.as("altitudeEstacao"),
                        estacao.codigoBacia.as("codigoBacia"),
                        estacao.codigoSubBacia.as("codigoSubBacia"),
                        rio.nome.as("nomeRio"),
                        cidade.nome.as("nomeCidade"),
                        estado.nome.as("nomeEstado")
                ))
                .from(estacao)
                .leftJoin(rio).on(estacao.rio.id.eq(rio.id))
                .leftJoin(cidade).on(cidade.id.eq(estacao.cidade.id))
                .leftJoin(estado).on(estado.id.eq(cidade.estado.id))
                .where(estacao.codigoEstacao.eq(codigoEstacao));

        return query.fetchOne();
    }

    public List<ResumoMensalDto> getResumoMensalByCodigoEstacao(
            Long codigoEstacao,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        QResumoMensal resumoMensal = QResumoMensal.resumoMensal;
        QEstacao estacao = QEstacao.estacao;

        JPAQuery<ResumoMensalDto> query = queryFactory
                .select(Projections.bean(ResumoMensalDto.class,
                        resumoMensal.dataInicial.as("dataInicial"),
                        resumoMensal.id.as("resumoMensalId"),
                        resumoMensal.vazaoMedia.as("vazaoMedia"),
                        resumoMensal.vazaoMaxima.as("vazaoMaxima"),
                        resumoMensal.vazaoMinima.as("vazaoMinima"),
                        resumoMensal.vazaoMediaReal.as("vazaoMediaReal"),
                        resumoMensal.vazaoMaximaReal.as("vazaoMaximaReal"),
                        resumoMensal.vazaoMinimaReal.as("vazaoMinimaReal"),
                        resumoMensal.nivelConsistencia.as("nivelConsistencia"),
                        resumoMensal.metodoObtencao.as("metodoObtencao")
                ))
                .from(resumoMensal)
                .join(estacao).on(estacao.id.eq(resumoMensal.estacao.id))
                .where(estacao.codigoEstacao.eq(codigoEstacao));

        if (inicio != null) {
            query.where(resumoMensal.dataInicial.goe(inicio));
        }
        if (fim != null) {
            query.where(resumoMensal.dataInicial.loe(fim));
        }

        return query.fetch();
    }

    public List<Double> getVazoesSomenteValores(Long codigoEstacao, LocalDateTime inicio, LocalDateTime fim) {
        QResumoMensal resumoMensal = QResumoMensal.resumoMensal;
        QEstacao estacao = QEstacao.estacao;

        JPAQuery<Double> query = queryFactory
                .select(resumoMensal.vazaoMedia.castToNum(Double.class))
                .from(resumoMensal)
                .join(estacao).on(estacao.id.eq(resumoMensal.estacao.id))
                .where(estacao.codigoEstacao.eq(codigoEstacao));

        if (inicio != null) {
            query.where(resumoMensal.dataInicial.goe(inicio));
        }
        if (fim != null) {
            query.where(resumoMensal.dataInicial.loe(fim));
        }

        return query.fetch();
    }
}
