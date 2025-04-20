package com.athlos.smashback.controller;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.service.AlunoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoService alunoService;
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<Aluno>> listarAlunos(){
        return alunoService.listarAlunos();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable int id){
        return alunoService.buscarAluno(id);
    }

    @PostMapping("/comprovantes")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes(@RequestBody AlunoFilter filtro) {
        return alunoService.listarAlunosComComprovantes(filtro);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Aluno> cadastrarAluno(@Valid @RequestBody Aluno aluno){
        return alunoService.cadastrarAluno(aluno);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> deletarAluno(@PathVariable int id){
        return alunoService.deletarAluno(id);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id, @Valid @RequestBody Aluno novoAluno){
        return alunoService.atualizarAluno(id, novoAluno);
    }
}