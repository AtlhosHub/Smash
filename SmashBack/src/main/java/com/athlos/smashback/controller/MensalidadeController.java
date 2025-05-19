package com.athlos.smashback.controller;

import com.athlos.smashback.dto.GraficoDTO;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.ValorMensalidade;
import com.athlos.smashback.repository.ValorMensalidadeRepository;
import com.athlos.smashback.service.MensalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.athlos.smashback.dto.PagamentoManualDTO;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.MensalidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mensalidades")
@SecurityRequirement(name = "Bearer")
@Tag(name = "MensalidadeController", description = "Endpoints para gerenciar os as mensalidades no sistema")
public class MensalidadeController {
    @Autowired
    private MensalidadeService mensalidadeService;

    @Autowired
    private MensalidadeRepository mensalidadeRepository;
    @Autowired
    private ValorMensalidadeRepository valorMensalidadeRepository;

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


        // ...existing code...
        ValorMensalidade valor = valorMensalidadeRepository
            .findByValorAndManual(dto.getValorPago().getValor(), true)
            .orElseGet(() -> {
                ValorMensalidade novo = new ValorMensalidade();
                novo.setValor(dto.getValorPago().getValor());
                novo.setManual(true);
                return valorMensalidadeRepository.save(novo);
            });

        return mensalidadeRepository.findById(id).map(m -> {
            m.setStatus(Status.PAGO);
            m.setDataPagamento(LocalDateTime.now());
            m.setValor(valor);
            m.setFormaPagamento(dto.getFormaPagamento());
            mensalidadeRepository.save(m);
            return ResponseEntity.ok(m);
        }).orElseThrow(() -> new ResourceNotFoundException("Mensalidade não encontrada"));
    }

    @GetMapping("/qtd-descontos")
    @Operation(summary = "Contar mensalidades com desconto", description = "Retorna a quantidade de mensalidades pagas com desconto no mês atual.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantidade de mensalidades com desconto retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Integer> countMensalidadesDesconto() {
        return mensalidadeService.countMensalidadesDesconto();
    }

    @GetMapping("/grafico")
    @Operation(summary = "Gerar gráfico de mensalidades", description = "Retorna os dados para gerar um gráfico de mensalidades pagas e pendentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do gráfico retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<GraficoDTO>> graficoMensalidade() {
        return mensalidadeService.graficoMensalidade();
    }
}
