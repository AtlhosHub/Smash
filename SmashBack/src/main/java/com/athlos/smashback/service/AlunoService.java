package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Endereco;
import com.athlos.smashback.model.Responsavel;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.EnderecoRepository;
import com.athlos.smashback.repository.ResponsavelRepository;
import com.athlos.smashback.service.AlunoComprovanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final EnderecoRepository enderecoRepository;
    private final ResponsavelRepository responsavelRepository;
    private final AlunoComprovanteService alunoComprovanteService;

    public AlunoService(AlunoRepository alunoRepository, EnderecoRepository enderecoRepository, ResponsavelRepository responsavelRepository, AlunoComprovanteService alunoComprovanteService) {
        this.alunoRepository = alunoRepository;
        this.enderecoRepository = enderecoRepository;
        this.responsavelRepository = responsavelRepository;
        this.alunoComprovanteService = alunoComprovanteService;
    }

    public ResponseEntity<List<Aluno>> listarAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(alunos);
    }

    public ResponseEntity<Aluno> buscarAluno(int id) {
        return alunoRepository.existsById(id) ? ResponseEntity.ok(alunoRepository.findById(id).get()) : ResponseEntity.notFound().build();
    }

    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes() {
        return ResponseEntity.ok(alunoComprovanteService.buscarAlunosComComprovantes());
    }

    public ResponseEntity<Aluno> cadastrarAluno(Aluno aluno) {
        if (alunoRepository.existsByEmailOrCpfOrRg(aluno.getEmail(), aluno.getCpf(), aluno.getRg())) {
            return ResponseEntity.status(409).body(aluno);
        }

        Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCep(
                aluno.getEndereco().getLogradouro(),
                aluno.getEndereco().getNumLogradouro(),
                aluno.getEndereco().getBairro(),
                aluno.getEndereco().getCidade(),
                aluno.getEndereco().getCep()
        );
        aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(aluno.getEndereco())));

        if (aluno.isMenor()) {
            List<Responsavel> responsaveis = aluno.getResponsaveis().stream()
                    .map(responsavel -> responsavelRepository.findByCpf(responsavel.getCpf())
                            .orElseGet(() -> responsavelRepository.save(responsavel)))
                    .collect(Collectors.toList());
            aluno.setResponsaveis(responsaveis);
        }

        return ResponseEntity.ok(alunoRepository.save(aluno));
    }

    public ResponseEntity<Void> deletarAluno(int id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Aluno> atualizarAluno(int id, Aluno novoAluno) {
        if (alunoRepository.existsByEmailAndIdIsNot(novoAluno.getEmail(), id) ||
                alunoRepository.existsByCpfAndIdIsNot(novoAluno.getCpf(), id) ||
                alunoRepository.existsByRgAndIdIsNot(novoAluno.getRg(), id)) {
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

            Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCep(
                    novoAluno.getEndereco().getLogradouro(),
                    novoAluno.getEndereco().getNumLogradouro(),
                    novoAluno.getEndereco().getBairro(),
                    novoAluno.getEndereco().getCidade(),
                    novoAluno.getEndereco().getCep()
            );
            aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(novoAluno.getEndereco())));

            if (novoAluno.isMenor()) {
                List<Responsavel> responsaveis = novoAluno.getResponsaveis().stream()
                        .map(responsavel -> responsavelRepository.findByCpf(responsavel.getCpf())
                                .orElseGet(() -> responsavelRepository.save(responsavel)))
                        .collect(Collectors.toList());
                aluno.setResponsaveis(responsaveis);
            }

            return ResponseEntity.ok(alunoRepository.save(aluno));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
