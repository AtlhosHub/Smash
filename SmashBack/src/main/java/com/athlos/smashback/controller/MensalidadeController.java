package com.athlos.smashback.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;
import com.athlos.smashback.dto.PagamentoManualDTO;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.MensalidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/mensalidades")
@SecurityRequirement(name = "Bearer")
@Tag(name = "MensalidadeController", description = "Endpoints para gerenciar os as mensalidades no sistema")
public class MensalidadeController {

    @Autowired
    private MensalidadeRepository mensalidadeRepository;

    @PutMapping("/{id}/pagar")
    public ResponseEntity<Mensalidade> pagarManual(
            @PathVariable Long id,
            @RequestBody @Valid PagamentoManualDTO dto) {

        return mensalidadeRepository.findById(id).map(m -> {
            m.setStatus(Status.PAGO);
            m.setDataPagamento(LocalDateTime.now());
            m.setValor(dto.getValorPago());
            m.setFormaPagamento(dto.getFormaPagamento());
            mensalidadeRepository.save(m);
            return ResponseEntity.ok(m);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
