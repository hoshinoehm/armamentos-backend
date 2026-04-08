package com.example.auth.controllers;


import com.example.auth.domain.Funcao;
import com.example.auth.services.FuncaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcoes")
public class FuncaoController {

    @Autowired
    private FuncaoService funcaoService;

    @GetMapping
    public List<Funcao> listarTodos() {
        return funcaoService.listarTodos();
    }

    @PostMapping
    public Funcao salvar(@RequestBody Funcao funcao) {
        return funcaoService.salvar(funcao);
    }
}
