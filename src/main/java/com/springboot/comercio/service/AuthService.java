package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.LoginRequestDTO;
import com.springboot.comercio.dto.response.TokenResponseDTO;
import com.springboot.comercio.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.gerarToken(usuario);

        return new TokenResponseDTO(
                token,
                "Bearer ",
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}
