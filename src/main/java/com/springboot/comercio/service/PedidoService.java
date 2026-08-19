package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.PedidoRequestDTO;
import com.springboot.comercio.dto.response.ItemPedidoResponseDTO;
import com.springboot.comercio.dto.response.PedidoResponseDTO;
import com.springboot.comercio.exception.InsufficientProductStockException;
import com.springboot.comercio.exception.PedidoNotFoundException;
import com.springboot.comercio.exception.ProductNotFoundException;
import com.springboot.comercio.model.*;
import com.springboot.comercio.repository.PedidoRepository;
import com.springboot.comercio.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;

    @Transactional
    public PedidoResponseDTO gerarPedido(PedidoRequestDTO dto, Cliente cliente) {

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PENDENTE);

        List<ItemPedido> itensEntidade = new ArrayList<>();
        BigDecimal totalCompra = BigDecimal.ZERO;



        for (PedidoRequestDTO.ItemPedidoData itemSolicitado : dto.itens()) {
            Produto produto = produtoRepository.findById(itemSolicitado.produtoId())
                    .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));


            int linhasAfetadas = produtoRepository.baixarEstoqueSeSuficiente(
                    itemSolicitado.produtoId(), itemSolicitado.quantidade());

            if(linhasAfetadas == 0) {
                throw new InsufficientProductStockException("Produtos insuficientes no estoque");
            }

            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal subTotal = precoUnitario.multiply(BigDecimal.valueOf(itemSolicitado.quantidade()));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemSolicitado.quantidade());
            item.setPrecoUnitario(precoUnitario);
            item.setPedido(pedido);
            itensEntidade.add(item);


            totalCompra = totalCompra.add(subTotal);
        }

        pedido.setItens(itensEntidade);
        pedido.setTotal(totalCompra);

        Pedido salvo = pedidoRepository.save(pedido);

        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPedidosCliente(Long id) {
        Cliente cliente = clienteService.buscarEntidadePorId(id);

        return pedidoRepository.findByClienteId(id)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PedidoResponseDTO me(Pedido pedido) {
        return toResponse(pedido);
    }

    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensResposta = pedido.getItens().stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getProduto().getId(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                itensResposta,
                pedido.getTotal(),
                pedido.getStatus()
        );
    }

}
