package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.LoginRequestDTO;
import com.springboot.comercio.dto.request.RefreshRequestDTO;
import com.springboot.comercio.dto.request.RegisterRequestDTO;
import com.springboot.comercio.dto.response.TokenResponseDTO;
import com.springboot.comercio.dto.response.UsuarioResponseDTO;
import com.springboot.comercio.exception.InvalidTokenException;
import com.springboot.comercio.exception.InvalidUserRequestData;
import com.springboot.comercio.model.RefreshToken;
import com.springboot.comercio.model.Role;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.repository.TokenRepository;
import com.springboot.comercio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.gerarToken(usuario);


        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiraEm(LocalDateTime.now().plusWeeks(1));
        refreshToken.setRevogado(false);
        tokenRepository.save(refreshToken);

        return new TokenResponseDTO(
                token,
                "Bearer ",
                usuario.getEmail(),
                usuario.getRole().name(),
                refreshToken.getToken()
        );
    }

    @Transactional
    public TokenResponseDTO register(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new InvalidUserRequestData("Esse email já foi cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(dto.email());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setRole(Role.USER);

        usuarioRepository.save(novoUsuario);

        String token = jwtService.gerarToken(novoUsuario);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUsuario(novoUsuario);
        refreshToken.setExpiraEm(LocalDateTime.now().plusWeeks(1));
        refreshToken.setRevogado(false);
        tokenRepository.save(refreshToken);

        return new TokenResponseDTO(
                token,
                "Bearer ",
                novoUsuario.getEmail(),
                novoUsuario.getRole().name(),
                refreshToken.getToken()
        );
    }

    public TokenResponseDTO refresh(RefreshRequestDTO dto) {
        RefreshToken refreshToken = tokenRepository.findByToken(dto.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Token inválido ou expirado.", null));

        if(refreshToken.isRevogado() || refreshToken.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Token inválido ou expirado.", null);
        }

        String token = jwtService.gerarToken(refreshToken.getUsuario());

        return new TokenResponseDTO(
                token,
                "Bearer ",
                refreshToken.getUsuario().getEmail(),
                refreshToken.getUsuario().getRole().name(),
                refreshToken.getToken()
        );
    }

    public UsuarioResponseDTO me(Usuario usuario) {
            return new UsuarioResponseDTO(
                    usuario.getEmail(),
                    usuario.getRole().name()
            );
    }
}
