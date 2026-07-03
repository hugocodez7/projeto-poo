package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txEmail;
    @FXML
    private PasswordField txSenha;
    @FXML
    private Button btnEntrar;
    @FXML
    private Button btnCadastrar;

    private final AuthController authController = new AuthController();

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
                trocarTela("/fxml/PainelGerente.fxml", "Painel do Gerente", btnEntrar);
            } else if (usuario instanceof Cliente) {
                trocarTela("/fxml/novaInterfaceCardapio.fxml", "Cardápio", btnEntrar);
            }

        } catch (Exception e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    public void irParaCadastro() {
        trocarTela("/fxml/CadastroCliente.fxml", "Cadastro", btnCadastrar);
    }

    private void trocarTela(String caminhoFXML, String titulo, Button botao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Stage stage = (Stage) botao.getScene().getWindow();
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
}