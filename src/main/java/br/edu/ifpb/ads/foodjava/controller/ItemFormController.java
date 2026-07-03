package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ItemNaoEncontradoException;
import br.edu.ifpb.ads.foodjava.model.Categoria;
import br.edu.ifpb.ads.foodjava.model.ItemCardapio;
import br.edu.ifpb.ads.foodjava.service.CardapioService;
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
    private ImageView imagem;
    @FXML
    private Button salvar;
    @FXML
    private Button escolherImagem;

    private CardapioService cardapioService;
    private ItemCardapio itemEditando;
    private String caminhoImagemSelecionada;

    @FXML
    public void initialize() {
        categoria.setItems(FXCollections.observableArrayList(Categoria.values()));
        disponivel.setSelected(true);
        carregarImagem(null);
    }

    public void setCardapioService(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public void preencherParaEdicao(ItemCardapio item) {
        itemEditando = item;
        caminhoImagemSelecionada = item.getCaminhoImagem();
        nome.setText(item.getNome());
        descricao.setText(item.getDescricao());
        campoPreco.setText(String.valueOf(item.getPreco()));
        categoria.setValue(item.getCategoria());
        disponivel.setSelected(item.isDisponivel());
        carregarImagem(caminhoImagemSelecionada);
    }

    @FXML
    public void salvar() {
        String nomeDigitado = nome.getText().trim();
        String descricaoDigitada = descricao.getText().trim();
        String precoDigitado = campoPreco.getText().trim();
        Categoria categoriaSelecionada = categoria.getValue();

        if (nomeDigitado.isEmpty() || precoDigitado.isEmpty() || categoriaSelecionada == null) {
            mostrarErro("Preencha nome, preço e categoria.");
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
                ItemCardapio novoItem = new ItemCardapio(null, nomeDigitado, descricaoDigitada, caminhoImagemSelecionada, preco, categoriaSelecionada, disponivel.isSelected()
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
            mostrarErro("Item não encontrado.");
        } catch (Exception e) {
            mostrarErro("Erro ao salvar item.");
            e.printStackTrace();
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png"));

        Stage stage = (Stage) escolherImagem.getScene().getWindow();
        File arquivo = fileChooser.showOpenDialog(stage);

        if (arquivo != null) {
            copiarImagem(arquivo);
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

            Files.copy(arquivoOrigem.toPath(), arquivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING
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
            e.printStackTrace();
        }
    }

    private void fecharJanela() {
        Stage stage = (Stage) salvar.getScene().getWindow();
        stage.close();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}