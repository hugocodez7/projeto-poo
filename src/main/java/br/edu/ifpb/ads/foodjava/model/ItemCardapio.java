package br.edu.ifpb.ads.foodjava.model;

public class ItemCardapio {
    private String id;
    private String nome;
    private String descricao;
    private String CaminhoImagem;
    private double preco;
    private Categoria categoria;
    private boolean disponivel;

    public ItemCardapio() {

    }

    public ItemCardapio(String id, String nome, String descricao, String CaminhoImagem, double preco, Categoria categoria, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.CaminhoImagem = CaminhoImagem;
        this.preco = preco;
        this.categoria = categoria;
        this.disponivel = disponivel;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoImagem() {
        return CaminhoImagem;
    }

    public void setCaminhoImagem(String CaminhoImagem) {
        this.CaminhoImagem = CaminhoImagem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

}