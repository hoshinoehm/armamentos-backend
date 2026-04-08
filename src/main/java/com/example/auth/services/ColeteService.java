package com.example.auth.services;


import com.example.auth.domain.Colete;
import com.example.auth.domain.dto.ColeteDTO;
import com.example.auth.repositories.ColeteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColeteService {

    private final ColeteRepository coleteRepository;
    private final ModelMapper modelMapper;

    public List<ColeteDTO> listarTodos() {
        return coleteRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, ColeteDTO.class))
                .collect(Collectors.toList());
    }

    public ColeteDTO salvar(ColeteDTO dto) {
        Colete colete = modelMapper.map(dto, Colete.class);
        return modelMapper.map(coleteRepository.save(colete), ColeteDTO.class);
    }

    public ColeteDTO buscarPorId(Long id) {
        Colete colete = coleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colete não encontrado"));
        return modelMapper.map(colete, ColeteDTO.class);
    }

    public void deletar(Long id) {
        coleteRepository.deleteById(id);
    }
}