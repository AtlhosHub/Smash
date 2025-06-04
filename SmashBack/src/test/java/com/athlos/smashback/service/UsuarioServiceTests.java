package com.athlos.smashback.service;

import com.athlos.smashback.config.GerenciadorTokenJWT;
import com.athlos.smashback.dto.UsuarioInfoDTO;
import com.athlos.smashback.dto.UsuarioListaDTO;
import com.athlos.smashback.dto.UsuarioTokenDTO;
import com.athlos.smashback.exception.AuthenticationException;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.UsuarioFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.ListaEspera;
import com.athlos.smashback.model.Usuario;
import com.athlos.smashback.repository.UsuarioRepository;
import com.athlos.smashback.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private GerenciadorTokenJWT gerenciadorTokenJWT;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void autenticar_Success() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@email.com");
        usuario.setSenha("senha");

        Usuario usuarioFromDb = new Usuario();
        usuarioFromDb.setEmail("test@email.com");
        usuarioFromDb.setSenha("hashed");

        when(usuarioRepository.findByEmailIgnoreCase("test@email.com")).thenReturn(Optional.of(usuarioFromDb));
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(gerenciadorTokenJWT.generateToken(auth)).thenReturn("token");

        UsuarioTokenDTO dto = usuarioService.autenticar(usuario);

        assertNotNull(dto);
        assertEquals("test@email.com", dto.getEmail());
        assertEquals("token", dto.getToken());
    }

    @Test
    void autenticar_Fail_UserNotFound() {
        Usuario usuario = new Usuario();
        usuario.setEmail("notfound@email.com");
        usuario.setSenha("senha");

        when(usuarioRepository.findByEmailIgnoreCase("notfound@email.com")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> usuarioService.autenticar(usuario));
    }

    @Test
    void listarUsuarios_ReturnsList() {
        Usuario u1 = new Usuario();
        u1.setId(1);
        u1.setNome("Ana");
        u1.setNomeSocial(null);

        Usuario u2 = new Usuario();
        u2.setId(2);
        u2.setNome("Bruno");
        u2.setNomeSocial("B");

        Usuario u3 = new Usuario();
        u3.setId(3);
        u3.setNome("Carlos");
        u3.setNomeSocial("   ");

        Usuario u4 = new Usuario();
        u4.setId(4);
        u4.setNome("Daniel");
        u4.setNomeSocial("");

        when(usuarioRepository.findAll(any(Sort.class))).thenReturn(List.of(u1, u2, u3, u4));

        ResponseEntity<List<UsuarioListaDTO>> resp = usuarioService.listarUsuarios();

        assertEquals(4, resp.getBody().size());
        assertEquals("Ana", resp.getBody().get(0).getNome());
        assertEquals("B", resp.getBody().get(1).getNome());
        assertEquals("Carlos", resp.getBody().get(2).getNome());
        assertEquals("Daniel", resp.getBody().get(3).getNome());
    }

    @Test
    void listarUsuarios_Empty() {
        when(usuarioRepository.findAll(any(Sort.class))).thenReturn(List.of());

        ResponseEntity<List<UsuarioListaDTO>> resp = usuarioService.listarUsuarios();

        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void usuarioFiltro_ReturnsFiltered() {
        UsuarioFilter filtro = new UsuarioFilter();
        Usuario u = new Usuario();
        u.setId(1);
        u.setNome("Carlos");
        u.setNomeSocial(null);

        Usuario u2 = new Usuario();
        u2.setId(2);
        u2.setNome("Bruno");
        u2.setNomeSocial("B");

        Usuario u3 = new Usuario();
        u3.setId(3);
        u3.setNome("Ana");
        u3.setNomeSocial("   ");

        Usuario u4 = new Usuario();
        u4.setId(4);
        u4.setNome("Daniel");
        u4.setNomeSocial("");

        when(usuarioRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of(u, u2, u3, u4));

        ResponseEntity<List<UsuarioListaDTO>> resp = usuarioService.usuarioFiltro(filtro);

        assertEquals(4, resp.getBody().size());
        assertEquals("Carlos", resp.getBody().get(0).getNome());
        assertEquals("B", resp.getBody().get(1).getNome());
        assertEquals("Ana", resp.getBody().get(2).getNome());
        assertEquals("Daniel", resp.getBody().get(3).getNome());
    }

    @Test
    void usuarioFiltro_Empty() {
        UsuarioFilter filtro = new UsuarioFilter();
        when(usuarioRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        ResponseEntity<List<UsuarioListaDTO>> resp = usuarioService.usuarioFiltro(filtro);

        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void buscarUsuarioPorId_Success() {
        Usuario u = new Usuario();
        u.setId(1);
        u.setNome("Ana");
        u.setEmail("ana@email.com");
        u.setCelular("123");
        u.setDataNascimento(LocalDate.of(2000,1,1));
        u.setNomeSocial("Ana");
        u.setGenero("F");
        u.setTelefone("321");
        u.setCargo("Admin");

        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        ResponseEntity<UsuarioInfoDTO> resp = usuarioService.buscarUsuarioPorId(1);

        assertEquals("Ana", resp.getBody().getNome());
        assertEquals("ana@email.com", resp.getBody().getEmail());
    }

    @Test
    void buscarUsuarioPorId_NotFound() {
        when(usuarioRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscarUsuarioPorId(2));
    }

    @Test
    void adicionarUsuario_Success() {
        Usuario u = new Usuario();
        u.setEmail("novo@email.com");
        u.setSenha("senha");
        u.setNome("Novo");
        u.setCelular("123");
        u.setDataNascimento(LocalDate.of(2000,1,1));
        u.setNomeSocial("Novo");
        u.setGenero("M");
        u.setTelefone("321");
        u.setCargo("User");

        when(usuarioRepository.existsByEmailIgnoreCase("novo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("hashed");

        ResponseEntity<UsuarioInfoDTO> resp = usuarioService.adicionarUsuario(u);

        verify(usuarioRepository).save(u);
        assertEquals("Novo", resp.getBody().getNome());
        assertEquals("novo@email.com", resp.getBody().getEmail());
    }

    @Test
    void adicionarUsuario_EmailConflict() {
        Usuario u = new Usuario();
        u.setEmail("existe@email.com");

        when(usuarioRepository.existsByEmailIgnoreCase("existe@email.com")).thenReturn(true);

        assertThrows(DataConflictException.class, () -> usuarioService.adicionarUsuario(u));
    }

    @Test
    void removerUsuario_Success() {
        Usuario u = new Usuario();
        u.setId(1);

        Aluno aluno = new Aluno();
        ListaEspera interessado = new ListaEspera();
        Usuario cadastrado = new Usuario();

        u.setAlunos(List.of(aluno));
        u.setInteressados(List.of(interessado));
        u.setUsuariosCadastrados(List.of(cadastrado));

        when(usuarioRepository.existsById(1)).thenReturn(true);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(u));

        ResponseEntity<Void> resp = usuarioService.removerUsuario(1);

        assertNull(aluno.getUsuarioInclusao());
        assertNull(interessado.getUsuarioInclusao());
        assertNull(cadastrado.getUsuarioInclusao());
        verify(usuarioRepository).deleteById(1);
        assertEquals(204, resp.getStatusCodeValue());
    }

    @Test
    void removerUsuario_NotFound() {
        when(usuarioRepository.existsById(2)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.removerUsuario(2));
    }

    @Test
    void atualizarUsuario_Success() {
        Usuario novo = new Usuario();
        novo.setNome("Novo");
        novo.setEmail("novo@email.com");
        novo.setCelular("123");
        novo.setDataNascimento(LocalDate.of(2000,1,1));
        novo.setCargo("User");
        novo.setDeletado(false);
        novo.setNomeSocial("Novo");
        novo.setGenero("M");
        novo.setTelefone("321");

        Usuario existente = new Usuario();
        existente.setId(1);

        when(usuarioRepository.existsByEmailIgnoreCaseAndIdIsNot("novo@email.com", 1)).thenReturn(false);
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(existente));

        ResponseEntity<UsuarioInfoDTO> resp = usuarioService.atualizarUsuario(1, novo);

        verify(usuarioRepository).save(existente);
        assertEquals("Novo", resp.getBody().getNome());
        assertEquals("novo@email.com", resp.getBody().getEmail());
    }

    @Test
    void atualizarUsuario_EmailConflict() {
        Usuario novo = new Usuario();
        novo.setEmail("conflict@email.com");

        when(usuarioRepository.existsByEmailIgnoreCaseAndIdIsNot("conflict@email.com", 1)).thenReturn(true);

        assertThrows(DataConflictException.class, () -> usuarioService.atualizarUsuario(1, novo));
    }

    @Test
    void atualizarUsuario_NotFound() {
        Usuario novo = new Usuario();
        novo.setEmail("notfound@email.com");

        when(usuarioRepository.existsByEmailIgnoreCaseAndIdIsNot("notfound@email.com", 1)).thenReturn(false);
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usuarioService.atualizarUsuario(1, novo));
    }
}