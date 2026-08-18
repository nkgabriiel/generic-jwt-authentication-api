package com.springboot.comercio.dto.response;

import java.math.BigDecimal;

public record ProdutoResponseDTO (
        Long id,
        String nome,
        Integer estoque,
        BigDecimal preco
) {
}
