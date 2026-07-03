package br.edu.ifpb.ads.foodjava.service;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.CarrinhoVazioException;
import br.edu.ifpb.ads.foodjava.model.Carrinho;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;

import java.util.List;

public class CarrinhoService {

    private static final Carrinho carrinho = new Carrinho();

    private final PedidoRepository pedidoRepository = new PedidoRepository();

    public void adicionarItem(ItemCardapio itemCardapio) {
        ItemPedido itemPedido = new ItemPedido(
                itemCardapio.getNome(),
                1,
                itemCardapio.getPreco()
        );

        carrinho.adicionarItem(itemPedido);
    }

    public void removerItem(ItemPedido item) {
        carrinho.removerItem(item);
    }

    public List<ItemPedido> getItens() {
        return carrinho.getItens();
    }

    public double calcularTotal() {
        return carrinho.calcularTotal();
    }

    public boolean estaVazio() {
        return carrinho.getItens().isEmpty();
    }

    public Pedido finalizarPedido(String emailCliente)
            throws CarrinhoVazioException, ArquivoImportacaoException {

        int novoId = pedidoRepository.getTotalPedidos() + 1;

        Pedido pedido = new Pedido(novoId, carrinho, emailCliente);

        pedidoRepository.adicionar(pedido);

        carrinho.limpar();

        return pedido;
    }
}