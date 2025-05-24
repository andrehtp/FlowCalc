package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_vazao_diaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VazaoDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_vazao_diaria")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_resumo_mensal", referencedColumnName = "co_seq_resumo_mensal", nullable = false)
    private ResumoMensal resumoMensal;

    @Column(name = "vazao")
    private Float vazao;

    @Column(name = "vazao_status")
    private Integer vazaoStatus;

    @Column(name = "data_vazao")
    private LocalDate dataVazao;
}