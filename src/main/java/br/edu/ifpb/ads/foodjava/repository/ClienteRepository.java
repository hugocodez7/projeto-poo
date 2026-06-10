package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.model.Cliente;
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

public class ClienteRepository implements Persistivel<Cliente>{
    private static final String CAMINHO = "src/main/resources/data/clientes.json";
    private Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<Cliente> lista) {
        try (FileWriter guardarArquivo = new FileWriter(CAMINHO)) {
            gson.toJson(lista, guardarArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cliente> carregar() {
        try (FileReader lerArquivo = new FileReader(CAMINHO)) {
            Type tipo = new TypeToken<List<Cliente>>() {}.getType();
            return gson.fromJson(lerArquivo, tipo);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void salvarCliente(Cliente cliente) {
        List<Cliente> lista = carregar();
        lista.add(cliente);
        salvar(lista);
    }

    public Cliente buscarPorEmail(String email) {
        return carregar().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public Cliente buscarPorCpf(String cpf) {
        return carregar().stream()
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }
}
