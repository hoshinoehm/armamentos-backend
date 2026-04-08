package com.example.auth.repositories;


import com.example.auth.domain.Colete;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColeteRepository extends JpaRepository<Colete, Long> {
    // Você pode adicionar métodos customizados se quiser, ex:
    // Optional<Colete> findByNumeroSerie(String numeroSerie);
}