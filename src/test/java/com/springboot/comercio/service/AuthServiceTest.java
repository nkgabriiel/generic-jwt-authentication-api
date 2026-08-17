package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.LoginRequestDTO;
import com.springboot.comercio.dto.response.TokenResponseDTO;
import com.springboot.comercio.model.Role;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("gabriel@email.com");
        usuario.setSenha("senha-encriptada");
        usuario.setRole(Role.USER);
        return usuario;
    }

    @Test
    @DisplayName("Deve retornar token quando credenciais forem válidas")
    void deveRetornarTokenQuandoCredenciaisValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("gabriel@email.com", "password123");
        Usuario usuario = criarUsuario();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(jwtService.gerarToken(usuario)).thenReturn("token-fake-gerado");

        TokenResponseDTO response = authService.login(dto);

        assertThat(response.token()).isEqualTo("token-fake-gerado");
        assertThat(response.tipo()).isEqualTo("Bearer ");
        assertThat(response.email()).isEqualTo("gabriel@email.com");
        assertThat(response.role()).isEqualTo("USER");

    }

    @Test
    @DisplayName("Deve retornar exception quando credenciais forem inválidas")
    void deveLancarExceptionQuandoCredenciaisInvalidas() {
        LoginRequestDTO dto = new LoginRequestDTO("gabriel@email.com", "senha-errada");

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThatThrownBy(() -> authService.login(dto))
                .isInstanceOf(BadCredentialsException.class);

        verify(jwtService, never()).gerarToken(any());

    }

}
