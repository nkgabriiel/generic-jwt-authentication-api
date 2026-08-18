package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.ProdutoRequestDTO;
import com.springboot.comercio.dto.request.ProdutoUpdateDTO;
import com.springboot.comercio.dto.response.ProdutoResponseDTO;
import com.springboot.comercio.service.ProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Auth")
public class ProdutoController {

    private final ProdutoService produtoService;


    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(
            @RequestBody @Valid ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> editar(
            @RequestBody @Valid ProdutoUpdateDTO dto, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.editarProduto(dto, id));
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarPaginado(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarPaginado(pageable));
    }

}
