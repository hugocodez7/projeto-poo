package br.edu.ifpb.ads.foodjava.util;

public class ValidadorSenha {

    public static boolean validar(String senha) {
        if (senha == null) {
            return false;
        }
        if (senha.length() < 8) {
            return false;
        }

        boolean temNumero = false;
        for (char c : senha.toCharArray()) {
            if (Character.isDigit(c)) {
                temNumero = true;
                break;
            }
        }

        return temNumero;
    }
}