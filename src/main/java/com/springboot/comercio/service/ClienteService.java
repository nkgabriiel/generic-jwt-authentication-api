package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.exception.ClienteNotFoundException;
import com.springboot.comercio.exception.InvalidUserRequestData;
import com.springboot.comercio.model.Cliente;
import com.springboot.comercio.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        if(repository.existsByCpf(dto.cpf())) {
            throw new InvalidUserRequestData("Já existe um usuário cadastrado com esse CPF");
        }

        if(repository.existsByEmail(dto.email())) {
            throw new InvalidUserRequestData("Já existe um usuário cadastrado com esse email");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setPontoFidelidade(0);

        Cliente salvo = repository.save(cliente);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));
        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public void deletar(Long id) {
        if(!repository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente não encontrado.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public ClienteResponseDTO editar(ClienteUpdateDTO dto, Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        if(dto.email() != null && !dto.email().equals(cliente.getEmail()) && repository.existsByEmail(dto.email())) {
            throw new InvalidUserRequestData("Já existe um usuário cadastrado com esse email.");
        }

        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.email() != null) {
            cliente.setEmail(dto.email());
        }

        Cliente clienteEditado = repository.save(cliente);
        return toResponse(clienteEditado);

    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public ClienteResponseDTO me(Cliente cliente) {
        if(cliente == null) {
            throw new ClienteNotFoundException("Nenhum cliente vinculado a esse usuário");
        }
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getPontoFidelidade()
        );
    }

    private ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getPontoFidelidade()
        );
    }

}
