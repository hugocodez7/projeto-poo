package br.edu.ifpb.ads.foodjava.view;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardapioView {

    private CardapioRepository repository;

    private List<ItemCardapio> lista;

    public CardapioView() throws ArquivoImportacaoException {
        this.repository = new CardapioRepository();
        this.lista = repository.carregar();

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

    public void adicionarItem(ItemCardapio item) {
        item.setId(UUID.randomUUID().toString());
        lista.add(item);
        repository.salvar(lista);
    }

    public void removerItem(String id) throws ItemNaoEncontradoException {
        ItemCardapio item = buscarPorId(id);
        lista.remove(item);
        repository.salvar(lista);
    }

    public void editarItem(ItemCardapio itemAtualizado) throws ItemNaoEncontradoException {
        ItemCardapio itemExistente = buscarPorId(itemAtualizado.getId());
        itemExistente.setNome(itemAtualizado.getNome());
        itemExistente.setDescricao(itemAtualizado.getDescricao());
        itemExistente.setPreco(itemAtualizado.getPreco());
        itemExistente.setCategoria(itemAtualizado.getCategoria());
        itemExistente.setDisponivel(itemAtualizado.isDisponivel());
        repository.salvar(lista);
    }

    public ItemCardapio buscarPorId(String id) throws ItemNaoEncontradoException {
        for (ItemCardapio item : lista) {
            if (item.getId().equals(id)) {
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

    public List<ItemCardapio> listarPorCategoria(Categoria categoria) {
        List<ItemCardapio> resultado = new ArrayList<>();

        for (ItemCardapio item : lista) {
            if (item.getCategoria().equals(categoria)) {
                resultado.add(item);
            }
        }
        return resultado;
    }
}