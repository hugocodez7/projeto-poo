package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Restaurante;
import br.edu.ifpb.ads.foodjava.repository.GerenteRepository;
import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import br.edu.ifpb.ads.foodjava.util.ValidadorDocumento;
import br.edu.ifpb.ads.foodjava.util.ValidadorSenha;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracaoRestauranteController {

    @FXML private TextField txNome;
    @FXML private TextField txCnpj;
    @FXML private TextField txEndereco;
    @FXML private TextField txTelefone;
    @FXML private TextField txCategoria;
    @FXML private TextField txEmail;
    @FXML private PasswordField txSenha;
    @FXML private Button btnSalvar;
    @FXML private Button btnVoltar;

    private final RestauranteRepository restauranteRepository = new RestauranteRepository();
    private final GerenteRepository gerenteRepository = new GerenteRepository();

    @FXML
    public void initialize() {
        try {
            Restaurante restaurante = restauranteRepository.carregar();
            if (restaurante != null) {
                txNome.setText(restaurante.getNome());
                txCnpj.setText(restaurante.getCnpj());
                txEndereco.setText(restaurante.getEndereco());
                txTelefone.setText(restaurante.getTelefone());
                txCategoria.setText(restaurante.getCategoriaCulinaria());
                txEmail.setText(restaurante.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        if (nome.isEmpty() || cnpj.isEmpty() || endereco.isEmpty() || telefone.isEmpty() || categoria.isEmpty() || email.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        if (!ValidadorDocumento.validarCNPJ(cnpj)) {
            mostrarErro("CNPJ inválido.");
            return;
        }

        Restaurante restaurante = new Restaurante(nome, cnpj, endereco, telefone, categoria, email, null);
        restauranteRepository.salvar(restaurante);

        if (!senha.isEmpty()) {
            if (!ValidadorSenha.validar(senha)) {
                mostrarErro("Senha deve ter ao menos 8 caracteres e um número.");
                return;
            }
            try {
                Gerente gerenteAtual = gerenteRepository.buscarPrimeiro();
                if (gerenteAtual != null) {
                    Gerente gerenteAtualizado = new Gerente(gerenteAtual.getId(), nome, email, senha, telefone);
                    List<Gerente> lista = new ArrayList<>();
                    lista.add(gerenteAtualizado);
                    gerenteRepository.salvar(lista);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mostrarSucesso("Configurações salvas com sucesso!");
    }

    @FXML
    public void voltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PainelGerente.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnVoltar.getScene().getWindow();
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
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}