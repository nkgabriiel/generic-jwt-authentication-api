package com.springboot.comercio.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotNull
        @Min(0)
        Integer estoque,

        @NotNull
        @Positive(message = "Valor deve ser positivo")
        BigDecimal preco

        ) {

}
