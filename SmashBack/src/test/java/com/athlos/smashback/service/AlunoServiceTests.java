package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoAniversarioDTO;
import com.athlos.smashback.dto.InfoAlunoDTO;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Endereco;
import com.athlos.smashback.model.Responsavel;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.EnderecoRepository;
import com.athlos.smashback.repository.MensalidadeRepository;
import com.athlos.smashback.repository.ResponsavelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class AlunoServiceTests {
    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ResponsavelRepository responsavelRepository;

    @Mock
    private AlunoComprovanteService alunoComprovanteService;

    @Mock
    private MensalidadeRepository mensalidadeRepository;

    @Mock
    private ValorMensalidadeService valorMensalidadeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        alunoService = new AlunoService(
                alunoRepository, enderecoRepository, responsavelRepository,
                alunoComprovanteService, mensalidadeRepository, valorMensalidadeService
        );
    }

    @Test
    void listarAlunosSuccess() {
        Aluno a1 = new Aluno();
        a1.setId(1);

        Aluno a2 = new Aluno();
        a2.setId(2);

        when(alunoRepository.findAll(any(Sort.class))).thenReturn(List.of(a1, a2));

        ResponseEntity<List<Aluno>> alunos = alunoService.listarAlunos();

        assertEquals(2, alunos.getBody().size());
    }

    @Test
    void listarAlunosEmpty() {
        when(alunoRepository.findAll(any(Sort.class))).thenReturn(List.of());

        ResponseEntity<List<Aluno>> alunos = alunoService.listarAlunos();

        assertTrue(alunos.getBody().isEmpty());
    }

    @Test
    void buscarAlunoPorIdSuccess() {
        Aluno a = new Aluno();
        a.setId(1);
        a.setNome("João");
        a.setEmail("");
        a.setDataNascimento(null);
        a.setCpf("");
        a.setRg("");
        a.setNomeSocial("");
        a.setGenero("");
        a.setCelular("");
        a.setNacionalidade("");
        a.setNaturalidade("");
        a.setTelefone("");
        a.setProfissao("");
        a.setAtivo(true);
        a.setTemAtestado(true);
        a.setDeficiencia("");
        a.setAutorizado(true);
        a.setDataInclusao(LocalDateTime.now());

        Endereco e = new Endereco();
        a.setEndereco(e);

        List<Responsavel> r = List.of();
        a.setResponsaveis(r);

        Usuario u = new Usuario();
        u.setId(1);
        a.setUsuarioInclusao(u);

        when(alunoRepository.existsById(1)).thenReturn(true);
        when(alunoRepository.findById(1)).thenReturn(Optional.of(a));

        ResponseEntity<InfoAlunoDTO> resp = alunoService.buscarAluno(1);

        assertEquals("João", resp.getBody().getNome());
    }

    @Test
    void buscarAlunoPorIdNotFound() {
        when(alunoRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            alunoService.buscarAluno(1);
        });

        assertEquals("Aluno não encontrado", exception.getMessage());
    }

    @Test
    void deletarAlunoSuccess() {
        Aluno a = new Aluno();
        a.setId(1);

        when(alunoRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> resp = alunoService.deletarAluno(1);

        assertEquals(204, resp.getStatusCodeValue());
        verify(alunoRepository, times(1)).deleteById(1);
    }

    @Test
    void deletarAlunoNotFound() {
        when(alunoRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            alunoService.deletarAluno(1);
        });

        assertEquals("Aluno não encontrado", exception.getMessage());
    }

    @Test
    void listarAniversariantesSuccess() {
        Aluno a1 = new Aluno();
        a1.setId(1);
        a1.setNome("Ana");
        a1.setDataNascimento(LocalDate.of(2005, 6, 1));

        Aluno a2 = new Aluno();
        a2.setId(2);
        a2.setNome("Bruno");
        a2.setDataNascimento(LocalDate.of(2005, 7, 1));

        Aluno a3 = new Aluno();
        a3.setId(3);
        a3.setNome("Carlos");
        a3.setDataNascimento(LocalDate.of(2005, 8, 1));

        when(alunoRepository.findAniversariantes()).thenReturn(List.of(a1, a2, a3));

        ResponseEntity<List<AlunoAniversarioDTO>> aniversariantes = alunoService.listarAniversarios();

        assertEquals(3, aniversariantes.getBody().size());
        assertEquals("Ana", aniversariantes.getBody().get(0).getNome());
        assertEquals("Bruno", aniversariantes.getBody().get(1).getNome());
        assertEquals("Carlos", aniversariantes.getBody().get(2).getNome());
    }

    @Test
    void qtdAlunosAtivos() {
        when(alunoRepository.countByAtivo(true)).thenReturn(10);

        ResponseEntity<Integer> resp = alunoService.qtdAlunosAtivos();

        assertEquals(10, resp.getBody());
    }
}
