package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Endereco;
import com.athlos.smashback.model.Mensalidade;
import com.athlos.smashback.model.Responsavel;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.AlunoRepository;
import com.athlos.smashback.repository.EnderecoRepository;
import com.athlos.smashback.repository.MensalidadeRepository;
import com.athlos.smashback.repository.ResponsavelRepository;
import com.athlos.smashback.service.AlunoComprovanteService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final EnderecoRepository enderecoRepository;
    private final ResponsavelRepository responsavelRepository;
    private final AlunoComprovanteService alunoComprovanteService;
    private final MensalidadeRepository mensalidadeRepository;
    private static final int NUMERO_PARCELAS = 6;

    public AlunoService(AlunoRepository alunoRepository, EnderecoRepository enderecoRepository, ResponsavelRepository responsavelRepository, AlunoComprovanteService alunoComprovanteService, MensalidadeRepository mensalidadeRepository) {
        this.alunoRepository = alunoRepository;
        this.enderecoRepository = enderecoRepository;
        this.responsavelRepository = responsavelRepository;
        this.alunoComprovanteService = alunoComprovanteService;
        this.mensalidadeRepository = mensalidadeRepository;
    }

    public ResponseEntity<List<Aluno>> listarAlunos() {
        List<Aluno> alunos = alunoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
        return alunos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(alunos);
    }

    public ResponseEntity<Aluno> buscarAluno(int id) {
        if(!alunoRepository.existsById(id)){
            throw new ResourceNotFoundException("Aluno não encontrado");
        }

        return ResponseEntity.ok(alunoRepository.findById(id).get());
    }

    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes(AlunoFilter filtro) {
        List<AlunoComprovanteDTO> lista = alunoComprovanteService.listarAlunosComComprovantes(filtro);
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }


    @Transactional
    public ResponseEntity<Aluno> cadastrarAluno(Aluno aluno) {
        if (alunoRepository.existsByEmailOrCpfOrRg(aluno.getEmail(), aluno.getCpf(), aluno.getRg())) {
            throw new DataConflictException("E-mail, RG ou CPF já cadastrados");
        }

        Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCepAndEstado(
                aluno.getEndereco().getLogradouro(),
                aluno.getEndereco().getNumLogradouro(),
                aluno.getEndereco().getBairro(),
                aluno.getEndereco().getCidade(),
                aluno.getEndereco().getCep(),
                aluno.getEndereco().getEstado()
        );
        aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(aluno.getEndereco())));

        if (aluno.isMenor()) {
            List<Responsavel> responsaveis = aluno.getResponsaveis().stream()
                    .map(responsavel -> responsavelRepository.findByCpf(responsavel.getCpf())
                            .orElseGet(() -> responsavelRepository.save(responsavel)))
                    .collect(Collectors.toList());
            aluno.setResponsaveis(responsaveis);
        }
        Aluno alunoSalvo = alunoRepository.save(aluno);
        gerarMensalidades(alunoSalvo);
        return ResponseEntity.ok(alunoSalvo);
    }

    public ResponseEntity<Void> deletarAluno(int id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("Aluno não encontrado");
    }

    public ResponseEntity<Aluno> atualizarAluno(int id, Aluno novoAluno) {
        if (alunoRepository.existsByEmailAndIdIsNot(novoAluno.getEmail(), id) ||
                alunoRepository.existsByCpfAndIdIsNot(novoAluno.getCpf(), id) ||
                alunoRepository.existsByRgAndIdIsNot(novoAluno.getRg(), id)) {
            throw new DataConflictException("E-mail, RG ou CPF já cadastrados");
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
            aluno.setAutorizado(novoAluno.isAutorizado());
            aluno.setNomeSocial(novoAluno.getNomeSocial());
            aluno.setGenero(novoAluno.getGenero());
            aluno.setDeficiencia(novoAluno.getDeficiencia());

            Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCepAndEstado(
                    novoAluno.getEndereco().getLogradouro(),
                    novoAluno.getEndereco().getNumLogradouro(),
                    novoAluno.getEndereco().getBairro(),
                    novoAluno.getEndereco().getCidade(),
                    novoAluno.getEndereco().getCep(),
                    novoAluno.getEndereco().getEstado()
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
        }).orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }

    public void gerarMensalidades(Aluno aluno) {
        LocalDate dataBase = aluno.getDataInclusao().toLocalDate().withDayOfMonth(5);
        LocalDate hoje = LocalDate.now();
        final double VALOR_PADRAO = 412.54; //por enquanto estou definindo o valor aqui

        for (int i = 0; i < NUMERO_PARCELAS; i++) {
            Mensalidade mensalidade = new Mensalidade();
            mensalidade.setAluno(aluno);
            LocalDate dataVencimento = dataBase.plusMonths(i);
            mensalidade.setDataVencimento(dataVencimento);
            mensalidade.setValor(VALOR_PADRAO);

            mensalidade.setStatus(dataVencimento.isBefore(hoje)
                    ? Status.ATRASADO
                    : Status.PENDENTE);

            mensalidadeRepository.save(mensalidade);
        }
    }
}
