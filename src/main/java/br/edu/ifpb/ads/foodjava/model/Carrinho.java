package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.exception.CarrinhoVazioException;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private List<ItemPedido> itens;

    public Carrinho() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
    }

    public double calcularTotal() {
        double total = 0;

        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }

        return total;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void validarCarrinho() throws CarrinhoVazioException {
        if (itens.isEmpty()) {
            throw new CarrinhoVazioException("Não é possível confirmar pedido com carrinho vazio.");
        }
    }

    public void limpar() {
        itens.clear();
    }
}




