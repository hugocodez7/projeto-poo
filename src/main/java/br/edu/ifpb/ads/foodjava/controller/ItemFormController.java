package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ItemFormController {

    @FXML private TextField nome;
    @FXML private TextArea descricao;
    @FXML private TextField campoPreco;
    @FXML private ComboBox<Categoria> categoria;
    @FXML private CheckBox checkDisponivel;
    @FXML private Button salvar;
    @FXML private Button cancelar;
    @FXML private ImageView imagem;
    @FXML private Button escolherImagem;

    private CardapioService cardapioService;
    private ItemCardapio itemEditando;
    private String caminhoImagemSelecionada = null;


    @FXML
    public void initialize() throws ArquivoImportacaoException, FileNotFoundException {
        cardapioService = new CardapioService();

        categoria.setItems(
                FXCollections.observableArrayList(Categoria.values())
        );
        carregarImagem(null);
    }

    public void preencherParaEdicao(ItemCardapio item) throws FileNotFoundException {
        this.itemEditando = item;

        nome.setText(item.getNome());
        descricao.setText(item.getDescricao());
        campoPreco.setText(String.valueOf(item.getPreco()));
        categoria.setValue(item.getCategoria());
        checkDisponivel.setSelected(item.isDisponivel());

        carregarImagem((item.getCaminhoImagem()));
    }

    @FXML
    public void salvar() throws ItemNaoEncontradoException {

        if (nome.getText().isEmpty()) {
            mostrarErro("O nome não pode estar vazio.");
            return;
        }

        if (categoria.getValue() == null) {
            mostrarErro("Selecione uma categoria.");
            return;
        }

        double preco;

        try {
            preco = Double.parseDouble(campoPreco.getText());
            if (preco <= 0) {
                mostrarErro("O preço deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarErro("Digite um preço válido.");
            return;
        }

        if (itemEditando == null) {
            ItemCardapio novoItem = new ItemCardapio(
                    null,
                    nome.getText(),
                    descricao.getText(),
                    caminhoImagemSelecionada,
                    preco,
                    categoria.getValue(),
                    checkDisponivel.isSelected()
            );
            cardapioService.adicionarItem(novoItem);

        } else {
            itemEditando.setNome(nome.getText());
            itemEditando.setDescricao(descricao.getText());
            if (caminhoImagemSelecionada != null) {
                itemEditando.setCaminhoImagem(caminhoImagemSelecionada);
            }
            itemEditando.setPreco(preco);
            itemEditando.setCategoria(categoria.getValue());
            itemEditando.setDisponivel(checkDisponivel.isSelected());

            cardapioService.editarItem(itemEditando);
        }

        Stage stage = (Stage) salvar.getScene().getWindow();
        stage.close();
    }

    private void carregarImagem(String nomeArquivo) throws FileNotFoundException {
        try {
            InputStream stream;

            if (nomeArquivo == null) {
                stream = getClass().getResourceAsStream("/images/placeholder.png");
            } else {
                stream = new FileInputStream(
                        "src/main/resources/images/cardapio/" + nomeArquivo
                );
            }

            imagem.setImage(new Image(stream));
            imagem.setPreserveRatio(true);
            imagem.setFitWidth(200);
            imagem.setFitHeight(140);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void escolherImagem() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher imagem do item");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) escolherImagem.getScene().getWindow();
        File arquivoEscolhido = fileChooser.showOpenDialog(stage);

        if(arquivoEscolhido != null) {
            copiarImagem(arquivoEscolhido);
        }
    }

    private void copiarImagem(File arquivoOrigem) {
        try {
            File pastaDestino = new File("src/main/resources/images/cardapio/");
            if (!pastaDestino.exists()) {
                pastaDestino.mkdir();
            }
            String extensao = arquivoOrigem.getName()
                    .substring(arquivoOrigem.getName().lastIndexOf("."));
            String nomeUnico = UUID.randomUUID().toString() + extensao;

            File arquivoDestino = new File(pastaDestino, nomeUnico);
            Files.copy(
                    arquivoOrigem.toPath(),
                    arquivoDestino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            caminhoImagemSelecionada = nomeUnico;
            carregarImagem(nomeUnico);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de validação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}