package br.edu.ifpb.ads.foodjava.model;

import br.edu.ifpb.ads.foodjava.exception.CancelamentoNaoPermitidoException;
import br.edu.ifpb.ads.foodjava.exception.CarrinhoVazioException;
import br.edu.ifpb.ads.foodjava.exception.StatusInvalidoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int id;
    private LocalDateTime dataHora;
    private List<ItemPedido> itens;
    private double valorTotal;
    private StatusPedido status;

    public Pedido(int id, Carrinho carrinho) throws CarrinhoVazioException {
        carrinho.validarCarrinho();

        this.id = id;
        this.dataHora = LocalDateTime.now();
        this.itens = new ArrayList<>(carrinho.getItens());
        this.valorTotal = carrinho.calcularTotal();
        this.status = StatusPedido.AGUARDANDO_CONFIRMACAO;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void avancarStatus() throws StatusInvalidoException {

        switch (status) {

            case AGUARDANDO_CONFIRMACAO:
                status = StatusPedido.CONFIRMADO;
                break;

            case CONFIRMADO:
                status = StatusPedido.EM_PREPARO;
                break;

            case EM_PREPARO:
                status = StatusPedido.SAIU_PARA_ENTREGA;
                break;

            case SAIU_PARA_ENTREGA:
                status = StatusPedido.ENTREGUE;
                break;

            default:
                throw new StatusInvalidoException(
                        "Não é possível avançar o status do pedido."
                );
        }
    }

    public void cancelarPedido() throws CancelamentoNaoPermitidoException {

        if (status == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            status = StatusPedido.CANCELADO;
        } else {
            throw new CancelamentoNaoPermitidoException(
                    "O pedido não pode ser cancelado após a confirmação."
            );
        }
    }
}





