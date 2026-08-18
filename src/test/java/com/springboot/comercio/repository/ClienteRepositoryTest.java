package com.springboot.comercio.repository;

import com.springboot.comercio.config.JpaAuditingConfig;
import com.springboot.comercio.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("Deve retornar erro quando tentar criar cliente com email já cadastrado")
    void deveRetornarErroQuandoCriandoClienteComEmailUtilizado() {
        Cliente clienteExistente = criarCliente("Kaiser", "12345678901");
        entityManager.persistAndFlush(clienteExistente);

        Cliente novoCliente = criarCliente("Kaiser", "98765432110");

        assertThatThrownBy(() -> clienteRepository.saveAndFlush(novoCliente))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @DisplayName("Deve retornar erro quando tentar criar cliente com CPF já cadastrado")
    void deveRetornarErroQuandoCriandoClienteComCpfUtilizado() {
        Cliente clienteExistente = criarCliente("Gabriel", "12345678901");
        entityManager.persistAndFlush(clienteExistente);

        Cliente novoCliente = criarCliente("Kaiser", "12345678901");

        assertThatThrownBy(() -> clienteRepository.saveAndFlush(novoCliente))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    private Cliente criarCliente(String nome, String cpf) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(nome + "@email.com");
        cliente.setCpf(cpf);
        return cliente;
    }
}

