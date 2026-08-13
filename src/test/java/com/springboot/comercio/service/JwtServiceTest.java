package com.springboot.comercio.service;

import com.springboot.comercio.exception.InvalidTokenException;
import com.springboot.comercio.model.Role;
import com.springboot.comercio.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;


public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret", "2c5eefb8f6aaeffb53152275ab601139ee14a04b99fbea755b2c421168835402");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail("gabriel@email.com");
        usuario.setSenha("senha123");
        usuario.setRole(Role.USER);
        return usuario;
    }

    // Teste de validação da chave secret
    @Test
    @DisplayName("Deve lançar exception quando secret inválido")
    void deveLancarExceptionQuandoTokenInvalido() {
        ReflectionTestUtils.setField(jwtService, "secret", "chave-curta");

        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(jwtService, "validarSecret"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("jwt.secret precisa ter pelo menos 32 bytes. "
                        + "Configure a variável JWT_SECRET com um valor mais longo.");
    }

    @Test
    @DisplayName("Deve lançar sucesso quando token for correto")
    void deveLancarSucessoQuandoTokenCorreto() {
        ReflectionTestUtils.setField(jwtService, "secret", "2c5eefb8f6aaeffb53152275ab601139ee14a04b99fbea755b2c421168835402");

        assertThatCode(() ->ReflectionTestUtils.invokeMethod(jwtService, "validarSecret"))
                .doesNotThrowAnyException();
    }

    //Testes do token
    @Test
    @DisplayName("Deve gerar um token com o email do usuário")
    void deveGerarTokenValido() {
        Usuario usuario = criarUsuario();

        String token = jwtService.gerarToken(usuario);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extrairEmail(token)).isEqualTo("gabriel@email.com");
    }

    @Test
    @DisplayName("Deve validar token para o mesmo usuário")
    void deveValidarTokenMesmoUsuario() {
        Usuario usuario = criarUsuario();

        String token = jwtService.gerarToken(usuario);

        assertThat(token).isNotBlank();
        assertThat(jwtService.tokenValido(token, usuario)).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exception quando token expirado")
    void deveLancarExceptionQuandoTokenExpirado() {
        ReflectionTestUtils.setField(jwtService, "expiration", -86400000L);
        Usuario usuario = criarUsuario();

        String token = jwtService.gerarToken(usuario);

        assertThatThrownBy(() -> jwtService.tokenValido(token, usuario))
                .isInstanceOf(InvalidTokenException.class);

    }

    @Test
    @DisplayName("Deve lançar exception quando assinatura alterada")
    void deveLancarExceptionParaAssinaturaAlterada() {
        Usuario usuario = criarUsuario();
        String token = jwtService.gerarToken(usuario);

        ReflectionTestUtils.setField(jwtService, "secret", "eujadigiteiapagueidigiteidenovoeujatexinguei");

        assertThatThrownBy(() -> jwtService.tokenValido(token, usuario))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("Deve lançar exception para token mal formado")
    void deveLancarExceptionTokenMalFormado() {
        String token = "ferrugem.e.pericles.as.vozes";

        assertThatThrownBy(() -> jwtService.extrairEmail(token))
                .isInstanceOf(InvalidTokenException.class);
    }

}
