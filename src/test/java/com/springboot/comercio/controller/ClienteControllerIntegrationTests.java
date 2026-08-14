package com.springboot.comercio.controller;

import com.springboot.comercio.model.Cliente;
import com.springboot.comercio.model.Role;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.repository.ClienteRepository;
import com.springboot.comercio.repository.UsuarioRepository;
import com.springboot.comercio.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClienteControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Usuario admin;
    private Usuario user;

    private String tokenAdmin;
    private String tokenUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);
        clienteRepository.deleteAll();
        usuarioRepository.deleteAll();

        admin = new Usuario();
        admin.setEmail("admin@test.com");
        admin.setSenha(passwordEncoder.encode("senha123"));
        admin.setRole(Role.ADMIN);
        usuarioRepository.save(admin);
        tokenAdmin = jwtService.gerarToken(admin);

        user = new Usuario();
        user.setEmail("user@test.com");
        user.setSenha(passwordEncoder.encode("senha123"));
        user.setRole(Role.USER);
        usuarioRepository.save(user);
        tokenUser = jwtService.gerarToken(user);
    }

    @Test
    @DisplayName("Deve retornar 401 quando não enviar token")
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(post("/api/v1/clientes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 403 quando USER tentar criar cliente")
    void deveRetornar403QuandoUserCriarCliente() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + tokenUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Gabriel",
                                "cpf": "12345678901",
                                "email": "Gabriel@email.com"                        
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 401 quando token estiver expirado")
    void deveRetornar401ComTokenExpirado() throws Exception {
        ReflectionTestUtils.setField(jwtService, "expiration", -10000000L);

        String tokenExpirado = jwtService.gerarToken(admin);

        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + tokenExpirado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Gabriel",
                                "cpf": "12345678901",
                                "email": "Gabriel@email.com"                        
                                }
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 201 quanto ADMIN criar cliente")
    void deveRetornar201QuandoAdminCriarCliente() throws Exception {
        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Gabriel",
                                "cpf": "12345678901",
                                "email": "Gabriel@email.com"                        
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar 409 quando cadastrando CPF duplicado")
    void deveRetornar409QuandoCpfDuplicado() throws Exception {

        Cliente cliente = new Cliente();
        cliente.setNome("Kaiser");
        cliente.setEmail("Kaiser@email.com");
        cliente.setCpf("12345678901");
        clienteRepository.save(cliente);

        mockMvc.perform(post("/api/v1/clientes")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Gabriel",
                                "cpf": "12345678901",
                                "email": "Gabriel@email.com"                        
                                }
                                """))
                .andExpect(status().isConflict());
    }

}
