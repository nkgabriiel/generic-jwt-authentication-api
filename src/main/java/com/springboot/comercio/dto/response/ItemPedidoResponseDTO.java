package com.springboot.comercio.dto.response;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long produtoId,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subTotal
) {
}
