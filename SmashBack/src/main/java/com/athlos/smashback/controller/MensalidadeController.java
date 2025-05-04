package com.athlos.smashback.controller;

import com.athlos.smashback.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Registrar pagamento manualmente", description = "Registra o pagamento realizado por um aluno.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Mensalidade não encontrada", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
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
        }).orElseThrow(() -> new ResourceNotFoundException("Mensalidade não encontrada"));
    }
}
