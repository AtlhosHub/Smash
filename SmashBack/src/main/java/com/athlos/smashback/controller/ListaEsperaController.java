package com.athlos.smashback.controller;

import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.ListaEsperaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-espera")
public class ListaEsperaController {
    private final ListaEsperaRepository listaEsperaRepository;

    public ListaEsperaController(ListaEsperaRepository listaEsperaRepository) {
        this.listaEsperaRepository = listaEsperaRepository;
    }

    @GetMapping
    public ResponseEntity<List<ListaEspera>> listaEspera() {
        List<ListaEspera> listaEspera = listaEsperaRepository.findAll();
        return listaEspera.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listaEspera);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaEspera> buscarInteressado(@PathVariable int id) {
        return listaEsperaRepository.existsById(id) ? ResponseEntity.ok(listaEsperaRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ListaEspera> adicionarInteressado(@RequestBody ListaEspera listaEspera) {
        return listaEsperaRepository.existsByNomeAndEmail(listaEspera.getNome(), listaEspera.getEmail()) ? ResponseEntity.status(409).body(listaEspera) : ResponseEntity.ok(listaEsperaRepository.save(listaEspera));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInteressado(@PathVariable int id) {
        if(listaEsperaRepository.existsById(id)){
            listaEsperaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaEspera> atualizarInteressado(@PathVariable int id, @RequestBody ListaEspera novoInteressado) {
        if(listaEsperaRepository.existsByNomeAndEmailAndIdIsNot(novoInteressado.getNome(), novoInteressado.getEmail(), id)){
            System.out.println("inferno");
            return ResponseEntity.status(409).body(novoInteressado);
        }

        return listaEsperaRepository.findById(id).map(interessado -> {
            interessado.setNome(novoInteressado.getNome());
            interessado.setEmail(novoInteressado.getEmail());
            interessado.setCelular(novoInteressado.getCelular());
            interessado.setDataInteresse(novoInteressado.getDataInteresse());
            interessado.setHorarioPreferencia(novoInteressado.getHorarioPreferencia());
            return ResponseEntity.ok(listaEsperaRepository.save(interessado));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
