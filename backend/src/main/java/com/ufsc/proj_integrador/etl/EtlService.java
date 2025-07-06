package com.ufsc.proj_integrador.etl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ufsc.proj_integrador.model.QEstacao;
import com.ufsc.proj_integrador.repository.etl.EtlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilderFactory;

@Service
@RequiredArgsConstructor
public class EtlService {

	private final JPAQueryFactory queryFactory;
	private final EntityManager em;
	private final EtlRepository etlRepository;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Transactional
	public void atualizarSeNecessario(Long codigoEstacao) {
		LocalDateTime dataInicial = etlRepository.getUltimaDataInsercaoByCodigoEstacao(codigoEstacao);
		if (dataInicial != null) {
			dataInicial = dataInicial.plusDays(1);
		}

		Document doc = requestHidroSerieHistorica(codigoEstacao, dataInicial);
		if (doc == null) return;

		NodeList series = doc.getElementsByTagName("SerieHistorica");
		if (series.getLength() == 0) {
			System.out.println("Nenhum dado novo para estação " + codigoEstacao);
			return;
		}

		processarDadosXml(doc, codigoEstacao);
	}

	private Document requestHidroSerieHistorica(Long codEstacao, LocalDateTime dataInicio) {
		try {
			String dataInicioStr = (dataInicio != null) ? dataInicio.toLocalDate().toString() : "";

			String xml = """
                <?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                               xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                               xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                  <soap:Body>
                    <HidroSerieHistorica xmlns="http://MRCS/">
                      <codEstacao>%s</codEstacao>
                      <dataInicio>%s</dataInicio>
                      <dataFim></dataFim>
                      <tipoDados>3</tipoDados>
                      <nivelConsistencia></nivelConsistencia>
                    </HidroSerieHistorica>
                  </soap:Body>
                </soap:Envelope>
                """.formatted(codEstacao, dataInicioStr);

			HttpURLConnection conn = (HttpURLConnection) new URL("http://telemetriaws1.ana.gov.br/ServiceANA.asmx").openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "http://MRCS/HidroSerieHistorica");
			conn.setDoOutput(true);
			conn.getOutputStream().write(xml.getBytes("UTF-8"));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			return factory.newDocumentBuilder().parse(conn.getInputStream());
		} catch (Exception e) {
			System.err.println("Erro ao consultar estação " + codEstacao + ": " + e.getMessage());
			return null;
		}
	}

	private void processarDadosXml(Document doc, Long codigoEstacao) {
		NodeList series = doc.getElementsByTagName("SerieHistorica");

		Long idEstacao = queryFactory.select(QEstacao.estacao.id)
				.from(QEstacao.estacao)
				.where(QEstacao.estacao.codigoEstacao.eq(codigoEstacao))
				.fetchFirst();

		if (idEstacao == null) throw new RuntimeException("Estação não encontrada: " + codigoEstacao);

		for (int i = 0; i < series.getLength(); i++) {
			Element el = (Element) series.item(i);

			String dataHora = get(el, "DataHora");
			if (dataHora == null || dataHora.isBlank()) continue;

			LocalDateTime dataInicial = parseDateTime(dataHora);
			LocalDateTime dataInsercao = parseDateTime(get(el, "DataIns"));

			Integer metodo = parseNullableInt(get(el, "MetodoObtencaoVazoes"));
			Integer nivel = parseNullableInt(get(el, "NivelConsistencia"));
			Float media = parseNullableFloat(get(el, "Media"));
			Float maxima = parseNullableFloat(get(el, "Maxima"));
			Float minima = parseNullableFloat(get(el, "Minima"));

			Long resumoId = ((Number) em.createNativeQuery("""
                INSERT INTO tb_resumo_mensal(
                    co_estacao, data_inicial, data_insercao_ana,
                    metodo_obtencao, nivel_consistencia,
                    vazao_media, vazao_maxima, vazao_minima
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING co_seq_resumo_mensal
            """)
					.setParameter(1, idEstacao)
					.setParameter(2, dataInicial)
					.setParameter(3, dataInsercao)
					.setParameter(4, metodo)
					.setParameter(5, nivel)
					.setParameter(6, media)
					.setParameter(7, maxima)
					.setParameter(8, minima)
					.getSingleResult()).longValue();

			Float maxReal = null, minReal = null, somaReal = 0f;
			int count = 0;

			for (int d = 1; d <= 31; d++) {
				String val = get(el, String.format("Vazao%02d", d));
				String status = get(el, String.format("Vazao%02dStatus", d));
				Float vazao = parseNullableFloat(val);
				Integer statusInt = parseNullableInt(status);
				LocalDate data = dataInicial.toLocalDate().plusDays(d - 1);

				em.createNativeQuery("""
                    INSERT INTO tb_vazao_diaria(co_resumo_mensal, data_vazao, vazao, vazao_status)
                    VALUES (?, ?, ?, ?)
                """)
						.setParameter(1, resumoId)
						.setParameter(2, Date.valueOf(data))
						.setParameter(3, vazao)
						.setParameter(4, statusInt)
						.executeUpdate();

				if (vazao != null) {
					somaReal += vazao;
					count++;
					maxReal = (maxReal == null || vazao > maxReal) ? vazao : maxReal;
					minReal = (minReal == null || vazao < minReal) ? vazao : minReal;
				}
			}

			em.createNativeQuery("""
                UPDATE tb_resumo_mensal
                SET vazao_maxima_real = ?, vazao_minima_real = ?, vazao_media_real = ?
                WHERE co_seq_resumo_mensal = ?
            """)
					.setParameter(1, maxReal)
					.setParameter(2, minReal)
					.setParameter(3, count > 0 ? somaReal / count : null)
					.setParameter(4, resumoId)
					.executeUpdate();
		}
	}

	private String get(Element el, String tag) {
		NodeList n = el.getElementsByTagName(tag);
		return (n.getLength() > 0) ? n.item(0).getTextContent().trim() : null;
	}

	private Integer parseNullableInt(String v) {
		try { return (v != null && !v.isBlank()) ? Integer.parseInt(v) : null; }
		catch (NumberFormatException e) { return null; }
	}

	private Float parseNullableFloat(String v) {
		try { return (v != null && !v.isBlank()) ? Float.parseFloat(v) : null; }
		catch (NumberFormatException e) { return null; }
	}

	private LocalDateTime parseDateTime(String v) {
		try {
			return (v != null && !v.isBlank()) ? LocalDateTime.parse(v, FORMATTER) : null;
		} catch (Exception e) {
			System.err.println("Erro ao fazer parse da data: " + v);
			return null;
		}
	}
}
