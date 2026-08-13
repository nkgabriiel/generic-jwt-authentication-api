package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.exception.ClienteNotFoundException;
import com.springboot.comercio.exception.InvalidUserRequestData;
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
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

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

    // Testes de clienteService.salvar();
    @Test
    @DisplayName("Deve retornar exception quando CPF já for cadastrado")
    void deveRetornarExceptionQuandoCpfCadatrado() {
        ClienteRequestDTO dto = new ClienteRequestDTO("Gabriel", "12345678901", "Gabriel@email.com");

        when(clienteRepository.existsByCpf(dto.cpf())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.salvar(dto)).
                isInstanceOf(InvalidUserRequestData.class)
                .hasMessage("Já existe um usuário cadastrado com esse CPF");
    }

    @Test
    @DisplayName("Deve retornar exception quando email já for cadastrado")
    void deveRetornarExceptionQuandoEmailJaCadastrado() {
        ClienteRequestDTO dto = new ClienteRequestDTO("Gabriel", "12345678901", "Gabriel@email.com");

        when(clienteRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.salvar(dto)).
                isInstanceOf(InvalidUserRequestData.class)
                .hasMessage("Já existe um usuário cadastrado com esse email");
    }

    @Test
    @DisplayName("Deve salvar o cliente com sucesso")
    void deveSalvarClienteComSucesso() {
        ClienteRequestDTO dto = new ClienteRequestDTO("Gabriel", "12345678901", "Gabriel@email.com");

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente clienteRecebido = invocation.getArgument(0);
            clienteRecebido.setId(1L);
            return clienteRecebido;
        });

        ClienteResponseDTO response = clienteService.salvar(dto);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Gabriel");
        assertThat(response.cpf()).isEqualTo("12345678901");
        assertThat(response.email()).isEqualTo("Gabriel@email.com");

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    //Testes de clienteService.buscarPorId();
    @Test
    @DisplayName("Deve lançar exception quando buscar por id e cliente não existir")
    void deveLancarExceptionQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve retornar sucesso quando buscando por usuário que existe")
    void deveLancarSucessoQuandoClienteExistir() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));

        ClienteResponseDTO response = clienteService.buscarPorId(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Gabriel");
    }

    //Testes de clienteService.listarTodos();
    @Test
    @DisplayName("Deve retornar lista de clientes")
    void deveListarTodosOsClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(clienteExistente));

        List<ClienteResponseDTO> lista = clienteService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.getFirst().nome()).isEqualTo("Gabriel");
    }

    //Testes de clienteService.deletar()
    @Test
    @DisplayName("Deve deletar cliente quando ID existir")
    void deveDeletarClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

       assertThatCode(() -> clienteService.deletar(1L))
               .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Deve lançar exception ao tentar deletar cliente inexistente")
    void deveLancarExceptionAoDeletarClienteInexistente() {
        when(clienteRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.deletar(999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessage("Cliente não encontrado.");
    }

    //Teste de clienteService.editar();
    @Test
    @DisplayName("Deve atualizar os campos enviados (nome)")
    void deveAtualizarApenasNomeCliente() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO("Kaiser", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente clienteRecebido = invocation.getArgument(0);
            clienteRecebido.setId(1L);
            return clienteRecebido;
        });

        ClienteResponseDTO response = clienteService.editar(dto, 1L);

        assertThat(response.nome()).isEqualTo("Kaiser");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve atualizar os campos enviados (email)")
    void deveAtualizarApenasEmailCliente() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO(null, "Kaiser@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente clienteRecebido = invocation.getArgument(0);
            clienteRecebido.setId(1L);
            return clienteRecebido;
        });

        ClienteResponseDTO response = clienteService.editar(dto, 1L);

        assertThat(response.email()).isEqualTo("Kaiser@email.com");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve atualizar o campo nome mesmo quando email for igual")
    void deveAtualizarCampoQuandoEmailForIgual() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO("Kaiser", "Gabriel@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente clienteRecebido = invocation.getArgument(0);
            clienteRecebido.setId(1L);
            return clienteRecebido;
        });

        ClienteResponseDTO response = clienteService.editar(dto, 1L);

        assertThat(response.nome()).isEqualTo("Kaiser");
        assertThat(response.email()).isEqualTo("Gabriel@email.com");
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exception quando usuário não for encontrado")
    void deveLancarExceptionQuandoClienteNaoEncontradoParaEditar() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO(null, "Kaiser@email.com");

        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.editar(dto, 999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessage("Cliente não encontrado");
    }

    @Test
    @DisplayName("Deve lançar exception quando tentar editar para um email já cadastrado")
    void deveLancarExceptionQuandoEditarParaEmailJaCadastrado() {
        ClienteUpdateDTO dto = new ClienteUpdateDTO(null, "Kaiser@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.editar(dto, 1L))
                .isInstanceOf(InvalidUserRequestData.class)
                .hasMessage("Já existe um usuário cadastrado com esse email.");
    }

}
