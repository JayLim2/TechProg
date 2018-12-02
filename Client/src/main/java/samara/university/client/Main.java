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
        /*String path = getClass().getResource("/forms/Main.fxml").getFile();
        System.out.println(path);*/
        //System.out.println(getClass().getResource("/forms/Main.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("/forms/Main.fxml"));
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
