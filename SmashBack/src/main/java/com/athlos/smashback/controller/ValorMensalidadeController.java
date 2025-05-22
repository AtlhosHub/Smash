package com.athlos.smashback.controller;

import com.athlos.smashback.model.ValorMensalidade;
import com.athlos.smashback.service.ValorMensalidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/valor-mensalidade")
@SecurityRequirement(name = "Bearer")
@Tag(name = "ValorMensalidadeController", description = "Endpoints para gerenciar valores de mensalidade no sistema")
public class ValorMensalidadeController {
    private final ValorMensalidadeService valorMensalidadeService;
    public ValorMensalidadeController(ValorMensalidadeService valorMensalidadeService) {
        this.valorMensalidadeService = valorMensalidadeService;
    }

    @GetMapping
    @Operation(summary = "Listar valores de mensalidade", description = "Retorna uma lista de todos os valores de mensalidade cadastrados no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de valores retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<ValorMensalidade>> listarValoresMensalidade(){
        return valorMensalidadeService.listarValoresMensalidade();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar valor de mensalidade por ID", description = "Retorna os detalhes de um valor de mensalidade específico com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor de mensalidade retornado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Valor de mensalidade não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ValorMensalidade> buscarValorMensalidade(
            @Parameter(description = "ID do valor de mensalidade a ser buscado", example = "1") @PathVariable int id
    ){
        return valorMensalidadeService.buscarValorMensalidadePorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo valor de mensalidade", description = "Adiciona um novo valor de mensalidade ao sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor de mensalidade cadastrado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: valor: Valor deve ser maior que zero"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ValorMensalidade> cadastrarValorMensalidade(
            @Parameter(description = "Dados do valor de mensalidade a ser cadastrada") @RequestBody ValorMensalidade valorMensalidade){
        return valorMensalidadeService.cadastrarValorMensalidade(valorMensalidade);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar valor de mensalidade por ID", description = "Atualiza os dados de um valor de mensalidade existente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Valor atualizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Valor não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ValorMensalidade> atualizarValor(
            @Parameter(description = "ID do valor de mensalidade a ser atualizado", example = "1") @PathVariable int id,
            @RequestBody @Parameter(description = "Novos dados do valor de mensalidade") ValorMensalidade novoValorMensalidade) {
        return valorMensalidadeService.atualizarValorMensalidade(id, novoValorMensalidade);
    }
}
