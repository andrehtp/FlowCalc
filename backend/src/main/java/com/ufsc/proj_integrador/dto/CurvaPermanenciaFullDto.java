package com.ufsc.proj_integrador.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurvaPermanenciaFullDto {
	private CurvaPermanenciaDto resumoMensal;
	private CurvaPermanenciaDto vazaoDiaria;
}
