package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.exception.ClienteNotFoundException;
import com.springboot.comercio.model.Cliente;
import com.springboot.comercio.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setPontoFidelidade(0);

        Cliente salvo = repository.save(cliente);
        return toResponse(salvo);
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
        return toResponse(cliente);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public void deletar(Long id) {
        if(!repository.existsById(id)) {
            throw new ClienteNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public ClienteResponseDTO editar(ClienteUpdateDTO dto, Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.email() != null) {
            cliente.setEmail(dto.email());
        }

        Cliente clienteEditado = repository.save(cliente);
        return toResponse(clienteEditado);

    }

    public Page<ClienteResponseDTO> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
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
