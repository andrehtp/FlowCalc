package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_estacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Estacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_estacao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_rio", referencedColumnName = "co_seq_rio")
    private Rio rio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_cidade", referencedColumnName = "co_seq_cidade")
    private Cidade cidade;

    @Column(name = "codigo_estacao", nullable = false)
    private Long codigoEstacao;

    @Column(name = "codigo_bacia")
    private Long codigoBacia;

    @Column(name = "codigo_sub_bacia")
    private Long codigoSubBacia;

    @Column(name = "nome", length = 144)
    private String nome;

    @Column(name = "latitude", length = 44)
    private String latitude;

    @Column(name = "longitude", length = 44)
    private String longitude;

    @Column(name = "altitude", length = 44)
    private String altitude;

    @Column(name = "operando")
    private Integer operando;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;
}