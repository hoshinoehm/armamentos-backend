package com.example.auth.repositories;

import com.example.auth.domain.ControleArmamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ControleArmamentoRepository extends JpaRepository<ControleArmamento, Long> {
    // Aqui também podem ser adicionados filtros no futuro (por militar, por data etc.)

    boolean existsByArmaIdAndDataDevolucaoIsNull(Long armaId);

    @Query("SELECT c FROM ControleArmamento c WHERE c.dataDevolucao IS NULL")
    List<ControleArmamento> findAllNaoBaixadas();

    @Query("SELECT COUNT(c) FROM ControleArmamento c WHERE c.dataDevolucao IS NULL")
    long contarCautelasAtivas();



}