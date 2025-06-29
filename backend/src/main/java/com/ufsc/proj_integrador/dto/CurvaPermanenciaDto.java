package com.ufsc.proj_integrador.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CurvaPermanenciaDto {
	private List<PontoCurvaDto> curva;
	private double q50;
	private double q90;
	private double q95;
	private double q98;
	private Map<Integer, Double> qmap;
	private List<ClasseLogaritmicaDto> classes;
}
