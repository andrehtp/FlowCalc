package com.ufsc.proj_integrador.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_seq_estado")
    private Integer id;

    @Column(name = "nome", length = 44, nullable = false)
    private String nome;
}