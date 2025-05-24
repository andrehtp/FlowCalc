package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_resumo_mensal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResumoMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_resumo_mensal")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_estacao", referencedColumnName = "co_seq_estacao", nullable = false)
    private Estacao estacao;

    @Column(name = "data_inicial")
    private LocalDateTime dataInicial;

    @Column(name = "data_insercao_ana")
    private LocalDateTime dataInsercaoAna;

    @Column(name = "metodo_obtencao")
    private Integer metodoObtencao;

    @Column(name = "nivel_consistencia")
    private Integer nivelConsistencia;

    @Column(name = "vazao_media")
    private Float vazaoMedia;

    @Column(name = "vazao_maxima")
    private Float vazaoMaxima;

    @Column(name = "vazao_minima")
    private Float vazaoMinima;

    @Column(name = "vazao_media_real")
    private Float vazaoMediaReal;

    @Column(name = "vazao_maxima_real")
    private Float vazaoMaximaReal;

    @Column(name = "vazao_minima_real")
    private Float vazaoMinimaReal;
}
