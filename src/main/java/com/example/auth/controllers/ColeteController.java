package com.example.auth.controllers;


import com.example.auth.domain.dto.ColeteDTO;
import com.example.auth.services.ColeteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coletes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ColeteController {

    private final ColeteService coleteService;

    @GetMapping
    public List<ColeteDTO> listar() {
        return coleteService.listarTodos();
    }

    @PostMapping
    public ColeteDTO salvar(@RequestBody ColeteDTO dto) {
        return coleteService.salvar(dto);
    }

    @GetMapping("/{id}")
    public ColeteDTO buscarPorId(@PathVariable Long id) {
        return coleteService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        coleteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}