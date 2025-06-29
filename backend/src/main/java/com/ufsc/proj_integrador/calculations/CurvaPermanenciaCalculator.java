package com.ufsc.proj_integrador.calculations;

import com.ufsc.proj_integrador.dto.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.Collections;

public class CurvaPermanenciaCalculator {

	public static CurvaPermanenciaDto calcular(List<Double> vazoes) {
		CurvaPermanenciaDto dto = new CurvaPermanenciaDto();

		List<Double> ordenadas = vazoes.stream()
				.filter(v -> v != null && !Double.isNaN(v))
				.sorted(Comparator.reverseOrder())
				.toList();

		int N = ordenadas.size();

		// Curva de permanência (Weibull)
		List<PontoCurvaDto> curva = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			double p = ((i + 1.0) / (N + 1)) * 100.0;
			curva.add(new PontoCurvaDto(p, ordenadas.get(i)));
		}
		dto.setCurva(curva);

		// Q-values específicos
		dto.setQ50(buscaQD(curva, 50));
		dto.setQ90(buscaQD(curva, 90));
		dto.setQ95(buscaQD(curva, 95));
		dto.setQ98(buscaQD(curva, 98));

		// Todos os Qd (1–99)
		Map<Integer, Double> qmap = new LinkedHashMap<>();
		for (int d = 1; d <= 99; d++) {
			qmap.put(d, buscaQD(curva, d));
		}
		dto.setQmap(qmap);

		// Classes logarítmicas
		if (N > 0) {
			int K = 30;
			double qMin = Collections.min(ordenadas);
			double qMax = Collections.max(ordenadas);
			double h = (Math.log(qMax) - Math.log(qMin)) / K;

			List<ClasseLogaritmicaDto> classes = new ArrayList<>();

			for (int j = K; j >= 1; j--) {
				double li = Math.exp(Math.log(qMin) + (j - 1) * h);
				double ls = Math.exp(Math.log(qMin) + j * h);

				int fi = (int) ordenadas.stream().filter(v -> v >= li && v < ls).count();
				int fac = (int) ordenadas.stream().filter(v -> v >= li).count();

				ClasseLogaritmicaDto c = new ClasseLogaritmicaDto();
				c.setClasse(K - j + 1);
				c.setLi(li);
				c.setLs(ls);
				c.setFi(fi);
				c.setFac(fac);
				classes.add(c);
			}

			dto.setClasses(classes);
		}

		return dto;
	}

	private static double buscaQD(List<PontoCurvaDto> curva, int d) {
		return curva.stream()
				.min(Comparator.comparing(c -> Math.abs(c.getPermanencia() - d)))
				.map(PontoCurvaDto::getVazao)
				.orElse(0.0);
	}
}
