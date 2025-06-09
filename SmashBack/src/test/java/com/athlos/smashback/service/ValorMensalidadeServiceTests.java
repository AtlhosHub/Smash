package com.athlos.smashback.service;

import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.ValorMensalidade;
import com.athlos.smashback.repository.ValorMensalidadeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ValorMensalidadeServiceTests {
    @InjectMocks
    private ValorMensalidadeService valorMensalidadeService;

    @Mock
    private ValorMensalidadeRepository valorMensalidadeRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        valorMensalidadeService = new ValorMensalidadeService(valorMensalidadeRepository);
    }

    @Test
    void buscarMensalidadeAtual() {
        ValorMensalidade v = new ValorMensalidade();
        v.setValor(120.0);

        when(valorMensalidadeRepository.findFirstByManualFalseAndDescontoFalseOrderByDataInclusaoDesc()).thenReturn(v);

        ValorMensalidade valor = valorMensalidadeService.buscarValorMensalidadeAtual();

        assertEquals(120.0, valor.getValor());
    }

    @Test
    void listarValoresMensalidade() {
        ValorMensalidade v1 = new ValorMensalidade();
        v1.setValor(100.0);
        ValorMensalidade v2 = new ValorMensalidade();
        v2.setValor(150.0);

        when(valorMensalidadeRepository.findAll()).thenReturn(List.of(v1, v2));

        ResponseEntity<List<ValorMensalidade>> resp = valorMensalidadeService.listarValoresMensalidade();

        assertEquals(2, resp.getBody().size());
        assertEquals(100.0, resp.getBody().get(0).getValor());
        assertEquals(150.0, resp.getBody().get(1).getValor());
    }

    @Test
    void buscarValorMensalidadePorIdSuccess() {
    ValorMensalidade v = new ValorMensalidade();
        v.setId(1);
        v.setValor(120.0);

        when(valorMensalidadeRepository.existsById(1)).thenReturn(true);
        when(valorMensalidadeRepository.findById(1)).thenReturn(Optional.of(v));

        ResponseEntity<ValorMensalidade> resp = valorMensalidadeService.buscarValorMensalidadePorId(1);

        assertEquals(120.0, resp.getBody().getValor());
    }

    @Test
    void buscarValorMensalidadePorIdNotFound() {
        when(valorMensalidadeRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            valorMensalidadeService.buscarValorMensalidadePorId(1);
        });

        assertEquals("Valor mensalidade não encontrado", exception.getMessage());
    }

    @Test
    void cadastrarValorMensalidade() {
        ValorMensalidade v = new ValorMensalidade();
        v.setValor(200.0);

        when(valorMensalidadeRepository.save(v)).thenReturn(v);

        ResponseEntity<ValorMensalidade> resp = valorMensalidadeService.cadastrarValorMensalidade(v);

        assertEquals(200.0, resp.getBody().getValor());
    }

    @Test
    void atualizarValorMensalidadeSuccess() {
        ValorMensalidade v = new ValorMensalidade();
        v.setId(1);
        v.setValor(200.0);

        ValorMensalidade novoValor = new ValorMensalidade();
        novoValor.setId(1);
        novoValor.setValor(250.0);

        when(valorMensalidadeRepository.findById(1)).thenReturn(Optional.of(v));
        when(valorMensalidadeRepository.save(v)).thenReturn(novoValor);

        ResponseEntity<ValorMensalidade> resp = valorMensalidadeService.atualizarValorMensalidade(1, novoValor);

        assertEquals(250.0, resp.getBody().getValor());
    }

    @Test
    void atualizarValorMensalidadeNotFound() {
        ValorMensalidade novoValor = new ValorMensalidade();
        novoValor.setId(1);
        novoValor.setValor(250.0);

        when(valorMensalidadeRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            valorMensalidadeService.atualizarValorMensalidade(1, novoValor);
        });

        assertEquals("Valor mensalidade não encontrado", exception.getMessage());
    }
}
