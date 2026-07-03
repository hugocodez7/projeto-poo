package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Vai salvar e carregar as informações do restaurante em um arquivo JSON. Como so existe um restaurante
configurado no sistema, ele salva um unico objeto, diferente dos outros repositorios que salvam listas.
O metodo "existe()" verifica se o restaurante ja foi configurado para
decidir se o sistema abre a tela de configuração inicial ou a tela de login. */

public class RestauranteRepository {

    private static final String CAMINHO = "data/restaurante.json";
    private final Gson gson = GsonUtil.getInstancia();

    public void salvar(Restaurante restaurante) {
        try {
            Files.createDirectories(Paths.get("data"));

            try (FileWriter guardarArquivo = new FileWriter(CAMINHO)) {
                gson.toJson(restaurante, guardarArquivo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Restaurante carregar() {
        try (FileReader lerArquivo = new FileReader(CAMINHO)) {
            return gson.fromJson(lerArquivo, Restaurante.class);

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean existe() {
        try {
            return Files.exists(Paths.get(CAMINHO))
                    && Files.size(Paths.get(CAMINHO)) > 0;

        } catch (IOException e) {
            return false;
        }
    }
}