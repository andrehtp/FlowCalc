package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "tb_config_etl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigEtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_config_etl")
    private Integer id;

    @Column(name = "st_ativo", nullable = false)
    private Integer ativo;

    @Column(name = "data_atualizacao_inicial")
    private LocalDate dataAtualizacaoInicial;
}