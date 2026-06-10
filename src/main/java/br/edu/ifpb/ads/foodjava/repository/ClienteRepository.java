package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Cliente;
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

public class ClienteRepository implements Persistivel<Cliente>{

    private static final String CAMINHO = "src/main/resources/data/clientes.json";
    private Gson gson = GsonUtil.getInstancia();

    public void salvar(List<Cliente> lista) {
        try (FileWriter writer = new FileWriter(CAMINHO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> carregar() throws ArquivoImportacaoException {
        try (FileReader reader = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<List<Cliente>>() {}.getType();
            return gson.fromJson(reader, tipo);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("cliente.json", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
