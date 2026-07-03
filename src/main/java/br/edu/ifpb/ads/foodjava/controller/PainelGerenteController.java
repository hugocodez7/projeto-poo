package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Pedido;
import br.edu.ifpb.ads.foodjava.model.StatusPedido;
import br.edu.ifpb.ads.foodjava.repository.PedidoRepository;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PainelGerenteController {

    @FXML private ComboBox<String> comboStatus;
    @FXML private Button btnSair;

    @FXML private TableView<Pedido> tabelaPedidos;
    @FXML private TableColumn<Pedido, Integer> colId;
    @FXML private TableColumn<Pedido, String> colData;
    @FXML private TableColumn<Pedido, Double> colValor;
    @FXML private TableColumn<Pedido, StatusPedido> colStatus;

    @FXML private Label lblTotalPedidos;
    @FXML private Label lblFaturamento;

    private final PedidoRepository pedidoRepository = new PedidoRepository();

    @FXML
    public void initialize() {
        configurarTabela();
        configurarComboBox();
        carregarPedidos();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colData.setCellValueFactory(cellData -> {
            Pedido pedido = cellData.getValue();

            if (pedido.getDataHora() == null) {
                return new SimpleStringProperty("");
            }

            String dataFormatada = pedido.getDataHora()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            return new SimpleStringProperty(dataFormatada);
        });

        colValor.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);

                if (vazio || valor == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", valor));
                }
            }
        });
    }

    private void configurarComboBox() {
        comboStatus.getItems().clear();
        comboStatus.getItems().add("TODOS");

        for (StatusPedido status : StatusPedido.values()) {
            comboStatus.getItems().add(status.name());
        }

        comboStatus.getSelectionModel().select("TODOS");
    }

    @FXML
    public void carregarPedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.carregar();
            atualizarTabela(pedidos);

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao carregar pedidos.");
        }
    }

    @FXML
    public void filtrarPedidos() {
        String statusSelecionado = comboStatus.getValue();

        try {
            List<Pedido> pedidos;

            if (statusSelecionado == null || statusSelecionado.equals("TODOS")) {
                pedidos = pedidoRepository.carregar();
            } else {
                pedidos = pedidoRepository.buscarPorStatus(statusSelecionado);
            }

            atualizarTabela(pedidos);

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao filtrar pedidos.");
        }
    }

    @FXML
    public void avancarStatus() {
        Pedido pedidoSelecionado = tabelaPedidos.getSelectionModel().getSelectedItem();

        if (pedidoSelecionado == null) {
            mostrarAviso("Selecione um pedido.");
            return;
        }

        try {
            pedidoSelecionado.avancarStatus();

            List<Pedido> pedidos = pedidoRepository.carregar();

            for (int i = 0; i < pedidos.size(); i++) {
                if (pedidos.get(i).getId() == pedidoSelecionado.getId()) {
                    pedidos.set(i, pedidoSelecionado);
                    break;
                }
            }

            pedidoRepository.salvar(pedidos);
            carregarPedidos();

            mostrarSucesso("Status atualizado com sucesso.");

        } catch (Exception e) {
            mostrarErro("Não foi possível avançar o status.");
        }
    }

    private void atualizarTabela(List<Pedido> pedidos) {
        tabelaPedidos.setItems(FXCollections.observableArrayList(pedidos));
        atualizarResumo(pedidos);
    }

    private void atualizarResumo(List<Pedido> pedidos) {
        int totalPedidos = pedidos.size();

        double faturamento = 0.0;

        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == StatusPedido.ENTREGUE) {
                faturamento += pedido.getValorTotal();
            }
        }

        lblTotalPedidos.setText("Total de pedidos: " + totalPedidos);
        lblFaturamento.setText(String.format("Faturamento: R$ %.2f", faturamento));
    }

    @FXML
    public void irParaCardapio() {
        trocarTela("/fxml/novaInterfaceCardapio.fxml", "FoodJava - Cardápio");
    }

    @FXML
    public void irParaConfiguracoes() {
        trocarTela("/fxml/ConfiguracaoRestaurante.fxml", "FoodJava - Configurações");
    }

    @FXML
    public void sair() {
        Sessao.encerrar();
        trocarTela("/fxml/Login.fxml", "FoodJava - Login");
    }

    private void trocarTela(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            Stage stage = (Stage) btnSair.getScene().getWindow();
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