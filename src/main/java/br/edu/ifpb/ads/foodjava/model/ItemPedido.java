package br.edu.ifpb.ads.foodjava.model;

public class ItemPedido {

    private String nome;
    private int quantidade;
    private double precoUnidade;

    public ItemPedido() {
    }

    public ItemPedido(String nome, int quantidade, double precoUnidade) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnidade = precoUnidade;
    }

    public void aumentarQuantidade() {
        quantidade++;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnidade() {
        return precoUnidade;
    }

    public double getSubtotal() {
        return quantidade * precoUnidade;
    }
}
