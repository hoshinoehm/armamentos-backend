package com.example.auth.repositories;


import com.example.auth.domain.Militar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MilitarRepository extends JpaRepository<Militar, Long> {
    Optional<Militar> findByMatricula(String matricula);
    Optional<Militar> findByCpf(String cpf);

    @Query("SELECT COUNT(m) FROM Militar m")
    long contarMilitares();

}
