package br.edu.ifpb.ads.foodjava.model;

public class ItemPedido {



    private String nome;
    private int quantidade;
    private double precoUnitario;


    public ItemPedido (String nome, int quantidade, double precoUnitario) {

        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double getSubtotal(){
        return quantidade * precoUnitario;
    }


}
