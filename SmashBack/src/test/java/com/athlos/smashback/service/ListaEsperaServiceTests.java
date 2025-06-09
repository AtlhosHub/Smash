package com.athlos.smashback.service;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.HorarioPrefRepository;
import com.athlos.smashback.repository.ListaEsperaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ListaEsperaServiceTests {

    @InjectMocks
    private ListaEsperaService listaEsperaService;

    @Mock
    private ListaEsperaRepository listaEsperaRepository;

    @InjectMocks
    private HorarioPrefService horarioPrefService;

    @Mock
    private HorarioPrefRepository horarioPrefRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        horarioPrefService = new HorarioPrefService(horarioPrefRepository);
        listaEsperaService = new ListaEsperaService(listaEsperaRepository);
    }

    @Test
    void listaEsperaSuccess() {
        ListaEspera l = new ListaEspera();
        HorarioPref p = new HorarioPref();
        l.setId(1);
        l.setNome("João Silva");
        l.setNomeSocial("Joãozinho");
        l.setDataInteresse(LocalDateTime.now());
        l.setHorarioPref(p);

        ListaEspera l2 = new ListaEspera();
        l2.setId(2);
        l2.setNome("Maria Oliveira");
        l2.setNomeSocial("   ");
        l2.setDataInteresse(LocalDateTime.now());
        l2.setHorarioPref(p);

        ListaEspera l3 = new ListaEspera();
        l3.setId(3);
        l3.setNome("Carlos Pereira");
        l3.setNomeSocial(null);
        l3.setDataInteresse(LocalDateTime.now());
        l3.setHorarioPref(p);

        when(listaEsperaRepository.findAll(any(Sort.class))).thenReturn(List.of(l, l2, l3));

        ResponseEntity<List<ListaEsperaDTO>> resp = listaEsperaService.listaEspera();

        assertEquals(3, resp.getBody().size());
        assertEquals("Joãozinho", resp.getBody().get(0).getNome());
        assertEquals("Maria Oliveira", resp.getBody().get(1).getNome());
        assertEquals("Carlos Pereira", resp.getBody().get(2).getNome());
    }

    @Test
    void listaEsperaEmpty() {
        when(listaEsperaRepository.findAll(any(Sort.class))).thenReturn(List.of());

        ResponseEntity<List<ListaEsperaDTO>> resp = listaEsperaService.listaEspera();

        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void listaEsperaFiltered(){
        ListaEsperaFilter lf = new ListaEsperaFilter();
        ListaEspera l = new ListaEspera();
        HorarioPref p = new HorarioPref();
        l.setId(1);
        l.setNome("João Silva");
        l.setNomeSocial("Joãozinho");
        l.setDataInteresse(LocalDateTime.now());
        l.setHorarioPref(p);

        ListaEspera l2 = new ListaEspera();
        l2.setId(2);
        l2.setNome("Maria Oliveira");
        l2.setNomeSocial("   ");
        l2.setDataInteresse(LocalDateTime.now());
        l2.setHorarioPref(p);

        ListaEspera l3 = new ListaEspera();
        l3.setId(3);
        l3.setNome("Carlos Pereira");
        l3.setNomeSocial(null);
        l3.setDataInteresse(LocalDateTime.now());
        l3.setHorarioPref(p);

        when(listaEsperaRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(l, l2, l3));

        ResponseEntity<List<ListaEsperaDTO>> resp = listaEsperaService.listaEsperaFiltro(lf);

        assertEquals(3, resp.getBody().size());
        assertEquals("Joãozinho", resp.getBody().get(0).getNome());
        assertEquals("Maria Oliveira", resp.getBody().get(1).getNome());
        assertEquals("Carlos Pereira", resp.getBody().get(2).getNome());
    }

    @Test
    void listaEsperaFilteredEmpty() {
        ListaEsperaFilter lf = new ListaEsperaFilter();
        when(listaEsperaRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        ResponseEntity<List<ListaEsperaDTO>> resp = listaEsperaService.listaEsperaFiltro(lf);

        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void listaEsperaDeletedHorario(){
        HorarioPref h = new HorarioPref();
        h.setId(1);
        h.setHorarioAulaInicio(LocalTime.now());
        h.setHorarioAulaFim(LocalTime.now());

        HorarioPref h2 = new HorarioPref();
        h2.setId(2);
        h2.setHorarioAulaInicio(LocalTime.now().plusHours(1));
        h2.setHorarioAulaFim(LocalTime.now().plusHours(2));

        ListaEspera l = new ListaEspera();
        l.setId(1);
        l.setNome("João Silva");
        l.setHorarioPref(h);

        ListaEspera l2 = new ListaEspera();
        l2.setId(2);
        l2.setNome("Maria Oliveira");
        l2.setHorarioPref(h2);

        h.setInteressados(List.of(l));

        when(horarioPrefRepository.existsById(1)).thenReturn(true);
        when(horarioPrefRepository.findById(1)).thenReturn(Optional.of(h));

        horarioPrefService.deletarHorario(1);

        when(listaEsperaRepository.findAll(any(Sort.class))).thenReturn(List.of(l,l2));

        ResponseEntity<List<ListaEsperaDTO>> resp = listaEsperaService.listaEspera();

        assertNull(resp.getBody().get(0).getHorarioPreferencia());
        assertEquals(h2 ,resp.getBody().get(1).getHorarioPreferencia());
    }

    @Test
    void buscarInteressadoPorIdSuccess(){
        ListaEspera l = new ListaEspera();
        l.setId(1);
        l.setNome("João Silva");
        l.setDataInteresse(LocalDateTime.now());

        when(listaEsperaRepository.existsById(1)).thenReturn(true);
        when(listaEsperaRepository.findById(1)).thenReturn(Optional.of(l));

        ResponseEntity<ListaEspera> resp = listaEsperaService.buscarInteressado(1);

        assertEquals("João Silva", resp.getBody().getNome());
    }

    @Test
    void buscarInteressadoNotFound(){
        when(listaEsperaRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            listaEsperaService.buscarInteressado(1);
        });

        assertEquals("Interessado não encontrado", exception.getMessage());
    }

    @Test
    void adicionarInteressadoSuccess() {
        ListaEspera l = new ListaEspera();
        l.setNome("João Silva");
        l.setEmail("joao.silva@email.com");
        l.setCelular("(11) 91234-5678");
        l.setDataInteresse(LocalDateTime.now());

        when(listaEsperaRepository.existsByNomeAndEmailIgnoreCase(l.getNome(), l.getEmail())).thenReturn(false);
        when(listaEsperaRepository.save(l)).thenReturn(l);

        ResponseEntity<ListaEspera> resp = listaEsperaService.adicionarInteressado(l);

        assertEquals("João Silva", resp.getBody().getNome());
    }

    @Test
    void adicionarInteressadoConflict() {
        ListaEspera l = new ListaEspera();
        l.setId(1);
        l.setNome("João Silva");
        l.setEmail("joao.silva@email.com");

        when(listaEsperaRepository.existsByNomeAndEmailIgnoreCase(l.getNome(), l.getEmail())).thenReturn(true);

        Exception exception = assertThrows(DataConflictException.class, () -> {
            listaEsperaService.adicionarInteressado(l);
        });
    }

    @Test
    void deletarInteressadoSuccess() {
        when(listaEsperaRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> resp = listaEsperaService.deletarInteressado(1);

        assertEquals(204, resp.getStatusCodeValue());
        verify(listaEsperaRepository, times(1)).deleteById(1);
    }

    @Test
    void deletarInteressadoNotFound() {
        when(listaEsperaRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            listaEsperaService.deletarInteressado(1);
        });

        assertEquals("Interessado não encontrado", exception.getMessage());
    }

    @Test
    void atualizarInteressadoSuccess() {
        ListaEspera novo = new ListaEspera();
        novo.setId(1);
        novo.setNome("João Silva Novo");
        novo.setEmail("joao.novo@email.com");

        ListaEspera existente = new ListaEspera();
        existente.setId(1);
        existente.setNome("João Silva");
        existente.setEmail("joao.silva@email.com");

        when(listaEsperaRepository.existsByNomeAndEmailIgnoreCaseAndIdIsNot(novo.getNome(), novo.getEmail(), novo.getId())).thenReturn(false);
        when(listaEsperaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(listaEsperaRepository.save(existente)).thenReturn(novo);

        ResponseEntity<ListaEspera> resp = listaEsperaService.atualizarInteressado(1, novo);

        assertEquals("João Silva Novo", resp.getBody().getNome());
        assertEquals("joao.novo@email.com", resp.getBody().getEmail());
    }

    @Test
    void atualizarInteressadoConflict() {
        ListaEspera novo = new ListaEspera();
        novo.setNome("novo");
        novo.setEmail("conflict@email.com");

        when(listaEsperaRepository.existsByNomeAndEmailIgnoreCaseAndIdIsNot("novo", "conflict@email.com", 1)).thenReturn(true);

        assertThrows(DataConflictException.class, () -> listaEsperaService.atualizarInteressado(1, novo));
    }

    @Test
    void atualizarInteressadoNotFound() {
        ListaEspera novo = new ListaEspera();
        novo.setId(1);
        novo.setNome("João Silva Novo");
        novo.setEmail("joao.novo@email.com");

        when(listaEsperaRepository.existsByNomeAndEmailIgnoreCaseAndIdIsNot(novo.getNome(), novo.getEmail(), novo.getId())).thenReturn(false);
        when(listaEsperaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            listaEsperaService.atualizarInteressado(1, novo);
        });
    }
}
