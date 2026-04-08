package com.example.auth.controllers;


import com.example.auth.repositories.ArmaRepository;
import com.example.auth.repositories.ControleArmamentoRepository;
import com.example.auth.repositories.MilitarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ControleArmamentoRepository controleArmamentoRepository;
    private final ArmaRepository armaRepository;
    private final MilitarRepository militarRepository;

    @GetMapping("/cautelas-ativas")
    public long getTotalCautelasAtivas() {
        return controleArmamentoRepository.contarCautelasAtivas();
    }

    @GetMapping("/militares")
    public long getTotalMilitares() {
        return militarRepository.count();
    }

    @GetMapping("/armamentos")
    public long getTotalArmas() {
        return armaRepository.contarArmas();
    }

    @GetMapping("/armamentos-disponiveis")
    public long getTotalArmasDisponiveis() {
        return armaRepository.contarArmasDisponiveis();
    }
}
