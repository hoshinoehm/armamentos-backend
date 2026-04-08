package com.example.auth.services;


import com.example.auth.domain.SubUnidade;
import com.example.auth.repositories.SubUnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubUnidadeService {

    @Autowired
    private SubUnidadeRepository subUnidadeRepository;

    public List<SubUnidade> listarTodos() {
        return subUnidadeRepository.findAll();
    }

    public SubUnidade salvar(SubUnidade subUnidade) {
        return subUnidadeRepository.save(subUnidade);
    }
}