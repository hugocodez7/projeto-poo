package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.view.CardapioView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ItemFormController {

    @FXML
    private TextField nome;
    @FXML
    private TextArea descricao;
    @FXML
    private TextField campoPreco;
    @FXML
    private ComboBox<Categoria> categoria;
    @FXML
    private CheckBox disponivel;
    @FXML
    private Button salvar;
    @FXML
    private Button cancelar;
    @FXML
    private ImageView imagem;
    @FXML
    private Button escolherImagem;

    private CardapioView cardapioService;
    private ItemCardapio itemEditando;
    private String caminhoImagemSelecionada;

    @FXML
    public void initialize() {
        try {

            categoria.setItems(FXCollections.observableArrayList(Categoria.values()));
            disponivel.setSelected(true);

            carregarImagem(null);

        } catch (Exception e) {
            mostrarErro("Erro ao carregar serviço do cardápio: " + e.getMessage());
        }
    }

    public void setCardapioService(CardapioView cardapioService) {
        this.cardapioService = cardapioService;
    }

    public void preencherParaEdicao(ItemCardapio item) {
        this.itemEditando = item;
        this.caminhoImagemSelecionada = item.getCaminhoImagem();

        nome.setText(item.getNome());
        descricao.setText(item.getDescricao());
        campoPreco.setText(String.valueOf(item.getPreco()));
        categoria.setValue(item.getCategoria());
        disponivel.setSelected(item.isDisponivel());

        carregarImagem(item.getCaminhoImagem());
    }

    @FXML
    public void salvar() {
        String nomeDigitado = nome.getText().trim();
        String descricaoDigitada = descricao.getText().trim();
        String precoDigitado = campoPreco.getText().trim();
        Categoria categoriaSelecionada = categoria.getValue();

        if (nomeDigitado.isEmpty()) {
            mostrarErro("O nome não pode estar vazio.");
            return;
        }

        if (precoDigitado.isEmpty()) {
            mostrarErro("O preço não pode estar vazio.");
            return;
        }

        if (categoriaSelecionada == null) {
            mostrarErro("Selecione uma categoria.");
            return;
        }

        double preco;

        try {
            preco = Double.parseDouble(precoDigitado.replace(",", "."));

            if (preco <= 0) {
                mostrarErro("O preço deve ser maior que zero.");
                return;
            }

        } catch (NumberFormatException e) {
            mostrarErro("Digite um preço válido.");
            return;
        }

        try {
            if (itemEditando == null) {
                ItemCardapio novoItem = new ItemCardapio(
                        null,
                        nomeDigitado,
                        descricaoDigitada,
                        caminhoImagemSelecionada,
                        preco,
                        categoriaSelecionada,
                        disponivel.isSelected()
                );

                cardapioService.adicionarItem(novoItem);

            } else {
                itemEditando.setNome(nomeDigitado);
                itemEditando.setDescricao(descricaoDigitada);
                itemEditando.setCaminhoImagem(caminhoImagemSelecionada);
                itemEditando.setPreco(preco);
                itemEditando.setCategoria(categoriaSelecionada);
                itemEditando.setDisponivel(disponivel.isSelected());

                cardapioService.editarItem(itemEditando);
            }

            fecharJanela();

        } catch (ItemNaoEncontradoException e) {
            mostrarErro("Erro ao salvar item: " + e.getMessage());
        }
    }

    @FXML
    public void cancelar() {
        fecharJanela();
    }

    @FXML
    public void escolherImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher imagem do item");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
        );

        Stage stage = (Stage) escolherImagem.getScene().getWindow();
        File arquivoEscolhido = fileChooser.showOpenDialog(stage);

        if (arquivoEscolhido != null) {
            copiarImagem(arquivoEscolhido);
        }
    }

    private void copiarImagem(File arquivoOrigem) {
        try {
            File pastaDestino = new File("src/main/resources/images/cardapio");

            if (!pastaDestino.exists()) {
                pastaDestino.mkdirs();
            }

            String nomeOriginal = arquivoOrigem.getName();
            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String nomeUnico = UUID.randomUUID() + extensao;

            File arquivoDestino = new File(pastaDestino, nomeUnico);

            Files.copy(
                    arquivoOrigem.toPath(),
                    arquivoDestino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            caminhoImagemSelecionada = nomeUnico;
            carregarImagem(nomeUnico);

        } catch (Exception e) {
            mostrarErro("Erro ao copiar imagem.");
            e.printStackTrace();
        }
    }

    private void carregarImagem(String nomeArquivo) {
        try {
            Image img;

            if (nomeArquivo == null || nomeArquivo.isBlank()) {
                img = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
            } else {
                File arquivo = new File("src/main/resources/images/cardapio", nomeArquivo);

                if (arquivo.exists()) {
                    img = new Image(arquivo.toURI().toString());
                } else {
                    img = new Image(getClass().getResourceAsStream("/images/placeholder.png"));
                }
            }

            imagem.setImage(img);
            imagem.setPreserveRatio(true);
            imagem.setFitWidth(200);
            imagem.setFitHeight(140);

        } catch (Exception e) {
            mostrarErro("Erro ao carregar imagem.");
            e.printStackTrace();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de validação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}