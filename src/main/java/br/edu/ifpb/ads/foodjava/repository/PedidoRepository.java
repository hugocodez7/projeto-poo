package br.edu.ifpb.ads.foodjava.repository;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
Esse repositorio salva e carrega os pedidos.
Ele também permite adicionar novos pedidos e
filtrar os pedidos pelo status, o que ajuda no painel do gerente
*/

public class PedidoRepository implements Persistivel<Pedido> {

    private static final String CAMINHO = "data/pedidos.json";
    private final Gson gson = GsonUtil.getInstancia();

    @Override
    public void salvar(List<Pedido> lista) {
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
    public List<Pedido> carregar() throws ArquivoImportacaoException {
        File arquivo = new File(CAMINHO);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader lerArquivo = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<Pedido>>() {}.getType();

            List<Pedido> lista = gson.fromJson(lerArquivo, tipo);

            if (lista == null) {
                return new ArrayList<>();
            }

            return lista;

        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("pedidos.json", e);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void adicionar(Pedido pedido) throws ArquivoImportacaoException {
        List<Pedido> lista = carregar();
        lista.add(pedido);
        salvar(lista);
    }

    public List<Pedido> buscarPorStatus(String status) throws ArquivoImportacaoException {
        List<Pedido> resultado = new ArrayList<>();

        for (Pedido pedido : carregar()) {
            if (pedido.getStatus().name().equalsIgnoreCase(status)) {
                resultado.add(pedido);
            }
        }

        return resultado;
    }

    public int getTotalPedidos() throws ArquivoImportacaoException {
        return carregar().size();
    }

    public boolean existePedidoAbertoComItem(String nomeItem) throws ArquivoImportacaoException {
        for (Pedido pedido : carregar()) {
            boolean pedidoFechado = pedido.getStatus() == StatusPedido.ENTREGUE || pedido.getStatus() == StatusPedido.CANCELADO;

            if (pedidoFechado || pedido.getItens() == null) {
                continue;
            }

            for (ItemPedido item : pedido.getItens()) {
                if (item.getNome() != null && item.getNome().equalsIgnoreCase(nomeItem)) {
                    return true;
                }
            }
        }

        return false;
    }
}