import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Forms;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/forms/Main.fxml"));
        primaryStage.setTitle("Игра \"Менеджмент\"");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Forms.registerForm("Main", primaryStage);
        Forms.initialize();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
