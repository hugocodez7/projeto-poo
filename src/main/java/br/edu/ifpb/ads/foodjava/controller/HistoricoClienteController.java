package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.CancelamentoNaoPermitidoException;
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
import java.util.ArrayList;
import java.util.List;

public class HistoricoClienteController {

    @FXML private Button btnVoltar;
    @FXML private TableView<Pedido> tabelaHistorico;
    @FXML private TableColumn<Pedido, Integer> colId;
    @FXML private TableColumn<Pedido, String> colData;
    @FXML private TableColumn<Pedido, Double> colValor;
    @FXML private TableColumn<Pedido, StatusPedido> colStatus;
    @FXML private Label lblMensagem;

    private final PedidoRepository pedidoRepository = new PedidoRepository();

    @FXML
    public void initialize() {
        configurarTabela();
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

            String dataFormatada = pedido.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

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

    private void carregarPedidos() {
        try {
            List<Pedido> todosPedidos = pedidoRepository.carregar();
            List<Pedido> pedidosDoCliente = new ArrayList<>();

            String emailLogado = Sessao.getUsuarioLogado().getEmail();

            for (Pedido pedido : todosPedidos) {
                if (pedido.getEmailCliente() != null
                        && pedido.getEmailCliente().equalsIgnoreCase(emailLogado)) {
                    pedidosDoCliente.add(pedido);
                }
            }

            tabelaHistorico.setItems(FXCollections.observableArrayList(pedidosDoCliente));

            if (pedidosDoCliente.isEmpty()) {
                lblMensagem.setText("Você ainda não fez nenhum pedido.");
            } else {
                lblMensagem.setText("Total de pedidos: " + pedidosDoCliente.size());
            }

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao carregar pedidos.");
        }
    }

    @FXML
    public void cancelarPedido() {
        Pedido pedidoSelecionado = tabelaHistorico.getSelectionModel().getSelectedItem();

        if (pedidoSelecionado == null) {
            mostrarAviso("Selecione um pedido para cancelar.");
            return;
        }

        try {
            pedidoSelecionado.cancelarPedido();

            List<Pedido> pedidos = pedidoRepository.carregar();

            for (int i = 0; i < pedidos.size(); i++) {
                if (pedidos.get(i).getId() == pedidoSelecionado.getId()) {
                    pedidos.set(i, pedidoSelecionado);
                    break;
                }
            }

            pedidoRepository.salvar(pedidos);
            carregarPedidos();

            mostrarSucesso("Pedido cancelado com sucesso.");

        } catch (CancelamentoNaoPermitidoException e) {
            mostrarErro(e.getMessage());

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao salvar pedido.");
        }
    }

    @FXML
    public void voltar() {
        trocarTela("/fxml/novaInterfaceCardapio.fxml", "Cardápio");
    }

    @FXML
    public void sair() {
        Sessao.encerrar();
        trocarTela("/fxml/Login.fxml", "Login");
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

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
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