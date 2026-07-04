package br.edu.ifpb.ads.foodjava.service;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import br.edu.ifpb.ads.foodjava.exception.ItemVinculadoException;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;
import br.edu.ifpb.ads.foodjava.util.GsonUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardapioService {

    private final CardapioRepository repository;
    private final List<ItemCardapio> lista;
    private final PedidoRepository pedidoRepository = new PedidoRepository();

    public CardapioService() throws ArquivoImportacaoException {
        this.repository = new CardapioRepository();
        this.lista = repository.carregar();

        preencherIdsFaltando();
    }

    public void adicionarItem(ItemCardapio item) {
        item.setId(UUID.randomUUID().toString());
        lista.add(item);
        repository.salvar(lista);
    }

    public void editarItem(ItemCardapio itemAtualizado) throws ItemNaoEncontradoException {
        ItemCardapio itemExistente = buscarPorId(itemAtualizado.getId());
        itemExistente.setNome(itemAtualizado.getNome());
        itemExistente.setDescricao(itemAtualizado.getDescricao());
        itemExistente.setCaminhoImagem(itemAtualizado.getCaminhoImagem());
        itemExistente.setPreco(itemAtualizado.getPreco());
        itemExistente.setCategoria(itemAtualizado.getCategoria());
        itemExistente.setDisponivel(itemAtualizado.isDisponivel());

        repository.salvar(lista);
    }

    public void removerItem(String id)
            throws ItemNaoEncontradoException, ItemVinculadoException, ArquivoImportacaoException {

        ItemCardapio item = buscarPorId(id);

        if (pedidoRepository.existePedidoAbertoComItem(item.getNome())) {
            throw new ItemVinculadoException(
                    "Este item está vinculado a um pedido em aberto e não pode ser excluído."
            );
        }

        lista.remove(item);
        repository.salvar(lista);
    }

    public void importarDeArquivo(File arquivo) throws ArquivoImportacaoException {
        try (FileReader leitor = new FileReader(arquivo)) {
            Type tipo = new TypeToken<List<ItemCardapio>>() {}.getType();

            List<ItemCardapio> itensImportados = GsonUtil.getInstancia().fromJson(leitor, tipo);

            if (itensImportados == null || itensImportados.isEmpty()) {
                throw new ArquivoImportacaoException("O arquivo está vazio ou sem itens válidos.");
            }

            for (ItemCardapio item : itensImportados) {
                item.setId(UUID.randomUUID().toString());
                lista.add(item);
            }

            repository.salvar(lista);

        } catch (JsonSyntaxException e) {
            throw new ArquivoImportacaoException("Arquivo JSON inválido ou corrompido.");

        } catch (IOException e) {
            throw new ArquivoImportacaoException("Erro ao ler o arquivo.");
        }
    }

    public ItemCardapio buscarPorId(String id) throws ItemNaoEncontradoException {
        for (ItemCardapio item : lista) {
            if (item.getId() != null && item.getId().equals(id)) {
                return item;
            }
        }

        throw new ItemNaoEncontradoException();
    }

    public List<ItemCardapio> listarTodos() {
        return new ArrayList<>(lista);
    }

    public List<ItemCardapio> listarDisponiveis() {
        List<ItemCardapio> disponiveis = new ArrayList<>();

        for (ItemCardapio item : lista) {
            if (item.isDisponivel()) {
                disponiveis.add(item);
            }
        }

        return disponiveis;
    }

    private void preencherIdsFaltando() {
        boolean alterou = false;

        for (ItemCardapio item : lista) {
            if (item.getId() == null || item.getId().isBlank()) {
                item.setId(UUID.randomUUID().toString());
                alterou = true;
            }
        }

        if (alterou) {
            repository.salvar(lista);
        }
    }
}