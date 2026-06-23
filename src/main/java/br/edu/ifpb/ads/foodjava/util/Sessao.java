package br.edu.ifpb.ads.foodjava.util;

import br.edu.ifpb.ads.foodjava.model.Usuario;

public class Sessao {

    private static Usuario usuarioLogado;

    public static void iniciar(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void encerrar() {
        usuarioLogado = null;
    }
}
