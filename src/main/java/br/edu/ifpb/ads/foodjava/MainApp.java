package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Ponto de entrada da aplicação FoodJava.
 *
 * Esta classe deve ser mantida mínima — ela apenas inicializa o JavaFX
 * e carrega a primeira tela. Toda a lógica de negócio deve ficar nos
 * pacotes model, controller e repository.
 *
 * DICA: para carregar uma tela FXML, substitua o conteúdo de start() por:
 *
 *   FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
 *   Parent root = loader.load();
 *   stage.setScene(new Scene(root, 900, 600));
 *   stage.show();
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            RestauranteRepository repo = new RestauranteRepository();
            String fxml = repo.existe() ? "/fxml/Login.fxml" : "/fxml/ConfiguracaoInicial.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("FoodJava");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
