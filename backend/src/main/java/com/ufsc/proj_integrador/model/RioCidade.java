package com.ufsc.proj_integrador.model;

import com.ufsc.proj_integrador.model.id.RioCidadeId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_rio_cidade")
@IdClass(RioCidadeId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RioCidade {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_rio", referencedColumnName = "co_seq_rio")
    private Rio rio;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_cidade", referencedColumnName = "co_seq_cidade")
    private Cidade cidade;
}