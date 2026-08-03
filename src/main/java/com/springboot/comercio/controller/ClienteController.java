package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.ClienteRequestDTO;
import com.springboot.comercio.dto.request.ClienteUpdateDTO;
import com.springboot.comercio.dto.response.ClienteResponseDTO;
import com.springboot.comercio.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
@SecurityRequirement(name = "Bearer Auth")
public class ClienteController {

    private final ClienteService service;

    @Operation(summary = "Criar cliente", description = "Cria um novo cliente. Requer role ADMIN")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(
            @RequestBody @Valid ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Listar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Deletar cliente", description = "Remove um cliente. Requer role ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza nome e/ou email. Requer role ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> editar(
            @RequestBody @Valid ClienteUpdateDTO dto, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.editar(dto, id));
    }

    @Operation(summary = "Listar clientes paginado")
    @ApiResponse(responseCode = "200", description = "Página retornada com sucesso")
    @GetMapping("/paginado")
    public ResponseEntity<Page<ClienteResponseDTO>> listarPaginado(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

}
