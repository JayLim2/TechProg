package client;

import client.utils.Forms;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Main extends Application {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/client/forms/Main.fxml"));
        primaryStage.setTitle("Игра \"Менеджмент\"");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        Forms.registerForm("Main", primaryStage);
        Forms.initialize();
        primaryStage.show();

        //Инициализация соединения с сервером
        Socket socket = new Socket(InetAddress.getLocalHost(), 7777);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
