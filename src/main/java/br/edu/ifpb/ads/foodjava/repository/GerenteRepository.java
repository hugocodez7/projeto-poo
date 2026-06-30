package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GerenteRepository implements Persistivel<Gerente> {
    private static final String CAMINHO = "src/main/resources/data/gerente.json";
    private Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<Gerente> lista) {
        try (FileWriter guardarArquivo = new FileWriter(CAMINHO)) {
            GsonUtil.getInstancia().toJson(lista, guardarArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Gerente> carregar() throws ArquivoImportacaoException {
        try (FileReader lerArquivo = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<List<Gerente>>() {
            }.getType();
            List<Gerente> lista = GsonUtil.getInstancia().fromJson(lerArquivo, tipo);
            if (lista == null) {
                return new ArrayList<>();
            }
            return lista;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("gerente.json", e);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Gerente buscarPorEmailESenha(String email, String senha) throws ArquivoImportacaoException {
        List<Gerente> lista = carregar();
        for (Gerente gerente : lista) {
            if (gerente.getEmail().equals(email) &&
            gerente.getSenha().equals(senha)) {
                return gerente;
            }
        }
        return null;
    }

    public Gerente buscarPrimeiro() throws ArquivoImportacaoException {
        List<Gerente> lista = carregar();
        if(lista.isEmpty()) {
            return null;
        }
        return lista.getFirst();
    }
}