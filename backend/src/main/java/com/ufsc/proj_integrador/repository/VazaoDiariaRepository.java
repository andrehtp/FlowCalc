package com.ufsc.proj_integrador.repository;


import com.ufsc.proj_integrador.model.VazaoDiaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VazaoDiariaRepository extends JpaRepository<VazaoDiaria, Long>, VazaoDiariaRepositoryCustom {
}
