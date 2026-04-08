package com.example.auth.controllers;


import com.example.auth.domain.dto.ArmaDTO;
import com.example.auth.services.ArmaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ArmaController {

    private final ArmaService armaService;

    @GetMapping
    public List<ArmaDTO> listar() {
        return armaService.listarTodas();
    }

    @GetMapping("/disponiveis")
    public List<ArmaDTO> listarDisponiveis() {
        return armaService.listarDisponiveis();
    }


    @PostMapping
    public ArmaDTO salvar(@RequestBody ArmaDTO dto) {
        return armaService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ArmaDTO buscarPorId(@PathVariable Long id) {
        return armaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        armaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}