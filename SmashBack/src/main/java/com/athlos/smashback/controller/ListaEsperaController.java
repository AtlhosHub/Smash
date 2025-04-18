package com.athlos.smashback.controller;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.service.ListaEsperaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-espera")
public class ListaEsperaController {
    private final ListaEsperaService listaEsperaService;
    public ListaEsperaController(ListaEsperaService listaEsperaService) {
        this.listaEsperaService = listaEsperaService;
    }

    @GetMapping
    public ResponseEntity<List<ListaEsperaDTO>> listaEspera() {
        return listaEsperaService.listaEspera();
    }

    @PostMapping("/filtro")
    public ResponseEntity<List<ListaEsperaDTO>> listaEsperaFiltro(@RequestBody ListaEsperaFilter filtro) {
        return listaEsperaService.listaEsperaFiltro(filtro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaEspera> buscarInteressado(@PathVariable int id) {
        return listaEsperaService.buscarInteressado(id);
    }

    @PostMapping
    public ResponseEntity<ListaEspera> adicionarInteressado(@Valid @RequestBody ListaEspera listaEspera) {
        return listaEsperaService.adicionarInteressado(listaEspera);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInteressado(@PathVariable int id) {
        return listaEsperaService.deletarInteressado(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaEspera> atualizarInteressado(@PathVariable int id, @Valid @RequestBody ListaEspera novoInteressado) {
        return listaEsperaService.atualizarInteressado(id, novoInteressado);
    }
}
