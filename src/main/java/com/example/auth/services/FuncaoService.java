package com.example.auth.services;


import com.example.auth.domain.Funcao;
import com.example.auth.repositories.FuncaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncaoService {

    @Autowired
    private FuncaoRepository funcaoRepository;

    public List<Funcao> listarTodos() {
        return funcaoRepository.findAll();
    }

    public Funcao salvar(Funcao funcao) {
        return funcaoRepository.save(funcao);
    }
}
