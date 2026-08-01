package com.springboot.comercio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 14, message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email

) {
}
