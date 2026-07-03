package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
Vai salvar e carregar os itens do cardapio em JSON.
Quando o gerente adiciona, edita ou remove um item, a lista é atualizada e salva novamente
*/

public class CardapioRepository implements Persistivel<ItemCardapio> {
    private static final String CAMINHO = "data/cardapio.json";
    private final Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<ItemCardapio> lista) {
        File arquivo = new File(CAMINHO);
        File pasta = arquivo.getParentFile();

        if (pasta != null && !pasta.exists()) {
            pasta.mkdirs();
        }

        try (FileWriter guardarArquivo = new FileWriter(arquivo)) {
            gson.toJson(lista, guardarArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemCardapio> carregar() throws ArquivoImportacaoException {
        File arquivo = new File(CAMINHO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader lerArquivo = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<ItemCardapio>>() {}.getType();

            List<ItemCardapio> lista = gson.fromJson(lerArquivo, tipo);

            if (lista == null) {
                return new ArrayList<>();
            }

            return lista;

        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("cardapio.json", e);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}