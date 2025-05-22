package com.athlos.smashback.controller;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.service.ListaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-espera")
@SecurityRequirement(name = "Bearer")
@Tag(name = "ListaEsperaController", description = "Endpoints para gerenciar interessados na lista de espera")
public class ListaEsperaController {
    private final ListaEsperaService listaEsperaService;

    public ListaEsperaController(ListaEsperaService listaEsperaService) {
        this.listaEsperaService = listaEsperaService;
    }

    @GetMapping
    @Operation(summary = "Listar interessados na lista de espera", description = "Retorna uma lista de todos os interessados cadastrados na lista de espera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de interessados retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<ListaEsperaDTO>> listaEspera() {
        return listaEsperaService.listaEspera();
    }

    @PostMapping("/filtro")
    @Operation(summary = "Filtrar interessados na lista de espera", description = "Retorna uma lista de interessados com base nos critérios do filtro fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de interessados retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<List<ListaEsperaDTO>> listaEsperaFiltro(
            @RequestBody @Parameter(description = "Critérios para filtrar os interessados na lista de espera") ListaEsperaFilter filtro) {
        return listaEsperaService.listaEsperaFiltro(filtro);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar interessado por ID", description = "Retorna os detalhes de um interessado específico com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do interessado retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Interessado não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ListaEspera> buscarInteressado(
            @Parameter(description = "ID do interessado a ser buscado", example = "1") @PathVariable int id) {
        return listaEsperaService.buscarInteressado(id);
    }

    @PostMapping
    @Operation(summary = "Adicionar um novo interessado", description = "Adiciona um novo interessado à lista de espera.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Interessado cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do interessado não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Nome e e-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ListaEspera> adicionarInteressado(
            @Valid @RequestBody @Parameter(description = "Dados do interessado a ser adicionado") ListaEspera listaEspera) {
        return listaEsperaService.adicionarInteressado(listaEspera);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar interessado por ID", description = "Remove um interessado da lista de espera com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Interessado deletado com sucesso", content = @Content()),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Interessado não encontrado", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<Void> deletarInteressado(
            @Parameter(description = "ID do interessado a ser deletado", example = "1") @PathVariable int id) {
        return listaEsperaService.deletarInteressado(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar interessado por ID", description = "Atualiza os dados de um interessado existente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Interessado atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "Erro(s) de validação: nome: O nome do interessado não pode ficar em branco"))),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Interessado não encontrado", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Nome e e-mail já cadastrados", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content())
    })
    public ResponseEntity<ListaEspera> atualizarInteressado(
            @Parameter(description = "ID do interessado a ser atualizado", example = "1") @PathVariable int id,
            @Valid @RequestBody @Parameter(description = "Novos dados do interessado") ListaEspera novoInteressado) {
        return listaEsperaService.atualizarInteressado(id, novoInteressado);
    }
}
