package br.edu.ifpb.ads.foodjava.model;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private int id;
    private LocalDateTime dataHora;
    private List<ItemPedido> itens;
    private double valorTotal;
    private StatusPedido status;

    public Pedido(int id, Carrinho carrinho) {
        this.id = id;
        this.dataHora = LocalDateTime.now();
        this.itens = carrinho.getItens();
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

    public void avancarStatus() {

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
                    break;
            }
        }

    public void cancelarPedido() {

        if (status == StatusPedido.AGUARDANDO_CONFIRMACAO) {
            status = StatusPedido.CANCELADO;
        }
    }


}





