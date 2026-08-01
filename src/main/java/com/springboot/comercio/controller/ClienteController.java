package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar (
            @RequestBody @Valid ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> editar(
            @RequestBody @Valid ClienteUpdateDTO dto, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.editar(dto, id));
    }

}
