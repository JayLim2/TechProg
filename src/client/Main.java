package client;

import client.utils.Forms;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/client/forms/Main.fxml"));
        primaryStage.setTitle("Игра \"Менеджмент\"");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Forms.registerForm("Main", primaryStage);
        Forms.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
