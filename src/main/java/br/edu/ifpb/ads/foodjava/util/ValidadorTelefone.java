package br.edu.ifpb.ads.foodjava.util;

public class ValidadorTelefone {

    public static boolean validar(String telefone) {

        if (telefone == null || telefone.isBlank()) {
            return false;
        }

        telefone = telefone.replaceAll("[^0-9]", "");

        if (telefone.length() != 10 && telefone.length() != 11) {
            return false;
        }

        if (telefone.matches("(\\d)\\1{9,10}")) {
            return false;
        }

        return true;
    }
}