package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.repository.RestauranteRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            RestauranteRepository restauranteRepository = new RestauranteRepository();
            String telaInicial;

            if (restauranteRepository.existe()) {
                telaInicial = "/fxml/Login.fxml";
            } else {
                telaInicial = "/fxml/ConfiguracaoInicial.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(telaInicial));
            Parent root = loader.load();
            stage.setTitle("FoodJava");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}