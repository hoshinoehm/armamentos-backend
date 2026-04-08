package com.example.auth.controllers;


import com.example.auth.domain.Militar;
import com.example.auth.domain.dto.MilitarDTO;
import com.example.auth.domain.enums.PostoGraduacao;
import com.example.auth.services.MilitarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/militares")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MilitarController {

    private final MilitarService militarService;

    @GetMapping
    public ResponseEntity<List<MilitarDTO>> listarTodos() {
        List<Militar> militares = militarService.listarTodos();
        List<MilitarDTO> militaresDTO = militares.stream()
                .map(MilitarDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(militaresDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilitarDTO> buscarPorId(@PathVariable Long id) {
        Militar militar = militarService.buscarPorId(id);
        return ResponseEntity.ok(MilitarDTO.fromEntity(militar));
    }

    @PostMapping
    public ResponseEntity<MilitarDTO> salvar(@Valid @RequestBody MilitarDTO militarDTO) {
        Militar militar = militarDTO.toEntity();
        Militar salvo = militarService.salvar(militar);
        return ResponseEntity.ok(MilitarDTO.fromEntity(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MilitarDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MilitarDTO militarDTO) {
        Militar militarAtualizado = militarDTO.toEntity();
        Militar atualizado = militarService.atualizar(id, militarAtualizado);
        return ResponseEntity.ok(MilitarDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        militarService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posto-graduacoes")
    public ResponseEntity<List<Map<String, String>>> listarPostosGraduacao() {
        List<Map<String, String>> lista = Arrays.stream(PostoGraduacao.values())
                .map(p -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("value", p.name());        // Ex: "CAPITAO"
                    map.put("label", p.getLabel());    // Ex: "Capitão"
                    return map;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }


}
