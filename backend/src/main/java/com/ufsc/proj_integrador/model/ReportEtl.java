package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_report_etl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportEtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_report_etl")
    private Long id;

    @Column(name = "data_inicio_etl", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim_etl", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "ds_erro", length = 128)
    private String erro;
}
