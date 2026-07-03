package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.CarrinhoVazioException;
import br.edu.ifpb.ads.foodjava.model.ItemPedido;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.service.CarrinhoService;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CarrinhoController {

    @FXML private Button btnVoltar;
    @FXML private Button btnFinalizarPedido;

    @FXML private TableView<ItemPedido> tabelaCarrinho;
    @FXML private TableColumn<ItemPedido, String> colItem;
    @FXML private TableColumn<ItemPedido, Integer> colQuantidade;
    @FXML private TableColumn<ItemPedido, Double> colPreco;
    @FXML private TableColumn<ItemPedido, Double> colSubtotal;

    @FXML private Label lblTotal;

    private final CarrinhoService carrinhoService = new CarrinhoService();
    private ObservableList<ItemPedido> listaObservavel;

    @FXML
    public void initialize() {
        configurarTabela();

        listaObservavel = FXCollections.observableArrayList();
        tabelaCarrinho.setItems(listaObservavel);
        tabelaCarrinho.setPlaceholder(new Label("Seu carrinho está vazio."));

        atualizarTela();
    }

    private void configurarTabela() {
        colItem.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnidade"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        colPreco.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(Double preco, boolean vazio) {
                super.updateItem(preco, vazio);
                setText(vazio || preco == null ? null : String.format("R$ %.2f", preco));
            }
        });

        colSubtotal.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(Double subtotal, boolean vazio) {
                super.updateItem(subtotal, vazio);
                setText(vazio || subtotal == null ? null : String.format("R$ %.2f", subtotal));
            }
        });
    }

    @FXML
    public void removerItemSelecionado() {
        ItemPedido itemSelecionado = tabelaCarrinho.getSelectionModel().getSelectedItem();

        if (itemSelecionado == null) {
            mostrarAviso("Selecione um item para remover.");
            return;
        }

        carrinhoService.removerItem(itemSelecionado);
        atualizarTela();
    }

    @FXML
    public void finalizarPedido() {
        try {
            String emailCliente = Sessao.getUsuarioLogado().getEmail();

            Pedido pedido = carrinhoService.finalizarPedido(emailCliente);

            atualizarTela();

            mostrarSucesso("Pedido #" + pedido.getId() + " realizado com sucesso!");
            trocarTela("/fxml/HistoricoCliente.fxml", "Histórico");

        } catch (CarrinhoVazioException e) {
            mostrarErro("O carrinho está vazio.");

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao salvar pedido.");
        }
    }

    private void atualizarTela() {
        if (listaObservavel == null) {
            return;
        }

        listaObservavel.setAll(carrinhoService.getItens());

        btnFinalizarPedido.setDisable(carrinhoService.estaVazio());

        lblTotal.setText(String.format(
                "Total: R$ %.2f",
                carrinhoService.calcularTotal()
        ));
    }

    @FXML
    public void voltar() {
        trocarTela("/fxml/novaInterfaceCardapio.fxml", "Cardápio");
    }

    private void trocarTela(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Stage stage = (Stage) btnVoltar.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle(titulo);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir tela.");
        }
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

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}