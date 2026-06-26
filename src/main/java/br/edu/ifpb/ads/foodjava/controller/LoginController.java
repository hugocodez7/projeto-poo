package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.GerenteRepository;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txEmail;
    @FXML private PasswordField txSenha;
    @FXML private Button btnEntrar;
    @FXML private Button btnCadastrar;

    private final AuthController authController = new AuthController();

    @FXML
    public void initialize() {
        try {
            GerenteRepository gerenteRepository = new GerenteRepository();
            Gerente gerente = gerenteRepository.buscarPrimeiro();
            if (gerente != null) {
                authController.setGerente(gerente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void entrar() {
        String email = txEmail.getText().trim();
        String senha = txSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Preencha e-mail e senha.");
            return;
        }

        try {
            Usuario usuario = authController.login(email, senha);
            Sessao.iniciar(usuario);

            if (usuario instanceof Gerente) {
                irParaPainel();
            } else if (usuario instanceof Cliente) {
                irParaCardapio();
            }

        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    public void irParaCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/CadastroCliente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnCadastrar.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void irParaPainel() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/PainelGerente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnEntrar.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void irParaCardapio() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/cardapio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnEntrar.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}