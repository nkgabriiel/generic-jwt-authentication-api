package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.LoginRequestDTO;
import com.springboot.comercio.dto.request.RefreshRequestDTO;
import com.springboot.comercio.dto.request.RegisterRequestDTO;
import com.springboot.comercio.dto.response.TokenResponseDTO;
import com.springboot.comercio.dto.response.UsuarioResponseDTO;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(service.me(usuario));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO dto) {
        return ResponseEntity.ok(service.refresh(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequestDTO dto) {
        service.logout(dto);
        return ResponseEntity.noContent().build();
    }
}
