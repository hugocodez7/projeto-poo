package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardapioRepository implements Persistivel<ItemCardapio> {

    private static final String CAMINHO = "src/main/resources/data/cardapio.json";

    private final Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<ItemCardapio> lista) {
        File arquivo = new File(CAMINHO);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try (FileWriter escritor = new FileWriter(arquivo)) {
            GsonUtil.getInstancia().toJson(lista, escritor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemCardapio> carregar() throws ArquivoImportacaoException {
        File arquivo = new File(CAMINHO);

        try (FileReader leitor = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<ItemCardapio>>() {}.getType();
            List<ItemCardapio> lista = GsonUtil.getInstancia().fromJson(leitor, tipo);

            if (lista == null) {
                return new ArrayList<>();
            }
            return lista;

        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new ArquivoImportacaoException("cardapio.json", e);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}