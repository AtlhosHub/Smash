package com.athlos.smashback.service;

import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.ValorMensalidade;
import com.athlos.smashback.repository.ValorMensalidadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValorMensalidadeService {
    private final ValorMensalidadeRepository valorMensalidadeRepository;
    public ValorMensalidadeService(ValorMensalidadeRepository valorMensalidadeRepository) {
        this.valorMensalidadeRepository = valorMensalidadeRepository;
    }

    public ValorMensalidade buscarValorMensalidadeAtual() {
        ValorMensalidade atual = valorMensalidadeRepository.findFirstByManualFalseAndDescontoFalseOrderByDataInclusaoDesc();
        return atual;
    }

    public ResponseEntity<List<ValorMensalidade>> listarValoresMensalidade() {
        List<ValorMensalidade> valoresMensalidade = valorMensalidadeRepository.findAll();
        return ResponseEntity.ok(valoresMensalidade);
    }

    public ResponseEntity<ValorMensalidade> buscarValorMensalidadePorId(int id) {
        if(!valorMensalidadeRepository.existsById(id)){
            throw new ResourceNotFoundException("Valor mensalidade não encontrado");
        }
        return ResponseEntity.ok(valorMensalidadeRepository.findById(id).get());
    }

    public ResponseEntity<ValorMensalidade> cadastrarValorMensalidade(ValorMensalidade valorMensalidade) {
        ValorMensalidade valor = valorMensalidadeRepository.save(valorMensalidade);
        return ResponseEntity.ok(valor);
    }

    public ResponseEntity<ValorMensalidade> atualizarValorMensalidade(int id, ValorMensalidade novoValorMensalidade) {
        return valorMensalidadeRepository.findById(id).map(valorMensalidade -> {
            valorMensalidade.setValor(novoValorMensalidade.getValor());
            ValorMensalidade valor =  valorMensalidadeRepository.save(valorMensalidade);
            return ResponseEntity.ok(valor);
        }).orElseThrow(() -> new ResourceNotFoundException("Valor mensalidade não encontrado"));
    }
}
