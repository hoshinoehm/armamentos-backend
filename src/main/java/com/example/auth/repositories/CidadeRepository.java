package com.example.auth.repositories;


import com.example.auth.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
    List<Cidade> findByUf(Integer uf);
}
