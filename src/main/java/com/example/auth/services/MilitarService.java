package com.example.auth.services;


import com.example.auth.domain.Militar;
import com.example.auth.repositories.FuncaoRepository;
import com.example.auth.repositories.MilitarRepository;
import com.example.auth.repositories.SubUnidadeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MilitarService {

    private final MilitarRepository militarRepository;
    private final FuncaoRepository funcaoRepository;
    private final SubUnidadeRepository subUnidadeRepository;

    public List<Militar> listarTodos() {
        return militarRepository.findAll();
    }

    public Militar buscarPorId(Long id) {
        return militarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Militar não encontrado"));
    }

    public Militar salvar(Militar militar) {
        return militarRepository.save(militar);
    }

    public Militar atualizar(Long id, Militar militarAtualizado) {
        Militar militarExistente = buscarPorId(id);

        // Apenas campos permitidos para edição
        militarExistente.setNomeCompleto(militarAtualizado.getNomeCompleto());
        militarExistente.setNomeGuerra(militarAtualizado.getNomeGuerra());
        militarExistente.setCelular(militarAtualizado.getCelular());
        militarExistente.setCidade(militarAtualizado.getCidade());
        militarExistente.setEstado(militarAtualizado.getEstado());
        militarExistente.setSubUnidade(militarAtualizado.getSubUnidade());
        militarExistente.setSituacao(militarAtualizado.getSituacao());
        militarExistente.setPostoGraduacao(militarAtualizado.getPostoGraduacao());
        militarExistente.setQuadro(militarAtualizado.getQuadro());
        militarExistente.setNumeroBarra(militarAtualizado.getNumeroBarra());
        militarExistente.setDataInclusao(militarAtualizado.getDataInclusao());
        militarExistente.setDataNascimento(militarAtualizado.getDataNascimento());
        militarExistente.setFuncao(militarAtualizado.getFuncao());
        militarExistente.setServe31bpm(militarAtualizado.getServe31bpm());
        militarExistente.setSexo(militarAtualizado.getSexo());
        militarExistente.setEmail(militarAtualizado.getEmail());
        militarExistente.setLogradouro(militarAtualizado.getLogradouro());
        militarExistente.setNumeroCasa(militarAtualizado.getNumeroCasa());
        militarExistente.setBairro(militarAtualizado.getBairro());

        return militarRepository.save(militarExistente);
    }


    public void deletar(Long id) {
        Militar militar = buscarPorId(id);
        militarRepository.delete(militar);
    }
}
