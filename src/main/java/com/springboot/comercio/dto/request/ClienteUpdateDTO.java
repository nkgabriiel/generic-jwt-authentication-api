package com.springboot.comercio.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record ClienteUpdateDTO(
        @Size(min = 1, message = "Nome não pode ser vazio")
        String nome,

        @Email(message = "Email inválido")
        String email
) {
}
