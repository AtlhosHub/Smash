package com.athlos.smashback.service;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.ListaEsperaRepository;
import com.athlos.smashback.specification.ListaEsperaSpecification;
import org.springframework.data.domain.Sort;
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
        List<ListaEspera> listaEspera = listaEsperaRepository.findAll(Sort.by(Sort.Direction.ASC, "dataInteresse"));

        List<ListaEsperaDTO> interessados = listaEspera.stream().map(interessado -> new ListaEsperaDTO(interessado.getId(), (interessado.getNomeSocial() == null || interessado.getNomeSocial().trim().isEmpty()) ? interessado.getNome() : interessado.getNomeSocial(), interessado.getDataInteresse(), interessado.getHorarioPref())).toList();
        return ResponseEntity.ok(listaEspera.isEmpty() ? List.of() : interessados);
    }

    public ResponseEntity<List<ListaEsperaDTO>> listaEsperaFiltro(ListaEsperaFilter filtro){
        Specification<ListaEspera> spec = ListaEsperaSpecification.filtrarPor(filtro);
        List<ListaEspera> listaEspera = listaEsperaRepository.findAll(Specification.where(spec), Sort.by(Sort.Direction.ASC, "dataInteresse"));

        List<ListaEsperaDTO> interessados = listaEspera.stream().map(interessado -> new ListaEsperaDTO(interessado.getId(), (interessado.getNomeSocial() == null || interessado.getNomeSocial().trim().isEmpty()) ? interessado.getNome() : interessado.getNomeSocial(), interessado.getDataInteresse(), interessado.getHorarioPref())).toList();
        return ResponseEntity.ok(listaEspera.isEmpty() ? List.of() : interessados);
    }


    public ResponseEntity<ListaEspera> buscarInteressado(int id) {
        if(!listaEsperaRepository.existsById(id)){
            throw new ResourceNotFoundException("Interessado não encontrado");
        }
        return ResponseEntity.ok(listaEsperaRepository.findById(id).get());
    }

    public ResponseEntity<ListaEspera> adicionarInteressado(ListaEspera listaEspera) {
        if(listaEsperaRepository.existsByNomeAndEmailIgnoreCase(listaEspera.getNome(), listaEspera.getEmail())){
            throw new DataConflictException("Nome e e-mail de interessado já cadastrados");
        }

        listaEsperaRepository.save(listaEspera);

        return ResponseEntity.ok(listaEspera);
    }

    public ResponseEntity<Void> deletarInteressado(int id){
        if(listaEsperaRepository.existsById(id)){
            listaEsperaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("Interessado não encontrado");
    }

    public ResponseEntity<ListaEspera> atualizarInteressado(int id, ListaEspera novoInteressado) {
        if(listaEsperaRepository.existsByNomeAndEmailIgnoreCaseAndIdIsNot(novoInteressado.getNome(), novoInteressado.getEmail(), id)){
            throw new DataConflictException("Nome e e-mail de interessado já cadastrados");
        }

        return listaEsperaRepository.findById(id).map(interessado -> {
            interessado.setNome(novoInteressado.getNome());
            interessado.setEmail(novoInteressado.getEmail());
            interessado.setCelular(novoInteressado.getCelular());
            interessado.setDataInteresse(novoInteressado.getDataInteresse());
            interessado.setNomeSocial(novoInteressado.getNomeSocial());
            interessado.setTelefone(novoInteressado.getTelefone());
            interessado.setHorarioPref(novoInteressado.getHorarioPref());
            listaEsperaRepository.save(interessado);
            return ResponseEntity.ok(interessado);
        }).orElseThrow(() -> new ResourceNotFoundException("Interessado não encontrado"));
    }
}
