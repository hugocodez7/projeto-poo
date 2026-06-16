package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.service.CardapioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class CardapioController {

    @FXML private TableView<ItemCardapio> tabelaCardapio;
    @FXML private TableColumn<ItemCardapio, String> colunaNome;
    @FXML private TableColumn<ItemCardapio, String> colunaCategoria;
    @FXML private TableColumn<ItemCardapio, Double> colunaPreco;
    @FXML private TableColumn<ItemCardapio, Boolean> colunaDisponivel;
    @FXML private TextField campoBusca;
    @FXML private Button adicionar;
    @FXML private Button editar;
    @FXML private Button remover;

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

        editar.setDisable(true);
        remover.setDisable(true);

        tabelaCardapio.getSelectionModel().selectedItemProperty().addListener(
                (obs, itemAntigo, itemNovo) -> {
                    boolean temSelecao = (itemNovo != null);
                    editar.setDisable(!temSelecao);
                    remover.setDisable(!temSelecao);
                }
        );
    }

    @FXML
    public void abrirFormularioAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/itemForm.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Adicionar Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            listaObservavel.setAll(cardapioService.listarTodos());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirFormularioEditar() {
        ItemCardapio selecionado = tabelaCardapio.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Slecione um item para editar.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/itemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.preencherParaEdicao(selecionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            listaObservavel.setAll(cardapioService.listarTodos());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removerItem() throws ItemNaoEncontradoException {
        ItemCardapio selecionado = tabelaCardapio.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setContentText("Confirmar remoção");
        confirmacao.setHeaderText("Remover item?");
        confirmacao.setContentText("Tem certeza que deseja remover \"" + selecionado.getNome() + "\"?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            cardapioService.removerItem(selecionado.getId());
            listaObservavel.setAll(cardapioService.listarTodos());
        }
    }
}