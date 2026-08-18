package com.springboot.comercio.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoUpdateDTO (
        @Size(min = 1, message = "Nome não pode ser vazio")
        String nome,

        @PositiveOrZero(message = "Valor deve ser positivo")
        Integer estoque,

        @Positive(message = "Valor deve ser  positivo")
        BigDecimal preco
) {
}
