package com.ufsc.proj_integrador.model.id;

import java.io.Serializable;
import java.util.Objects;

public class RioCidadeId implements Serializable {

    private Long rio;
    private Long cidade;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RioCidadeId that)) return false;
        return Objects.equals(rio, that.rio) && Objects.equals(cidade, that.cidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rio, cidade);
    }
}