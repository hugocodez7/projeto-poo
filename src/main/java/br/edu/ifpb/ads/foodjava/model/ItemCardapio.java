package br.edu.ifpb.ads.foodjava.model;

public class ItemCardapio {

    private String id;
    private String nome;
    private String descricao;
    private String caminhoImagem;
    private double preco;
    private Categoria categoria;
    private boolean disponivel;

    public ItemCardapio() {
    }

    public ItemCardapio(String id, String nome, String descricao, String caminhoImagem,
                        double preco, Categoria categoria, boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.caminhoImagem = caminhoImagem;
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
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
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

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}