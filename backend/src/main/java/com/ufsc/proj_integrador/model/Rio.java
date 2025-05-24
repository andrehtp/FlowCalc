package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_rio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_rio")
    private Long id;

    @Column(name = "nome", nullable = false, length = 144)
    private String nome;

    @Column(name = "descricao", length = 88)
    private String descricao;
}