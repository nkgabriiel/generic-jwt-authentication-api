package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.LoginRequestDTO;
import com.springboot.comercio.dto.response.TokenResponseDTO;
import com.springboot.comercio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints públicos de autenticação")
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login (@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }
}
