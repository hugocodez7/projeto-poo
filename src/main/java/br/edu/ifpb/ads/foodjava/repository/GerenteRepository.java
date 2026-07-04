package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
Fica responsaavel por cuidar dos dados do gerente.
Ele permite carregar o gerente salvo e verificar o login com email e senha
*/

public class GerenteRepository implements Persistivel<Gerente> {

    private static final String CAMINHO = "data/gerente.json";
    private final Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<Gerente> lista) {
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
    public List<Gerente> carregar() throws ArquivoImportacaoException {
        File arquivo = new File(CAMINHO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader lerArquivo = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Gerente>>() {}.getType();

            List<Gerente> lista = gson.fromJson(lerArquivo, tipo);

            if (lista == null) {
                return new ArrayList<>();
            }

            return lista;

        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("gerente.json", e);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Gerente buscarPorEmail(String email) throws ArquivoImportacaoException {
        List<Gerente> lista = carregar();

        for (Gerente gerente : lista) {
            if (gerente.getEmail() != null && gerente.getEmail().equalsIgnoreCase(email)) {
                return gerente;
            }
        }

        return null;
    }

    public Gerente buscarPorEmailESenha(String email, String senha) throws ArquivoImportacaoException {
        List<Gerente> lista = carregar();

        for (Gerente gerente : lista) {
            if (gerente.getEmail() != null && gerente.getSenha() != null && gerente.getEmail().equalsIgnoreCase(email) && gerente.getSenha().equals(senha)) {
                return gerente;
            }
        }

        return null;
    }

    public Gerente buscarPrimeiro() throws ArquivoImportacaoException {
        List<Gerente> lista = carregar();

        if (lista.isEmpty()) {
            return null;
        }

        return lista.getFirst();
    }
}