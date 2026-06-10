package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardapioRepository implements Persistivel<ItemCardapio> {

    private static final String CAMINHO = "src/main/resources/data/cardapio.json";
    private Gson gson = GsonUtil.getInstancia();


    public void salvar(List<ItemCardapio> lista) {
        try (FileWriter writer = new FileWriter(CAMINHO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ItemCardapio> carregar() {
        try (FileReader reader = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<List<ItemCardapio>>(){}.getType();
            return gson.fromJson(reader, tipo);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}