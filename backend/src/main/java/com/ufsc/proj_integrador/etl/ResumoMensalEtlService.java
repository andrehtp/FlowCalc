package com.ufsc.proj_integrador.etl;

import com.ufsc.proj_integrador.model.ResumoMensal;
import com.ufsc.proj_integrador.model.VazaoDiaria;
import com.ufsc.proj_integrador.repository.EstacaoRepository;
import com.ufsc.proj_integrador.repository.ResumoMensalRepository;
import com.ufsc.proj_integrador.repository.VazaoDiariaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumoMensalEtlService {

    private static final int PAGE_SIZE = 500;

    private final EstacaoRepository estacaoRepository;
    private final ResumoMensalRepository resumoMensalRepository;
    private final VazaoDiariaRepository vazaoDiariaRepository;

    @Scheduled(cron = "0 0 1 10 * *") // Executa à 01:00h do dia 10º de cada mês
    @Transactional
    public void run() {
        log.info("Iniciando processo de ETL");

        int page = 0;
        Page<Long> pagina;

        do {
            pagina = estacaoRepository.getCodigosEstacoesPaginado(PageRequest.of(page, PAGE_SIZE));
            List<Long> codigos = pagina.getContent();

            for (Long codigoEstacao : codigos) {
                log.info("Processando estação {}", codigoEstacao);
                Document doc = requestHidroSerieHistorica(codigoEstacao);
                if (doc == null) continue;

                NodeList series = doc.getElementsByTagName("SerieHistorica");
                for (int i = 0; i < series.getLength(); i++) {
                    Element el = (Element) series.item(i);
                    try {
                        ResumoMensal resumo = new ResumoMensal();
                        resumo.setDataInicial(parseDateTime(el, "DataHora"));
                        resumo.setDataInsercaoAna(parseDateTime(el, "DataIns"));
                        resumo.setMetodoObtencao(parseInt(el, "MetodoObtencaoVazoes"));
                        resumo.setNivelConsistencia(parseInt(el, "NivelConsistencia"));
                        resumo.setVazaoMedia(parseFloat(el, "Media"));
                        resumo.setVazaoMaxima(parseFloat(el, "Maxima"));
                        resumo.setVazaoMinima(parseFloat(el, "Minima"));

                        resumo = resumoMensalRepository.save(resumo);

                        for (int dia = 1; dia <= 31; dia++) {
                            String valor = get(el, String.format("Vazao%02d", dia));
                            if (valor == null || valor.isBlank()) continue;

                            VazaoDiaria vd = new VazaoDiaria();
                            vd.setResumoMensal(resumo);
                            vd.setVazao(Float.parseFloat(valor));
                            vd.setVazaoStatus(parseInt(el, String.format("Vazao%02dStatus", dia)));

                            vazaoDiariaRepository.save(vd);
                        }
                    } catch (Exception e) {
                        log.error("Erro processando estação {}: {}", codigoEstacao, e.getMessage());
                    }
                }
            }
            page++;
        } while (pagina.hasNext());

        log.info("ETL finalizado com sucesso!");
    }

    private Document requestHidroSerieHistorica(Long codEstacao) {
        try {
            String xml = """
                <?xml version=\"1.0\" encoding=\"utf-8\"?>
                <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
                               xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"
                               xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">
                  <soap:Body>
                    <HidroSerieHistorica xmlns=\"http://MRCS/\">
                      <codEstacao>%s</codEstacao>
                      <dataInicio></dataInicio>
                      <dataFim></dataFim>
                      <tipoDados>3</tipoDados>
                      <nivelConsistencia></nivelConsistencia>
                    </HidroSerieHistorica>
                  </soap:Body>
                </soap:Envelope>
            """.formatted(codEstacao);

            HttpURLConnection conn = (HttpURLConnection) new URL("http://telemetriaws1.ana.gov.br/ServiceANA.asmx").openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "http://MRCS/HidroSerieHistorica");
            conn.setDoOutput(true);
            conn.getOutputStream().write(xml.getBytes("UTF-8"));

            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());

        } catch (Exception e) {
            log.error("Erro ao requisitar XML da estação {}: {}", codEstacao, e.getMessage());
            return null;
        }
    }

    private static String get(Element el, String tag) {
        NodeList n = el.getElementsByTagName(tag);
        return (n.getLength() > 0) ? n.item(0).getTextContent().trim() : null;
    }

    private static LocalDateTime parseDateTime(Element el, String tag) {
        String value = get(el, tag);
        return (value != null && !value.isBlank()) ? Timestamp.valueOf(value.replace("T", " ")).toLocalDateTime() : null;
    }

    private static Integer parseInt(Element el, String tag) {
        String value = get(el, tag);
        return (value != null && !value.isBlank()) ? Integer.parseInt(value) : null;
    }

    private static Float parseFloat(Element el, String tag) {
        String value = get(el, tag);
        return (value != null && !value.isBlank()) ? Float.parseFloat(value) : null;
    }
}
