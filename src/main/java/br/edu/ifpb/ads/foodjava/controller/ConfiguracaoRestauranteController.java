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
        Restaurante restaurante = restauranteRepository.carregar();

        if (restaurante != null) {
            txNome.setText(restaurante.getNome());
            txCnpj.setText(restaurante.getCnpj());
            txEndereco.setText(restaurante.getEndereco());
            txTelefone.setText(restaurante.getTelefone());
            txCategoria.setText(restaurante.getCategoriaCulinaria());
            txEmail.setText(restaurante.getEmail());
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

        if (!ValidadorTelefone.validar(telefone)) {
            mostrarErro("Telefone inválido.");
            return;
        }

        if (!senha.isEmpty() && !ValidadorSenha.validar(senha)) {
            mostrarErro("Senha deve ter ao menos 8 caracteres e um número.");
            return;
        }

        Restaurante restaurante = new Restaurante(nome, cnpj, endereco, telefone, categoria, email, null);
        restauranteRepository.salvar(restaurante);
        atualizarGerente(nome, email, senha, telefone);

        mostrarSucesso("Configurações salvas com sucesso!");
    }

    private void atualizarGerente(String nome, String email, String senha, String telefone) {
        try {
            Gerente gerenteAtual = gerenteRepository.buscarPrimeiro();

            if (gerenteAtual == null) {
                return;
            }

            String senhaFinal = senha.isEmpty() ? gerenteAtual.getSenha() : senha;

            Gerente gerenteAtualizado = new Gerente(gerenteAtual.getId(), nome, email, senhaFinal, telefone
            );

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