package com.springboot.comercio.service;

import com.springboot.comercio.dto.request.ProdutoRequestDTO;
import com.springboot.comercio.dto.request.ProdutoUpdateDTO;
import com.springboot.comercio.dto.response.ProdutoResponseDTO;
import com.springboot.comercio.exception.InvalidProductRequestData;
import com.springboot.comercio.exception.ProductNotFoundException;
import com.springboot.comercio.model.Produto;
import com.springboot.comercio.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO dto) {
        if (produtoRepository.existsByNome(dto.nome())) {
            throw new InvalidProductRequestData("Já existe um produto cadastrado com esse nome");
        }

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());

        Produto salvo = produtoRepository.save(produto);
        return toResponse(salvo);
    }

    @Transactional
    public ProdutoResponseDTO editarProduto(ProdutoUpdateDTO dto, Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        if (dto.nome() != null && !dto.nome().equals(produto.getNome()) && produtoRepository.existsByNome(dto.nome())) {
            throw new InvalidProductRequestData("Já existe um produto cadastrado com esse nome");
        }

        if (dto.nome() != null) {
            produto.setNome(dto.nome());
        }

        if (dto.estoque() != null) {
            produto.setEstoque(dto.estoque());
        }

        if (dto.preco() != null) {
            produto.setPreco(dto.preco());
        }

        Produto produtoEditado = produtoRepository.save(produto);
        return toResponse(produtoEditado);
    }

    @Transactional
    public void deletarProduto(Long id) {
        if(!produtoRepository.existsById(id)) {
            throw new ProductNotFoundException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));

        return toResponse(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarPaginado(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(this::toResponse);
    }

    private ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getEstoque(),
                produto.getPreco()
        );
    }
}
