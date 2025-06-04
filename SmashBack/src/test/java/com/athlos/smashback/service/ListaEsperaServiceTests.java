package com.athlos.smashback.service;

import com.athlos.smashback.dto.ListaEsperaDTO;
import com.athlos.smashback.filter.ListaEsperaFilter;
import com.athlos.smashback.model.HorarioPref;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.repository.ListaEsperaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;


import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class ListaEsperaServiceTests {

    @InjectMocks
    private ListaEsperaService listaEsperaService;

    @Mock
    private ListaEsperaRepository listaEsperaRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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
}
