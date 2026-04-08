package com.example.auth.services;


import com.example.auth.domain.Arma;
import com.example.auth.domain.dto.ArmaDTO;
import com.example.auth.repositories.ArmaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArmaService {

    private final ArmaRepository armaRepository;
    private final ModelMapper modelMapper;

    public List<ArmaDTO> listarTodas() {
        return armaRepository.findAll()
                .stream()
                .map(arma -> modelMapper.map(arma, ArmaDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ArmaDTO salvar(ArmaDTO dto) {
        // Verifica se número de série é nulo ou vazio
        if (dto.getNumeroSerie() == null || dto.getNumeroSerie().isBlank()) {
            throw new RuntimeException("O número de série da arma é obrigatório.");
        }

        // Verifica se já existe arma com mesmo número de série
        Optional<Arma> existente = armaRepository.findByNumeroSerie(dto.getNumeroSerie());
        if (existente.isPresent()) {
            throw new RuntimeException("Já existe uma arma com esse número de série.");
        }

        // Outras validações simples (pode expandir conforme a regra de negócio)
        if (dto.getCalibre() == null || dto.getCalibre().isBlank()) {
            throw new RuntimeException("O calibre da arma é obrigatório.");
        }

        Arma arma = modelMapper.map(dto, Arma.class);
        Arma salva = armaRepository.save(arma);
        return modelMapper.map(salva, ArmaDTO.class);
    }

    public ArmaDTO buscarPorId(Long id) {
        Arma arma = armaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arma não encontrada"));
        return modelMapper.map(arma, ArmaDTO.class);
    }

    public void deletar(Long id) {
        armaRepository.deleteById(id);
    }

    public List<ArmaDTO> listarDisponiveis() {
        List<Arma> armasDisponiveis = armaRepository.findArmasDisponiveis();
        return armasDisponiveis.stream().map(this::toDTO).toList();
    }

    private ArmaDTO toDTO(Arma arma) {
        return new ArmaDTO(
                arma.getId(),
                arma.getNumeroSerie(),
                arma.getTipo(),
                arma.getMarca(),
                arma.getModelo(),
                arma.getCalibre(),
                arma.getAcabamento(),
                arma.getNumeroTiro(),
                arma.getCano(),
                arma.getNumeroPMMA(),
                arma.getTombo(),
                arma.getEstadoConservacao(),
                arma.getQtdeCarregador(),
                arma.getMaleta(),
                arma.getAcessorios(),
                arma.getProcedencia(),
                arma.getObs()
        );
    }

}
