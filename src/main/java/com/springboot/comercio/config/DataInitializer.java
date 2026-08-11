package com.springboot.comercio.config;

import com.springboot.comercio.model.Role;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if(usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setEmail(adminEmail);
            admin.setSenha(adminEmail);
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
        }
    }
}
