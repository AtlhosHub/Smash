package com.exemplo.smashback.controller;

import com.exemplo.smashback.dto.AlunoComprovanteDTO;
import com.exemplo.smashback.model.Aluno;
import com.exemplo.smashback.model.Endereco;
import com.exemplo.smashback.model.Responsavel;
import com.exemplo.smashback.repository.AlunoRepository;
import com.exemplo.smashback.repository.EnderecoRepository;
import com.exemplo.smashback.repository.ResponsavelRepository;
import com.exemplo.smashback.service.AlunoComprovanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    private final AlunoRepository alunoRepository;
    private final EnderecoRepository enderecoRepository;
    private final ResponsavelRepository responsavelRepository;
    private final AlunoComprovanteService alunoComprovanteService;
    public AlunoController(AlunoRepository alunoRepository, EnderecoRepository enderecoRepository, ResponsavelRepository responsavelRepository, AlunoComprovanteService alunoComprovanteService) {
        this.alunoRepository = alunoRepository;
        this.enderecoRepository = enderecoRepository;
        this.responsavelRepository = responsavelRepository;
        this.alunoComprovanteService = alunoComprovanteService;
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listarAlunos(){
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAluno(@PathVariable int id){
        return alunoRepository.existsById(id) ? ResponseEntity.ok(alunoRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    @GetMapping("/comprovantes")
    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes() {
        return ResponseEntity.ok(alunoComprovanteService.buscarAlunosComComprovantes());
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrarAluno(@RequestBody Aluno aluno){
        if(alunoRepository.existsByEmailOrCpfOrRg(aluno.getEmail(), aluno.getCpf(), aluno.getRg())) {
            return ResponseEntity.status(409).body(aluno);
        }

        if(aluno.getEndereco() != null){
            Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCep(
                    aluno.getEndereco().getLogradouro(),
                    aluno.getEndereco().getNumLogradouro(),
                    aluno.getEndereco().getBairro(),
                    aluno.getEndereco().getCidade(),
                    aluno.getEndereco().getCep()
            );
            aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(aluno.getEndereco())));
        } else {
            return ResponseEntity.badRequest().body(aluno);
        }

        if (aluno.isMenor()) {
            if (aluno.getResponsaveis() != null && !aluno.getResponsaveis().isEmpty()) {
                List<Responsavel> responsaveis = aluno.getResponsaveis().stream()
                .map(responsavel -> responsavelRepository.findByCpf(responsavel.getCpf())
                .orElseGet(() -> responsavelRepository.save(responsavel)))
                .collect(Collectors.toList());

                aluno.setResponsaveis(responsaveis);
            } else {
                return ResponseEntity.badRequest().body(aluno);
            }
        }

        return ResponseEntity.ok(alunoRepository.save(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable int id){
        if(alunoRepository.existsById(id)){
            alunoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable int id, @RequestBody Aluno novoAluno){
        if(
                alunoRepository.existsByEmailAndIdIsNot(novoAluno.getEmail(), id) ||
                alunoRepository.existsByCpfAndIdIsNot(novoAluno.getCpf(), id) ||
                alunoRepository.existsByRgAndIdIsNot(novoAluno.getRg(), id)
        ){
            return ResponseEntity.status(409).body(novoAluno);
        }
        return alunoRepository.findById(id).map(aluno -> {
            aluno.setNome(novoAluno.getNome());
            aluno.setEmail(novoAluno.getEmail());
            aluno.setNacionalidade(novoAluno.getNacionalidade());
            aluno.setNaturalidade(novoAluno.getNaturalidade());
            aluno.setDataNascimento(novoAluno.getDataNascimento());
            aluno.setTelefone(novoAluno.getTelefone());
            aluno.setCelular(novoAluno.getCelular());
            aluno.setProfissao(novoAluno.getProfissao());
            aluno.setRg(novoAluno.getRg());
            aluno.setCpf(novoAluno.getCpf());
            aluno.setAtivo(novoAluno.isAtivo());
            aluno.setTemAtestado(novoAluno.isTemAtestado());
            aluno.setTemAssinatura(novoAluno.isTemAssinatura());
            aluno.setAutorizado(novoAluno.isAutorizado());

            if (novoAluno.getEndereco() != null) {
                Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCep(
                        novoAluno.getEndereco().getLogradouro(),
                        novoAluno.getEndereco().getNumLogradouro(),
                        novoAluno.getEndereco().getBairro(),
                        novoAluno.getEndereco().getCidade(),
                        novoAluno.getEndereco().getCep()
                );
                aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(novoAluno.getEndereco())));
            } else {
                return ResponseEntity.badRequest().body(aluno);
            }

            if (novoAluno.isMenor()) {
                if (novoAluno.getResponsaveis() != null && !novoAluno.getResponsaveis().isEmpty()) {
                    List<Responsavel> responsaveis = novoAluno.getResponsaveis().stream()
                    .map(responsavel -> responsavelRepository.findByCpf(responsavel.getCpf())
                    .orElseGet(() -> responsavelRepository.save(responsavel)))
                    .collect(Collectors.toList());

                    aluno.setResponsaveis(responsaveis);
                } else {
                    return ResponseEntity.badRequest().body(aluno);
                }
            }
            return ResponseEntity.ok(alunoRepository.save(aluno));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}