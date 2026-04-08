package com.example.auth.services;


import com.example.auth.domain.Arma;
import com.example.auth.domain.ControleArmamento;
import com.example.auth.domain.Militar;
import com.example.auth.domain.dto.ControleArmamentoCadastroDTO;
import com.example.auth.domain.dto.ControleArmamentoProcessoVisualizacaoDTO;
import com.example.auth.domain.dto.ControleArmamentoVisualizacaoDTO;
import com.example.auth.domain.dto.DetalhesCautelaDTO;
import com.example.auth.repositories.ArmaRepository;
import com.example.auth.repositories.ColeteRepository;
import com.example.auth.repositories.ControleArmamentoRepository;
import com.example.auth.repositories.MilitarRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ControleArmamentoService {

    private final ControleArmamentoRepository controleArmamentoRepository;
    private final MilitarRepository militarRepository;
    private final ArmaRepository armaRepository;
    private final ColeteRepository coleteRepository;

    public List<ControleArmamentoVisualizacaoDTO> listarTodos() {
        return controleArmamentoRepository.findAll()
                .stream()
                .map(c -> {
                    ControleArmamentoVisualizacaoDTO dto = new ControleArmamentoVisualizacaoDTO();
                    dto.setId(c.getId());

                    dto.setMilitarId(c.getMilitar() != null ? c.getMilitar().getId() : null);
                    dto.setMilitarNome(c.getMilitar() != null ? c.getMilitar().getNomeGuerra() : null);

                    dto.setArmaId(c.getArma() != null ? c.getArma().getId() : null);
                    dto.setArmaNumeroSerie(c.getArma() != null ? c.getArma().getNumeroSerie() : null);

                    dto.setColeteId(c.getColete() != null ? c.getColete().getId() : null);

                    dto.setQuantidadeMunicao(c.getQuantidadeMunicao());
                    dto.setDataEntrega(c.getDataEntrega());
                    dto.setDataDevolucao(c.getDataDevolucao());
                    dto.setObservacoes(c.getObservacoes());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ControleArmamentoVisualizacaoDTO salvar(ControleArmamentoCadastroDTO dto) {
        Militar militar = militarRepository.findById(dto.getMilitarId())
                .orElseThrow(() -> new RuntimeException("Militar não encontrado"));

        Arma arma = dto.getArmaId() != null
                ? armaRepository.findById(dto.getArmaId()).orElse(null)
                : null;

        if (arma != null && controleArmamentoRepository.existsByArmaIdAndDataDevolucaoIsNull(arma.getId())) {
            throw new RuntimeException("Esta arma já está em uso por outro militar.");
        }

        ControleArmamento entidade = new ControleArmamento();
        entidade.setMilitar(militar);
        entidade.setArma(arma);
        entidade.setQuantidadeMunicao(dto.getQuantidadeMunicao());
        entidade.setDataEntrega(dto.getDataEntrega());
        entidade.setObservacoes(dto.getObservacoes());

        ControleArmamento salvo = controleArmamentoRepository.save(entidade);

        ControleArmamentoVisualizacaoDTO visualizacaoDTO = new ControleArmamentoVisualizacaoDTO();
        visualizacaoDTO.setId(salvo.getId());
        visualizacaoDTO.setMilitarNome(militar.getNomeGuerra());
        visualizacaoDTO.setArmaNumeroSerie(arma != null ? arma.getNumeroSerie() : null);
        visualizacaoDTO.setQuantidadeMunicao(salvo.getQuantidadeMunicao());
        visualizacaoDTO.setDataEntrega(salvo.getDataEntrega());
        visualizacaoDTO.setObservacoes(salvo.getObservacoes());

        return visualizacaoDTO;
    }

    public ControleArmamentoVisualizacaoDTO buscarPorId(Long id) {
        ControleArmamento c = controleArmamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cautela não encontrada"));

        ControleArmamentoVisualizacaoDTO dto = new ControleArmamentoVisualizacaoDTO();
        dto.setId(c.getId());

        dto.setMilitarId(c.getMilitar() != null ? c.getMilitar().getId() : null);
        dto.setMilitarNome(c.getMilitar() != null ? c.getMilitar().getNomeGuerra() : null);

        dto.setArmaId(c.getArma() != null ? c.getArma().getId() : null);
        dto.setArmaNumeroSerie(c.getArma() != null ? c.getArma().getNumeroSerie() : null);

        dto.setColeteId(c.getColete() != null ? c.getColete().getId() : null);

        dto.setQuantidadeMunicao(c.getQuantidadeMunicao());
        dto.setDataEntrega(c.getDataEntrega());
        dto.setDataDevolucao(c.getDataDevolucao());
        dto.setObservacoes(c.getObservacoes());

        return dto;
    }

    public DetalhesCautelaDTO buscarDetalhesPorId(Long id) {
        ControleArmamento cautela = controleArmamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cautela não encontrada"));

        Militar militar = cautela.getMilitar();
        Arma arma = cautela.getArma();

        DetalhesCautelaDTO dto = new DetalhesCautelaDTO();

        // Militar
        dto.setPostoGraduacao(militar.getPostoGraduacao().getLabel());
        dto.setNomeCompleto(militar.getNomeCompleto());
        dto.setRgMilitar(militar.getRgMilitar());

        // Arma
        if (arma != null) {
            dto.setNumeroSerie(arma.getNumeroSerie());
            dto.setTipo(arma.getTipo());
            dto.setMarca(arma.getMarca());
            dto.setModelo(arma.getModelo());
            dto.setCalibre(arma.getCalibre());
            dto.setNumeroTiro(arma.getNumeroTiro());
            dto.setCano(arma.getCano());
        }

        // Cautela
        dto.setCreatedAt(cautela.getCreatedAt());

        return dto;
    }

    @Transactional
    public void darBaixaNaCautela(Long id) {
        ControleArmamento cautela = controleArmamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cautela não encontrada"));

        cautela.setDataDevolucao(LocalDate.now());
        controleArmamentoRepository.save(cautela);
    }

    public List<ControleArmamentoVisualizacaoDTO> listarNaoBaixadas() {
        return controleArmamentoRepository.findAllNaoBaixadas()
                .stream()
                .map(ControleArmamentoVisualizacaoDTO::fromEntity)
                .toList();
    }

    public ControleArmamentoProcessoVisualizacaoDTO getDadosParaRelatorio(Long id) {
        ControleArmamento entity = controleArmamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cautela não encontrada"));

        var militar = entity.getMilitar();
        var arma = entity.getArma();

        ControleArmamentoProcessoVisualizacaoDTO dto = new ControleArmamentoProcessoVisualizacaoDTO();

        // Dados do militar
        dto.setNomeCompleto(militar.getNomeCompleto());
        dto.setNomeGuerra(militar.getNomeGuerra());
        dto.setMatricula(militar.getMatricula());
        dto.setCpf(militar.getCpf());
        dto.setRgMilitar(militar.getRgMilitar());
        dto.setPostoGraduacao(militar.getPostoGraduacao() != null ? militar.getPostoGraduacao().name() : "N/A");
        dto.setQuadro(militar.getQuadro() != null ? militar.getQuadro().name() : "N/A");
        dto.setDataNascimento(militar.getDataNascimento() != null
                ? militar.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : null);
        dto.setCidade(militar.getCidade() != null ? militar.getCidade().getNome() : null);
        dto.setEstado(militar.getEstado() != null ? militar.getEstado().getNome() : null);
        dto.setEmail(militar.getEmail());
        dto.setTelefone(militar.getCelular());

        dto.setSubUnidade(
                militar.getSubUnidade() != null ? militar.getSubUnidade().getNome() : "Não informado"
        );

        // Endereço completo: preferencialmente o campo direto
        if (militar.getEnderecoCompleto() != null && !militar.getEnderecoCompleto().isBlank()) {
            dto.setEnderecoCompleto(militar.getEnderecoCompleto());
        } else {
            String enderecoFormatado = Stream.of(
                            militar.getLogradouro(),
                            militar.getNumeroCasa(),
                            militar.getBairro(),
                            militar.getCidade() != null ? militar.getCidade().getNome() : null,
                            militar.getEstado() != null ? militar.getEstado().getNome() : null
                    )
                    .map(Objects::toString)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(", "));


            dto.setEnderecoCompleto(enderecoFormatado);
        }

        dto.setIdentificacaoCompleta(
                (militar.getPostoGraduacao() != null ? militar.getPostoGraduacao().name() : "") + " " +
                        (militar.getQuadro() != null ? militar.getQuadro().name() : "") + " " +
                        militar.getNomeCompleto()
        );

        // Dados da arma
        dto.setEspecie(arma.getTipo());
        dto.setMarca(arma.getMarca());
        dto.setModelo(arma.getModelo());
        dto.setCalibre(arma.getCalibre());
        dto.setNumeroSerie(arma.getNumeroSerie());
        dto.setNumeroTiro(arma.getNumeroTiro());
        dto.setCano(arma.getCano());
        dto.setEstadoConservacao(arma.getEstadoConservacao());
        dto.setTombo(arma.getTombo());
        dto.setQuantidadeCarregadores(arma.getQtdeCarregador());

        // Dados da cautela
        dto.setQuantidadeMunicao(entity.getQuantidadeMunicao());
        dto.setDataEntrega(entity.getDataEntrega());
        dto.setObservacoes(entity.getObservacoes());

        return dto;
    }

}
