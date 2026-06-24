package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.view.CardapioView;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardapioController {

    @FXML
    private FlowPane painelCards;
    @FXML
    private TextField campoBusca;
    @FXML
    private Button adicionar;
    @FXML
    private Button editar;
    @FXML
    private Button remover;
    @FXML
    private Button importar;

    private ItemCardapio itemSelecionado;
    private VBox cardSelecionadoVisual;
    private List<ItemCardapio> itens;
    private CardapioView cardapioView;

    @FXML
    public void initialize() {
        try {
            cardapioView = new CardapioView();

            controleDeAcesso();

            popularCards(itens);

            campoBusca.textProperty().addListener((obs, antes, depois) -> {
                if (depois == null || depois.trim().isEmpty()) {
                    popularCards(itens);
                    return;
                }

                String busca = depois.toLowerCase().trim();
                List<ItemCardapio> filtrados = new ArrayList<>();

                for (ItemCardapio item : itens) {
                    if (item.getNome().toLowerCase().contains(busca) ||
                            item.getCategoria().toString().toLowerCase().contains(busca)) {
                        filtrados.add(item);
                    }
                }
                popularCards(filtrados);
            });

            editar.setDisable(true);
            remover.setDisable(true);

        } catch (ArquivoImportacaoException e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar cardápio: " + e.getMessage());
        }
    }

    private VBox criarCard(ItemCardapio item) {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(160);
        card.setStyle(estiloCardNormal());

        ImageView imageView = new ImageView();
        imageView.setFitWidth(130);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        try {
            if (item.getCaminhoImagem() == null) {
                InputStream stream = getClass().getResourceAsStream("/images/placeholder.png");
                if (stream != null) imageView.setImage(new Image(stream));
            } else {
                File imgFile = new File("src/main/resources/images/cardapio/" + item.getCaminhoImagem());
                if (imgFile.exists()) {
                    imageView.setImage(new Image(new FileInputStream(imgFile)));
                } else {
                    InputStream stream = getClass().getResourceAsStream("/images/placeholder.png");
                    if (stream != null) imageView.setImage(new Image(stream));
                }
            }
        } catch (FileNotFoundException e) {
            InputStream stream = getClass().getResourceAsStream("/images/placeholder.png");
            if (stream != null) imageView.setImage(new Image(stream));
        }

        Label labelNome = new Label(item.getNome());
        labelNome.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        labelNome.setWrapText(true);
        labelNome.setMaxWidth(130);

        Label labelPreco = new Label(String.format("R$ %.2f", item.getPreco()));
        labelPreco.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Label labelCategoria = new Label(item.getCategoria().toString());
        labelCategoria.setStyle(
                "-fx-background-color: #eaf4fb;" +
                        "-fx-text-fill: #2980b9;" +
                        "-fx-padding: 2px 8px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-font-size: 11px;"
        );

        card.getChildren().addAll(imageView, labelNome, labelPreco, labelCategoria);

        Usuario usuarioLogado = Sessao.getUsuarioLogado();

        if ("GERENTE".equals(usuarioLogado.getTipo())) {

            card.setOnMouseClicked(e -> {
                if (cardSelecionadoVisual != null) {
                    cardSelecionadoVisual.setStyle(estiloCardNormal());
                }
                itemSelecionado = item;
                cardSelecionadoVisual = card;
                card.setStyle(estiloCardSelecionado());

                editar.setDisable(false);
                remover.setDisable(false);

                if (e.getClickCount() == 2) {
                    abrirFormulario(item, "Editar Item");
                }
            });

            Button remover = new Button("Remover");
            remover.setStyle(
                    "-fx-background-color: #e74c3c;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 4px;" +
                            "-fx-font-size: 11px;" +
                            "-fx-cursor: hand;"
            );

            remover.setOnAction(e -> confirmarRemocao(item));
            card.getChildren().add(remover);

        } else {
            Button btnCarrinho = new Button("+ Carrinho");
            btnCarrinho.setStyle(
                    "-fx-background-color: #27ae60;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 4px;" +
                            "-fx-font-size: 11px;" +
                            "-fx-cursor: hand;"
            );

            btnCarrinho.setOnAction(e -> adicionarAoCarrinho(item));
            card.getChildren().add(btnCarrinho);
        }

        card.setOnMouseEntered(e -> {
            if (card != cardSelecionadoVisual) {
                card.setStyle(estiloCardHover());
            }
        });

        card.setOnMouseExited(e -> {
            if (card != cardSelecionadoVisual) {
                card.setStyle(estiloCardNormal());
            }
        });

        return card;
    }

    private void popularCards(List<ItemCardapio> lista) {
        painelCards.getChildren().clear();
        itemSelecionado = null;
        cardSelecionadoVisual = null;
        editar.setDisable(true);
        remover.setDisable(true);

        if (lista == null || lista.isEmpty()) {
            Label vazio = new Label("Nenhum item encontrado.");
            vazio.setStyle("-fx-text-fill: #aaa; -fx-font-size: 13px;");
            painelCards.getChildren().add(vazio);
            return;
        }

        for (ItemCardapio item : lista) {
            VBox card = criarCard(item);
            painelCards.getChildren().add(card);
        }
    }

    public void adicionarAoCarrinho(ItemCardapio item) {
        System.out.println("Adicionado ao carrinho: " + item.getNome());
        mostrarSucesso(item.getNome() + " adicionado ao carrinho!");
    }

    public void controleDeAcesso() {
        Usuario usuarioLogado = Sessao.getUsuarioLogado();

        if (usuarioLogado == null) {
            adicionar.setVisible(false);
            editar.setVisible(false);
            remover.setVisible(false);
            importar.setVisible(false);
            itens = new ArrayList<>();
            return;
        }
        if ("GERENTE".equals(usuarioLogado.getTipo())) {
            itens = cardapioView.listarTodos();
            adicionar.setVisible(true);
            editar.setVisible(true);
            remover.setVisible(true);
            importar.setVisible(true);
        } else {
            itens = cardapioView.listarDisponiveis();
            adicionar.setVisible(false);
            editar.setVisible(false);
            remover.setVisible(false);
            importar.setVisible(false);
        }
    }

    @FXML
    public void importarCardapio() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Cardápio");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo JSON", "*.json")
        );
        Stage stage = (Stage) importar.getScene().getWindow();
        File arquivo = fileChooser.showOpenDialog(stage);

        if (arquivo != null) {
            try {
                cardapioView.importarDeArquivo(arquivo);
                atualizarCards();
                mostrarSucesso("Cardápio importado com sucesso!");
            } catch (ArquivoImportacaoException e) {
                mostrarErro("Erro ao importar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void abrirFormularioAdicionar() {
        abrirFormulario(null, "Adicionar Item");
    }

    @FXML
    public void abrirFormularioEditar() {
        if (itemSelecionado == null) {
            mostrarAviso("Selecione um item para editar.");
            return;
        }
        abrirFormulario(itemSelecionado, "Editar Item");
    }

    private void abrirFormulario(ItemCardapio item, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/itemForm.fxml"));
            Parent root = loader.load();

            ItemFormController controller = loader.getController();
            controller.setCardapioService(this.cardapioView);

            if (item != null) {
                controller.preencherParaEdicao(item);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            atualizarCards();

        } catch (IOException e) {
            mostrarErro("Erro ao abrir formulário do item.");
            e.printStackTrace();
        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao atualizar após formulário: " + e.getMessage());
        }
    }

    @FXML
    public void removerItem() {
        if (itemSelecionado == null) {
            mostrarAviso("Selecione um item para remover.");
            return;
        }
        confirmarRemocao(itemSelecionado);
    }

    private void confirmarRemocao(ItemCardapio item) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação");
        confirmacao.setHeaderText("Remover item?");
        confirmacao.setContentText("Tem certeza que deseja remover \"" + item.getNome() + "\"?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                cardapioView.removerItem(item.getId());
                atualizarCards();
            } catch (ItemNaoEncontradoException | ArquivoImportacaoException e) {
                mostrarErro("Erro ao remover item: " + e.getMessage());
            }
        }
    }

    private void atualizarCards() throws ArquivoImportacaoException {
        controleDeAcesso();
        popularCards(itens);
    }

    private String estiloCardNormal() {
        return "-fx-background-color: white;" +
                "-fx-border-color: #e0e0e0;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 12px;" +
                "-fx-cursor: hand;";
    }

    private String estiloCardHover() {
        return "-fx-background-color: #f0f8ff;" +
                "-fx-border-color: #2980b9;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);";
    }

    private String estiloCardSelecionado() {
        return "-fx-background-color: #ddeeff;" +
                "-fx-border-color: #1a5276;" +
                "-fx-border-radius: 8px;" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 12px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);";
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

    private void mostrarAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}