package com.springboot.comercio.repository;

import com.springboot.comercio.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNome(String nome);

    @Modifying
    @Query("UPDATE Produto p SET p.estoque = p.estoque - :quantidade WHERE p.id = :id AND p.estoque >= :quantidade")
    int baixarEstoqueSeSuficiente(@Param("id") Long id, @Param("quantidade") Integer quantidade);
}
