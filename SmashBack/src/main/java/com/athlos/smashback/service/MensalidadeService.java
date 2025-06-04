package com.athlos.smashback.service;

import com.athlos.smashback.dto.GraficoDTO;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.repository.MensalidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MensalidadeService {
    private final MensalidadeRepository mensalidadeRepository;

    public MensalidadeService(MensalidadeRepository mensalidadeRepository) {
        this.mensalidadeRepository = mensalidadeRepository;
    }

    @Scheduled(cron = "0 1 0 * * *") //todo dia as 00:01 ele atualiza as mensalidades
    @Transactional
    public void atualizarStatusMensalidades() {
        LocalDate hoje = LocalDate.now();

        List<Mensalidade> mensalidadesAtrasadas = mensalidadeRepository
                .findByStatusAndDataVencimentoBefore(Status.PENDENTE, hoje);

        mensalidadesAtrasadas.forEach(m -> {
            m.setStatus(Status.ATRASADO);

            if (m.getDataVencimento().getMonthValue() < hoje.getMonthValue() ||
                    m.getDataVencimento().getYear() < hoje.getYear()) {
                m.getValor().setValor(m.getValor().getValor() + 3.0);            }

            mensalidadeRepository.save(m);
        });

        System.out.println("🔄 Mensalidades atualizadas: " + mensalidadesAtrasadas.size());
    }

    public ResponseEntity<Integer> countMensalidadesDesconto() {
        return ResponseEntity.ok(mensalidadeRepository.countMensalidadesDesconto());
    }

    public ResponseEntity<List<GraficoDTO>> graficoMensalidade() {
        List<GraficoDTO> grafico = mensalidadeRepository.graficoMensalidade();
        return ResponseEntity.ok(grafico.isEmpty() ? List.of() : grafico);
    }
}