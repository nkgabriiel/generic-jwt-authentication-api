package com.springboot.comercio.repository;

import com.springboot.comercio.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);
    boolean existsByCpf (String cpf);
    boolean existsByEmail (String email);
}
