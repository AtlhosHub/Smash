package com.athlos.smashback.service;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.ListaEsperaRepository;
import com.athlos.smashback.specification.ListaEsperaSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListaEsperaService {
    final private ListaEsperaRepository listaEsperaRepository;
    public ListaEsperaService(ListaEsperaRepository listaEsperaRepository) {
        this.listaEsperaRepository = listaEsperaRepository;
    }

    public ResponseEntity<List<ListaEsperaDTO>> listaEspera() {
        List<ListaEspera> listaEspera = listaEsperaRepository.findAll();

        List<ListaEsperaDTO> interessados = listaEspera.stream().map(interessado -> new ListaEsperaDTO(interessado.getId(), interessado.getNome(), interessado.getDataInteresse(), interessado.getHorarioPref().getHorarioAula())).toList();
        return listaEspera.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(interessados);
    }

    public ResponseEntity<List<ListaEsperaDTO>> listaEsperaFiltro(ListaEsperaFilter filtro){
        Specification<ListaEspera> spec = ListaEsperaSpecification.filtrarPor(filtro);
        List<ListaEspera> listaEspera = listaEsperaRepository.findAll(spec);

        List<ListaEsperaDTO> interessados = listaEspera.stream().map(interessado -> new ListaEsperaDTO(interessado.getId(), interessado.getNome(), interessado.getDataInteresse(), interessado.getHorarioPref().getHorarioAula())).toList();
        return listaEspera.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(interessados);
    }

    public ResponseEntity<ListaEspera> buscarInteressado(int id) {
        return listaEsperaRepository.existsById(id) ? ResponseEntity.ok(listaEsperaRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    public ResponseEntity<ListaEspera> adicionarInteressado(ListaEspera listaEspera) {
        return listaEsperaRepository.existsByNomeAndEmail(listaEspera.getNome(), listaEspera.getEmail()) ? ResponseEntity.status(409).body(listaEspera) : ResponseEntity.ok(listaEsperaRepository.save(listaEspera));
    }

    public ResponseEntity<Void> deletarInteressado(int id){
        if(listaEsperaRepository.existsById(id)){
            listaEsperaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<ListaEspera> atualizarInteressado(int id, ListaEspera novoInteressado) {
        if(listaEsperaRepository.existsByNomeAndEmailAndIdIsNot(novoInteressado.getNome(), novoInteressado.getEmail(), id)){
            System.out.println("inferno");
            return ResponseEntity.status(409).body(novoInteressado);
        }

        return listaEsperaRepository.findById(id).map(interessado -> {
            interessado.setNome(novoInteressado.getNome());
            interessado.setEmail(novoInteressado.getEmail());
            interessado.setCelular(novoInteressado.getCelular());
            interessado.setDataInteresse(novoInteressado.getDataInteresse());
            interessado.setNomeSocial(novoInteressado.getNomeSocial());
            interessado.setTelefone(novoInteressado.getTelefone());
            interessado.setHorarioPref(novoInteressado.getHorarioPref());
            return ResponseEntity.ok(listaEsperaRepository.save(interessado));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
