package br.edu.ifpb.ads.foodjava;

import br.edu.ifpb.ads.foodjava.controller.CardapioController;
import br.edu.ifpb.ads.foodjava.controller.ItemFormController;
import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.repository.CardapioRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.FileNotFoundException;
import java.io.IOException;

/**

 Ponto de entrada da aplicação FoodJava.*
 Esta classe deve ser mantida mínima — ela apenas inicializa o JavaFX
 e carrega a primeira tela. Toda a lógica de negócio deve ficar nos
 pacotes model, controller e repository.*
 DICA: para carregar uma tela FXML, substitua o conteúdo de start() por:*
 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
 Parent root = loader.load();
 stage.setScene(new Scene(root, 900, 600));
 stage.show();*/
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        CardapioRepository cardapioRepository = new CardapioRepository();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/cardapio.fxml")
        );
        Parent root = loader.load();
        stage.setScene(new Scene(root, 900,600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}