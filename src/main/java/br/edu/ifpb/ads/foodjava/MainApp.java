package br.edu.ifpb.ads.foodjava;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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
        // Tela provisória — substitua pelo carregamento do seu FXML
        Label label = new Label("FoodJava — ambiente configurado com sucesso!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("FoodJava");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
