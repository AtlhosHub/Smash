package com.athlos.smashback.service;

import com.athlos.smashback.dto.GraficoDTO;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.MensalidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class MensalidadeServiceTests {
    @InjectMocks
    private MensalidadeService mensalidadeService;

    @Mock
    private MensalidadeRepository mensalidadeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mensalidadeService = new MensalidadeService(mensalidadeRepository);
    }

    @Test
    void countMensalidadesDesconto() {
        when(mensalidadeRepository.countMensalidadesDesconto()).thenReturn(5);

        assertEquals(5, mensalidadeService.countMensalidadesDesconto().getBody());
    }

    @Test
    void graficoMensalidadeNotEmpty() {
        when(mensalidadeRepository.graficoMensalidade()).thenReturn(List.of(new GraficoDTO(1, 3L,10L,2L)));

        ResponseEntity<List<GraficoDTO>> resp = mensalidadeService.graficoMensalidade();
        assertEquals(1, resp.getBody().size());
        assertEquals(1, resp.getBody().get(0).getMes());
        assertEquals(10L, resp.getBody().get(0).getPagos());
    }

    @Test
    void graficoMensalidadeEmpty() {
        when(mensalidadeRepository.graficoMensalidade()).thenReturn(List.of());

        ResponseEntity<List<GraficoDTO>> resp = mensalidadeService.graficoMensalidade();
        assertEquals(0, resp.getBody().size());
    }

    @Test
    void atualizarStatusMensalidades() {
        Mensalidade m = new Mensalidade();
        m.setStatus(Status.PENDENTE);

        Mensalidade m2 = new Mensalidade();
        m2.setStatus(Status.PENDENTE);

        Mensalidade m3 = new Mensalidade();
        m3.setStatus(Status.PENDENTE);

        when(mensalidadeRepository.findByStatusAndDataVencimentoBefore(Status.PENDENTE, LocalDate.now())).thenReturn(List.of(m, m2, m3));
        when(mensalidadeRepository.save(m)).thenReturn(m);
        when(mensalidadeRepository.save(m2)).thenReturn(m2);
        when(mensalidadeRepository.save(m3)).thenReturn(m3);

        mensalidadeService.atualizarStatusMensalidades();

        assertEquals(Status.ATRASADO, m.getStatus());
        assertEquals(Status.ATRASADO, m2.getStatus());
        assertEquals(Status.ATRASADO, m3.getStatus());
        verify(mensalidadeRepository, times(3)).save(any(Mensalidade.class));
    }
}
