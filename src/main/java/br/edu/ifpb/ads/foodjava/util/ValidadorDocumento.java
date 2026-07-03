package br.edu.ifpb.ads.foodjava.util;

public class ValidadorDocumento {

    public static boolean validarCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }

        int primeiro = (soma * 10 % 11) % 10;

        if (primeiro != (cpf.charAt(9) - '0')) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }

        int segundo = (soma * 10 % 11) % 10;

        return segundo == (cpf.charAt(10) - '0');
    }

    public static boolean validarCNPJ(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma = 0;

        for (int i = 0; i < 12; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos1[i];
        }

        int primeiro = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        if (primeiro != (cnpj.charAt(12) - '0')) {
            return false;
        }

        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        soma = 0;

        for (int i = 0; i < 13; i++) {
            soma += (cnpj.charAt(i) - '0') * pesos2[i];
        }

        int segundo = soma % 11 < 2 ? 0 : 11 - (soma % 11);

        return segundo == (cnpj.charAt(13) - '0');
    }
}