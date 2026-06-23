package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.view.CardapioView;
import br.edu.ifpb.ads.foodjava.util.Sessao;
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
import java.util.List;
import java.util.Optional;

public class CardapioController {

    @FXML
    private TableView<ItemCardapio> tabelaCardapio;
    @FXML
    private TableColumn<ItemCardapio, String> colunaNome;
    @FXML
    private TableColumn<ItemCardapio, String> colunaCategoria;
    @FXML
    private TableColumn<ItemCardapio, Double> colunaPreco;
    @FXML
    private TableColumn<ItemCardapio, Boolean> colunaDisponivel;
    @FXML
    private TextField campoBusca;
    @FXML
    private Button adicionar;
    @FXML
    private Button editar;
    @FXML
    private Button remover;

    private List<ItemCardapio> itens;

    private CardapioView cardapioService;
    private ObservableList<ItemCardapio> listaObservavel;

    @FXML
    public void initialize() {
        try {
            cardapioService = new CardapioView();

            colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
            colunaDisponivel.setCellValueFactory(new PropertyValueFactory<>("disponivel"));

            colunaPreco.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Double preco, boolean empty) {
                    super.updateItem(preco, empty);
                    setText(empty || preco == null ? null : String.format("R$ %.2f", preco));
                }
            });

            colunaDisponivel.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(Boolean disponivel, boolean empty) {
                    super.updateItem(disponivel, empty);
                    setText(empty || disponivel == null ? null : (disponivel ? "Sim" : "Não"));
                }
            });

            controleDeAcesso();

            listaObservavel = FXCollections.observableArrayList(itens);

            tabelaCardapio.setItems(listaObservavel);

            FilteredList<ItemCardapio> listaFiltrada = new FilteredList<>(listaObservavel, item -> true);

            campoBusca.textProperty().addListener((obs, antes, depois) -> {
                listaFiltrada.setPredicate(item -> {
                    if (depois == null || depois.trim().isEmpty()) {
                        return true;
                    }

                    String busca = depois.toLowerCase().trim();

                    return item.getNome().toLowerCase().contains(busca)
                            || item.getCategoria().toString().toLowerCase().contains(busca);
                });
            });

            tabelaCardapio.setItems(listaFiltrada);

            tabelaCardapio.setPlaceholder(new Label("Nenhum item encontrado no cardápio."));

            editar.setDisable(true);
            remover.setDisable(true);

            tabelaCardapio.getSelectionModel().selectedItemProperty().addListener(
                    (obs, itemAntigo, itemNovo) -> {
                        boolean temSelecao = itemNovo != null;
                        editar.setDisable(!temSelecao);
                        remover.setDisable(!temSelecao);
                    }
            );

        } catch (ArquivoImportacaoException e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    public void controleDeAcesso() {
        Usuario usuarioLogado = Sessao.getUsuarioLogado();
        List<ItemCardapio> lista;

        if (usuarioLogado == null) {
            adicionar.setVisible(false);
            editar.setVisible(false);
            remover.setVisible(false);
        }

        if ("GERENTE".equals(usuarioLogado.getTipo())) {
            lista = cardapioService.listarTodos();
            adicionar.setVisible(true);
            editar.setVisible(true);
            remover.setVisible(true);

        } else {
            lista = cardapioService.listarDisponiveis();
            adicionar.setVisible(false);
            editar.setVisible(false);
            remover.setVisible(false);
        }
        itens = lista;
    }

    @FXML
    public void abrirFormularioAdicionar() {
        abrirFormulario(null, "Adicionar Item");
    }

    @FXML
    public void abrirFormularioEditar() {
        ItemCardapio selecionado = tabelaCardapio.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAviso("Selecione um item para editar.");
            return;
        }

        abrirFormulario(selecionado, "Editar Item");
    }

    private void abrirFormulario(ItemCardapio item, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/itemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setCardapioService(this.cardapioService);

            if (item != null) {
                controller.preencherParaEdicao(item);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            atualizarTabela();

        } catch (IOException e) {
            mostrarErro("Erro ao abrir formulário do item.");
            e.printStackTrace();
        } catch (ArquivoImportacaoException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void removerItem() {
        ItemCardapio selecionado = tabelaCardapio.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAviso("Selecione um item para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover item?");
        confirmacao.setContentText("Tem certeza que deseja remover \"" + selecionado.getNome() + "\"?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                cardapioService.removerItem(selecionado.getId());
                atualizarTabela();
            } catch (ItemNaoEncontradoException | ArquivoImportacaoException e) {
                mostrarErro("Erro ao remover item: " + e.getMessage());
            }
        }
    }

    private void atualizarTabela() throws ArquivoImportacaoException {
        listaObservavel.setAll(cardapioService.listarTodos());
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}