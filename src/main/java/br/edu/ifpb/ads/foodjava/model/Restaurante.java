package br.edu.ifpb.ads.foodjava.model;

public class Restaurante {
    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String categoriaCulinaria;
    private String email;
    private String logo;

    public Restaurante() {

    }

    public Restaurante(String nome, String cnpj, String endereco, String telefone, String categoriaCulinaria, String email, String logo) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.telefone = telefone;
        this.categoriaCulinaria = categoriaCulinaria;
        this.email = email;
        this.logo = logo;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCategoriaCulinaria() {
        return categoriaCulinaria;
    }

    public void setCategoriaCulinaria(String categoriaCulinaria) {
        this.categoriaCulinaria = categoriaCulinaria;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
