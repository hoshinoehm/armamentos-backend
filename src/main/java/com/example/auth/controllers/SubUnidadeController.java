package com.example.auth.controllers;


import com.example.auth.domain.SubUnidade;
import com.example.auth.services.SubUnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subunidades")
public class SubUnidadeController {

    @Autowired
    private SubUnidadeService subUnidadeService;

    @GetMapping
    public List<SubUnidade> listarTodos() {
        return subUnidadeService.listarTodos();
    }

    @PostMapping
    public SubUnidade salvar(@RequestBody SubUnidade subUnidade) {
        return subUnidadeService.salvar(subUnidade);
    }
}

