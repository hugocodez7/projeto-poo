package br.edu.ifpb.ads.foodjava.service;

import java.util.ArrayList;
import java.util.List;

public class ResultadoImportacao {
    private int importados;
    private final List<String> erros = new ArrayList<>();

    public void adicionarImportado() {
        importados++;
    }

    public void adicionarErro(String erro) {
        erros.add(erro);
    }

    public int getImportados() {
        return importados;
    }

    public List<String> getErros() {
        return erros;
    }

    public boolean temErros() {
        return !erros.isEmpty();
    }

    public String gerarRelatorio() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("Itens importados: ").append(importados).append("\n");

        if (erros.isEmpty()) {
            relatorio.append("Nenhum erro encontrado.");
        } else {
            relatorio.append("\nErros encontrados:\n");
            for (String erro : erros) {
                relatorio.append("- ").append(erro).append("\n");
            }
        }

        return relatorio.toString();
    }
}