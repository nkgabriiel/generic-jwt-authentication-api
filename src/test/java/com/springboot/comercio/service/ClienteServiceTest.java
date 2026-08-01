package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.exception.ClienteNotFoundException;
import com.springboot.comercio.model.Cliente;
import com.springboot.comercio.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    private Cliente clienteExistente;

    @BeforeEach
    void setUp() {
        clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNome("Gabriel");
        clienteExistente.setCpf("12345678901");
        clienteExistente.setEmail("Gabriel@email.com");
        clienteExistente.setPontoFidelidade(0);
    }

    @Test
    @DisplayName("Deve salvar o cliente e retornar response com o ID gerado")
    void deveSalvarClienteComSucesso() {
        ClienteRequestDTO dto = new ClienteRequestDTO("Gabriel", "12345678901", "Gabriel@email.com");

        when(repository.save(any(Cliente.class))).thenReturn(clienteExistente);

        ClienteResponseDTO response = service.salvar(dto);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Gabriel");
        assertThat(response.email()).isEqualTo("Gabriel@email.com");
        verify(repository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar ClienteNotFoundException quando ID não existir")
    void deveLancarExceptionQuandoClienteNaoEncontrado() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("Deve retornar lista de clientes")
    void deveListarTodosOsClientes() {
        when(repository.findAll()).thenReturn(List.of(clienteExistente));

        List<ClienteResponseDTO> lista = service.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.getFirst().nome()).isEqualTo("Gabriel");
    }

    @Test
    @DisplayName("Deve deletar cliente quando ID existir")
    void deveDeletarClienteComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar deletar cliente inexistente")
    void deveLancarExceptionAoDeletarClienteInexistente() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.deletar(999L))
                .isInstanceOf(ClienteNotFoundException.class);
    }

    @Test
    @DisplayName("Deve atualizar apenas os campos enviados")
    void deveAtualizarClienteParcialmente() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO("Kaiser", null);

        when(repository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(repository.save(any(Cliente.class))).thenReturn(clienteExistente);

        service.editar(dto, 1L);
        verify(repository, times(1)).save(any(Cliente.class));
    }
}
