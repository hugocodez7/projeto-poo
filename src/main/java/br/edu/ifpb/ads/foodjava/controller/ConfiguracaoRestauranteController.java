package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.repository.ClienteRepository;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.repository.GerenteRepository;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import br.edu.ifpb.ads.foodjava.util.ValidadorDocumento;
import br.edu.ifpb.ads.foodjava.util.ValidadorSenha;
import br.edu.ifpb.ads.foodjava.util.ValidadorTelefone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfiguracaoRestauranteController {

    @FXML private TextField txNome;
    @FXML private TextField txCnpj;
    @FXML private TextField txEndereco;
    @FXML private TextField txTelefone;
    @FXML private TextField txCategoria;
    @FXML private TextField txEmail;
    @FXML private PasswordField txSenha;
    @FXML private Button btnVoltar;
    @FXML private Button btnEscolherLogo;
    @FXML private ImageView imgLogo;

    private String logoSelecionada;

    private final RestauranteRepository restauranteRepository = new RestauranteRepository();
    private final GerenteRepository gerenteRepository = new GerenteRepository();
    private final ClienteRepository clienteRepository = new ClienteRepository();

    @FXML
    public void initialize() {
        Restaurante restaurante = restauranteRepository.carregar();

        if (restaurante != null) {
            txNome.setText(restaurante.getNome());
            txCnpj.setText(restaurante.getCnpj());
            txEndereco.setText(restaurante.getEndereco());
            txTelefone.setText(restaurante.getTelefone());
            txCategoria.setText(restaurante.getCategoriaCulinaria());
            txEmail.setText(restaurante.getEmail());

            logoSelecionada = restaurante.getLogo();
            carregarLogo(logoSelecionada);
        }
    }

    @FXML
    public void salvar() {
        String nome = txNome.getText().trim();
        String cnpj = txCnpj.getText().trim();
        String endereco = txEndereco.getText().trim();
        String telefone = txTelefone.getText().trim();
        String categoria = txCategoria.getText().trim();
        String email = txEmail.getText().trim();
        String senha = txSenha.getText().trim();

        if (nome.isEmpty() || cnpj.isEmpty() || endereco.isEmpty()
                || telefone.isEmpty() || categoria.isEmpty() || email.isEmpty()) {
            mostrarErro("Todos os campos sao obrigatorios.");
            return;
        }

        if (!ValidadorDocumento.validarCNPJ(cnpj)) {
            mostrarErro("CNPJ invalido.");
            return;
        }

        if (!ValidadorTelefone.validar(telefone)) {
            mostrarErro("Telefone invalido.");
            return;
        }

        if (!senha.isEmpty() && !ValidadorSenha.validar(senha)) {
            mostrarErro("Senha deve ter ao menos 8 caracteres e um numero.");
            return;
        }

        try {
            Cliente clienteComMesmoEmail = clienteRepository.buscarPorEmail(email);

            if (clienteComMesmoEmail != null) {
                mostrarErro("Este e-mail já está cadastrado como cliente.");
                return;
            }
        } catch (Exception e) {
            mostrarErro("Erro ao verificar e-mail.");
            return;
        }

        Restaurante restauranteAtual = restauranteRepository.carregar();
        String logoFinal = logoSelecionada;

        if (logoFinal == null && restauranteAtual != null) {
            logoFinal = restauranteAtual.getLogo();
        }

        Restaurante restaurante = new Restaurante(nome, cnpj, endereco, telefone, categoria, email, logoFinal);
        restauranteRepository.salvar(restaurante);
        atualizarGerente(nome, email, senha, telefone);

        mostrarSucesso("Configuracoes salvas com sucesso!");
    }

    @FXML
    public void escolherLogo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolher logotipo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.jpeg", "*.png")
        );

        Stage stage = (Stage) btnEscolherLogo.getScene().getWindow();
        File arquivo = fileChooser.showOpenDialog(stage);

        if (arquivo != null) {
            copiarLogo(arquivo);
        }
    }

    private void copiarLogo(File arquivoOrigem) {
        try {
            File pastaDestino = new File("uploads/logos");

            if (!pastaDestino.exists() && !pastaDestino.mkdirs()) {
                mostrarErro("Erro ao criar pasta de logotipos.");
                return;
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

            logoSelecionada = "uploads/logos/" + nomeUnico;
            carregarLogo(logoSelecionada);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao copiar logotipo.");
        }
    }

    private void carregarLogo(String caminhoLogo) {
        try {
            if (caminhoLogo == null || caminhoLogo.isBlank()) {
                imgLogo.setImage(null);
                return;
            }

            File arquivo = new File(caminhoLogo);

            if (arquivo.exists()) {
                imgLogo.setImage(new Image(arquivo.toURI().toString()));
            } else {
                imgLogo.setImage(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizarGerente(String nome, String email, String senha, String telefone) {
        try {
            Gerente gerenteAtual = gerenteRepository.buscarPrimeiro();

            if (gerenteAtual == null) {
                return;
            }

            String senhaFinal = senha.isEmpty() ? gerenteAtual.getSenha() : senha;
            Gerente gerenteAtualizado = new Gerente(gerenteAtual.getId(), nome, email, senhaFinal, telefone);

            List<Gerente> lista = new ArrayList<>();
            lista.add(gerenteAtualizado);
            gerenteRepository.salvar(lista);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarErro("Erro ao atualizar dados do gerente.");
        }
    }

    @FXML
    public void voltar() {
        trocarTela("/fxml/PainelGerente.fxml", "Painel do Gerente");
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
            mostrarErro("Erro ao abrir tela: " + caminhoFXML);
        }
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