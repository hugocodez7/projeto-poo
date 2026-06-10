package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RestauranteRepository {
    private static final String CAMINHO = "src/main/resources/data/restaurante.json";
    private Gson gson = GsonUtil.getInstancia();

    public void salvar(Restaurante restaurante) {
        try {
            Files.createDirectories(Paths.get("src/main/resources/data"));
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
        return Files.exists(Paths.get(CAMINHO));
    }
}