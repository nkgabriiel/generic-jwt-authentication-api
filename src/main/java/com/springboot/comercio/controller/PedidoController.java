package com.springboot.comercio.controller;

import com.springboot.comercio.dto.request.PedidoRequestDTO;
import com.springboot.comercio.dto.response.PedidoResponseDTO;
import com.springboot.comercio.model.Usuario;
import com.springboot.comercio.service.PedidoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Auth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(
            @RequestBody @Valid PedidoRequestDTO dto,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.gerarPedido(dto, usuario.getCliente()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PedidoResponseDTO>> meusPedidos(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(pedidoService.listarPedidosCliente(usuario.getCliente().getId()));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<PedidoResponseDTO>> pedidosPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.listarPedidosCliente(id));
    }
}