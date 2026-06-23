package br.edu.ifpb.ads.foodjava.model;

public class Gerente extends Usuario {

    @Override
    public String getTipoPerfil() {
        return "GERENTE";
    }

    public Gerente() {

    }

    public Gerente(long id, String nome, String email, String senha, String telefone) {
        super(id, nome, email, senha, telefone);
        setTipo(getTipoPerfil());
    }
}
