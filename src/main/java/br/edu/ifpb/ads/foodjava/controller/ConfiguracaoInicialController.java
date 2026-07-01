package br.edu.ifpb.ads.foodjava.controller;

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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracaoInicialController {

    @FXML private TextField txNome;
    @FXML private TextField txCnpj;
    @FXML private TextField txEndereco;
    @FXML private TextField txTelefone;
    @FXML private TextField txCategoria;
    @FXML private TextField txEmail;
    @FXML private PasswordField txSenha;
    @FXML private Button btnSalvar;

    private final RestauranteRepository restauranteRepository = new RestauranteRepository();

    @FXML
    public void salvar() {
        String nome = txNome.getText().trim();
        String cnpj = txCnpj.getText().trim();
        String endereco = txEndereco.getText().trim();
        String telefone = txTelefone.getText().trim();
        String categoria = txCategoria.getText().trim();
        String email = txEmail.getText().trim();
        String senha = txSenha.getText().trim();

        if (nome.isEmpty() || cnpj.isEmpty() || endereco.isEmpty() || telefone.isEmpty() || categoria.isEmpty()
                || email.isEmpty() || senha.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        if (!ValidadorDocumento.validarCNPJ(cnpj)) {
            mostrarErro("CNPJ inválido.");
            return;
        }

        if (!ValidadorTelefone.validar(telefone)) {
            mostrarErro("Telefone inválido.");
            return;
        }

        if (!ValidadorSenha.validar(senha)) {
            mostrarErro("Senha deve ter ao menos 8 caracteres e um número.");
            return;
        }


        Restaurante restaurante = new Restaurante(nome, cnpj, endereco, telefone, categoria, email, null);
        restauranteRepository.salvar(restaurante);

        GerenteRepository gerenteRepository = new GerenteRepository();
        Gerente gerente = new Gerente(1L, nome, email, senha, telefone);
        List<Gerente> lista = new ArrayList<>();
        lista.add(gerente);
        gerenteRepository.salvar(lista);

        mostrarSucesso("Restaurante configurado com sucesso!");
        irParaLogin();
    }

    private void irParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnSalvar.getScene().getWindow();
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

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);alert.setContentText(mensagem);
        alert.showAndWait();

    }
}
