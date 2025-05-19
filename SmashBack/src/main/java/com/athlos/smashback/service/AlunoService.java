package com.athlos.smashback.service;

import com.athlos.smashback.dto.AlunoAniversarioDTO;
import com.athlos.smashback.dto.AlunoComprovanteDTO;
import com.athlos.smashback.exception.DataConflictException;
import com.athlos.smashback.exception.ResourceNotFoundException;
import com.athlos.smashback.filter.AlunoFilter;
import com.athlos.smashback.model.*;
import com.athlos.smashback.model.enums.Status;
import com.athlos.smashback.repository.*;
import com.athlos.smashback.service.AlunoComprovanteService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
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
    private final ValorMensalidadeService valorMensalidadeService;
    private static final int NUMERO_PARCELAS = 6;

    public AlunoService(AlunoRepository alunoRepository, EnderecoRepository enderecoRepository, ResponsavelRepository responsavelRepository, AlunoComprovanteService alunoComprovanteService, MensalidadeRepository mensalidadeRepository, ValorMensalidadeService valorMensalidadeService) {
        this.alunoRepository = alunoRepository;
        this.enderecoRepository = enderecoRepository;
        this.responsavelRepository = responsavelRepository;
        this.alunoComprovanteService = alunoComprovanteService;
        this.mensalidadeRepository = mensalidadeRepository;
        this.valorMensalidadeService = valorMensalidadeService;
    }

    public ResponseEntity<List<Aluno>> listarAlunos() {
        List<Aluno> alunos = alunoRepository.findAll(Sort.by(Sort.Order.asc("nome").ignoreCase()));
        return ResponseEntity.ok(alunos.isEmpty() ? List.of() : alunos);
    }

    public ResponseEntity<Aluno> buscarAluno(int id) {
        if(!alunoRepository.existsById(id)){
            throw new ResourceNotFoundException("Aluno não encontrado");
        }

        return ResponseEntity.ok(alunoRepository.findById(id).get());
    }

    public ResponseEntity<List<AlunoComprovanteDTO>> listarAlunosComComprovantes(AlunoFilter filtro) {
        List<AlunoComprovanteDTO> lista = alunoComprovanteService.listarAlunosComComprovantes(filtro);
        return ResponseEntity.ok(lista);
    }


    @Transactional
    public ResponseEntity<Aluno> cadastrarAluno(Aluno aluno) {
        if(!aluno.isMenor()){
            if (alunoRepository.existsByEmailIgnoreCaseOrCpfOrRg(aluno.getEmail(), aluno.getCpf(), aluno.getRg())) {
                throw new DataConflictException("E-mail, RG ou CPF já cadastrados");
            }
        }else{
            if (alunoRepository.existsByCpfOrRg(aluno.getCpf(),aluno.getRg())){
                throw new DataConflictException("RG ou CPF já cadastrados");
            }
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
        // Verifica conflitos de dados únicos (e-mail, CPF, RG)
        if ((!novoAluno.isMenor() && alunoRepository.existsByEmailIgnoreCaseAndIdIsNot(novoAluno.getEmail(), id)) ||
                alunoRepository.existsByCpfAndIdIsNot(novoAluno.getCpf(), id) ||
                alunoRepository.existsByRgAndIdIsNot(novoAluno.getRg(), id)) {
            throw new DataConflictException("E-mail, RG ou CPF já cadastrados");
        }

        // Busca o aluno existente e atualiza os dados
        return alunoRepository.findById(id).map(aluno -> {
            // Atualiza campos básicos
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

            // Atualiza ou reutiliza endereço existente
            Endereco novoEndereco = novoAluno.getEndereco();
            if (novoEndereco != null) {
                Optional<Endereco> enderecoExistente = enderecoRepository
                        .findByLogradouroAndNumLogradouroAndBairroAndCidadeAndCepAndEstado(
                                novoEndereco.getLogradouro(),
                                novoEndereco.getNumLogradouro(),
                                novoEndereco.getBairro(),
                                novoEndereco.getCidade(),
                                novoEndereco.getCep(),
                                novoEndereco.getEstado()
                        );
                aluno.setEndereco(enderecoExistente.orElseGet(() -> enderecoRepository.save(novoEndereco)));
            }

            // Atualiza responsáveis (caso o aluno seja menor de idade)
            if (novoAluno.isMenor()) {
                List<Responsavel> responsaveisAtualizados = Optional.ofNullable(novoAluno.getResponsaveis())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(responsavelNovo ->
                                responsavelRepository.findByCpf(responsavelNovo.getCpf())
                                        .map(responsavelExistente -> {
                                            responsavelNovo.setId(responsavelExistente.getId()); // Garante update
                                            return responsavelRepository.save(responsavelNovo);
                                        })
                                        .orElseGet(() -> responsavelRepository.save(responsavelNovo))
                        )
                        .collect(Collectors.toList());

                aluno.setResponsaveis(responsaveisAtualizados);
            } else {
                aluno.setResponsaveis(Collections.emptyList()); // Ou mantenha os atuais, se preferir
            }

            // Salva e retorna o aluno atualizado
            return ResponseEntity.ok(alunoRepository.save(aluno));
        }).orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }


    public void gerarMensalidades(Aluno aluno) {
        LocalDate dataBase = aluno.getDataInclusao().toLocalDate().withDayOfMonth(5);
        LocalDate hoje = LocalDate.now();
        ValorMensalidade valor = valorMensalidadeService.buscarValorMensalidadeAtual();

        for (int i = 0; i < NUMERO_PARCELAS; i++) {
            Mensalidade mensalidade = new Mensalidade();
            mensalidade.setAluno(aluno);
            LocalDate dataVencimento = dataBase.plusMonths(i);
            mensalidade.setDataVencimento(dataVencimento);
            mensalidade.setValor(valor);

            mensalidade.setStatus(dataVencimento.isBefore(hoje)
                    ? Status.ATRASADO
                    : Status.PENDENTE);

            mensalidadeRepository.save(mensalidade);
        }
    }

    public ResponseEntity<List<AlunoAniversarioDTO>> listarAniversarios() {
        int mesAtual = LocalDate.now().getMonthValue();
        List<Aluno> alunos = alunoRepository.findAniversariantes().stream()
                .filter(a -> {
                    int mesNascimento = a.getDataNascimento().getMonthValue();
                    return mesNascimento >= mesAtual;
                })
                .collect(Collectors.toList());

        List<AlunoAniversarioDTO> aniversariantes = alunos.stream()
                .map(aluno -> new AlunoAniversarioDTO(aluno.getNome(), aluno.getDataNascimento()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(aniversariantes.isEmpty() ? List.of() : aniversariantes);
    }

    public ResponseEntity<Integer> qtdAlunosAtivos() {
        int qtdAlunosAtivos = alunoRepository.countByAtivo(true);
        return ResponseEntity.ok(qtdAlunosAtivos);
    }
}
