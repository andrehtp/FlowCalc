package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_cidade")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_estado", referencedColumnName = "co_seq_estado")
    private Estado estado;

    @Column(name = "nome", length = 44, nullable = false)
    private String nome;
}