package com.example.auth.repositories;

import com.example.auth.domain.Arma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArmaRepository extends JpaRepository<Arma, Long> {
    Optional<Arma> findByNumeroSerie(String numeroSerie);

    @Query("""
    SELECT a FROM Arma a
    WHERE a.id NOT IN (
        SELECT c.arma.id FROM ControleArmamento c
        WHERE c.dataDevolucao IS NULL AND c.arma IS NOT NULL
    )
""")

    List<Arma> findArmasDisponiveis();

    @Query("""
    SELECT COUNT(a) FROM Arma a
    WHERE a.id NOT IN (
        SELECT c.arma.id FROM ControleArmamento c
        WHERE c.dataDevolucao IS NULL
    )
""")
    long contarArmasDisponiveis();


    @Query("SELECT COUNT(a) FROM Arma a")
    long contarArmas();

    // Aqui você pode adicionar métodos customizados se precisar, como:
    // Optional<Arma> findByNumeroSerie(String numeroSerie);
}