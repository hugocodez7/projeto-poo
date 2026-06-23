package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.util.Sessao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {

            Sessao.iniciar(new Cliente(1, "Joao", "Joao@email.com", "4321", "0011111111", "000.000.000-00", "muito longe"));

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/Cardapio.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setTitle("FoodJava - Cardápio");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}