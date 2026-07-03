package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.DocumentoInvalidoException;
import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;
import br.edu.ifpb.ads.foodjava.exception.UsuarioDuplicadoException;
import br.edu.ifpb.ads.foodjava.util.ValidadorTelefone;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroClienteController {

    @FXML private TextField txNome;
    @FXML private TextField txEmail;
    @FXML private PasswordField txSenha;
    @FXML private TextField txCpf;
    @FXML private TextField txTelefone;
    @FXML private TextField txEndereco;
    @FXML private Button btnCadastrar;
    @FXML private Button btnVoltar;

    private final AuthController authController = new AuthController();

    @FXML
    public void cadastrar() {
        String nome = txNome.getText().trim();
        String email = txEmail.getText().trim();
        String senha = txSenha.getText().trim();
        String cpf = txCpf.getText().trim();
        String telefone = txTelefone.getText().trim();
        String endereco = txEndereco.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || endereco.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        if (!ValidadorTelefone.validar(telefone)) {
            mostrarErro("Telefone inválido. Use um telefone com 10 ou 11 dígitos.");
            return;
        }

        try {
            authController.cadastrarCliente(nome, email, senha, cpf, telefone, endereco);
            mostrarSucesso("Cliente cadastrado com sucesso!");
            voltar();

        } catch (UsuarioDuplicadoException | SenhaInvalidaException | DocumentoInvalidoException e) {
            mostrarErro(e.getMessage());

        } catch (ArquivoImportacaoException e) {
            mostrarErro("Erro ao acessar arquivo de clientes.");
        }
    }

    @FXML
    public void voltar() {
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