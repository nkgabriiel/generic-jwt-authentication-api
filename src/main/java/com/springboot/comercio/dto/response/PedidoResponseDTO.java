package com.springboot.comercio.dto.response;

import com.springboot.comercio.model.StatusPedido;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal total,
        StatusPedido status

) {
}
