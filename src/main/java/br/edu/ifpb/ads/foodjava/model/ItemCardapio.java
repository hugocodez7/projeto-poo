package br.edu.ifpb.ads.foodjava.model;

public class ItemCardapio {
    private String id;
    private String nome;
    private String descricao;
    private String getCaminhoImagem;
    private double preco;
    private Categoria categoria;
    private boolean disponivel;

    public ItemCardapio() {

    }

    public ItemCardapio(String id, String nome, String descricao, String getCaminhoImagem, double preco, Categoria categoria, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.getCaminhoImagem = getCaminhoImagem;
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

    public String getGetCaminhoImagem() {
        return getCaminhoImagem;
    }

    public void setGetCaminhoImagem(String getCaminhoImagem) {
        this.getCaminhoImagem = getCaminhoImagem;
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

    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", getCaminhoImagem='" + getCaminhoImagem + '\'' +
                ", preco=" + preco +
                ", categoria=" + categoria +
                ", disponivel=" + disponivel +
                '}';
    }
}