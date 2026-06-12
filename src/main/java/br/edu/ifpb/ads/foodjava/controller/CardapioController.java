package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.service.CardapioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CardapioController {

    @FXML private TableView<ItemCardapio> tabelaCardapio;
    @FXML private TableColumn<ItemCardapio, String> colunaNome;
    @FXML private TableColumn<ItemCardapio, String> colunaCategoria;
    @FXML private TableColumn<ItemCardapio, Double> colunaPreco;
    @FXML private TableColumn<ItemCardapio, Boolean> colunaDisponivel;
    @FXML private TextField campoBusca;
    @FXML private Button Adicionar;
    @FXML private Button Editar;
    @FXML private Button Remover;

    private CardapioService cardapioService;
    private ObservableList<ItemCardapio> listaObservavel;

    public void initialize() throws ArquivoImportacaoException {
        cardapioService = new CardapioService();

        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        colunaCategoria.setCellValueFactory(
                new PropertyValueFactory<>("categoria"));

        colunaPreco.setCellValueFactory(
                new PropertyValueFactory<>("preco"));

        colunaPreco.setCellFactory(
                column -> new TableCell<ItemCardapio, Double>() {
                    @Override
                    protected void updateItem(Double preco, boolean empty) {
                        super.updateItem(preco, empty);
                        if (empty || preco == null) {
                            setText(null);
                        } else {
                            setText(String.format("R$ %.2f", preco));
                        }
                    }
                });

        colunaDisponivel.setCellValueFactory(
                new PropertyValueFactory<>("disponivel"));
        colunaDisponivel.setCellFactory(
                column -> new TableCell<ItemCardapio, Boolean>() {
                    @Override
                    protected void updateItem(Boolean disponivel, boolean empty) {
                        super.updateItem(disponivel, empty);
                        if (empty || disponivel == null) {
                            setText(null);
                        } else {
                            setText(disponivel ? "Sim" : "Não");
                        }
                    }
                }
            );

        listaObservavel = FXCollections.observableArrayList(
                cardapioService.listarTodos()
        );
        tabelaCardapio.setItems(listaObservavel);

        FilteredList<ItemCardapio> listaFiltrada =
                new FilteredList<>(listaObservavel, p -> true);

        campoBusca.textProperty().addListener((obs, antes, depois) -> {
            listaFiltrada.setPredicate(item -> {
                if (depois == null || depois.isEmpty()) {
                    return true;
                }
                String busca = depois.toLowerCase();
                return item.getNome().toLowerCase().contains(busca);
            });
        });

        tabelaCardapio.setItems(listaFiltrada);
    }
}