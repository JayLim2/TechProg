package samara.university.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import samara.university.client.utils.Forms;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/forms/Main.fxml"));
        primaryStage.setTitle("Игра \"Менеджмент\"");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.getScene().getStylesheets().add(Forms.CSS_PATH);
        primaryStage.show();

        Forms.registerForm("Main", primaryStage);
        Forms.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
