package samara.university.client.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import samara.university.client.Main;
import samara.university.client.controllers.DisplayingFormController;
import samara.university.client.controllers.GameFieldFormController;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Forms {
    private static Map<String, Stage> formStages;
    private static final Pattern pattern = Pattern.compile("([\\w]*).fxml");
    private static final String CSS_PATH = "/css/main.css";

    public static void initialize() {

    }

    private static void addStyle(Stage stage) {
        stage.getScene().getStylesheets().add(CSS_PATH);
    }

    public static void registerForm(String formName, Stage stage) {
        if (formStages == null)
            formStages = new HashMap<>();
        formStages.putIfAbsent(formName, stage);
    }

    private static final List<String> displayingForms = Arrays.asList(
            "WaitingPlayers", "GameField",
            "Production", "GameResults"
    );

    public static void openForm(String formName) {
        Stage stage = formStages.get(formName);
        if (stage != null) {
            stage.show();
            return;
        }

        try {
            if (formName != null) {
                stage = getForm(formName);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFormAsModal(String formName) {
        Stage stage = formStages.get(formName);
        if (stage != null) {
            stage.show();
            return;
        }

        try {
            if (formName != null) {
                stage = getModal(formName);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void configureDisplayingForm(Stage stage, FXMLLoader fxmlLoader, String formName) {
        DisplayingFormController controller = fxmlLoader.getController();

        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent window) {
                controller.showAction(window);
            }
        });
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent window) {
                controller.hideAction(window);
            }
        });
    }

    private static Stage getForm(String formName) throws IOException {
        URL resourceURL = Main.class.getResource("/forms/" + formName + ".fxml");
        Stage stage = new Stage();
        Parent root;
        if (displayingForms.contains(formName)) {
            FXMLLoader fxmlLoader = new FXMLLoader(resourceURL);
            root = fxmlLoader.load();
            configureDisplayingForm(stage, fxmlLoader, formName);
            if (formName.equals("GameField")) {
                ((GameFieldFormController) fxmlLoader.getController()).setLoader(fxmlLoader);
            }
        } else {
            root = FXMLLoader.load(resourceURL);
        }
        stage.setScene(new Scene(root));
        addStyle(stage);
        formStages.putIfAbsent(formName, stage);

        return stage;
    }

    private static Stage getModal(String formName) throws IOException {
        URL resourceURL = Main.class.getResource("/forms/" + formName + ".fxml");
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(resourceURL);
        Parent root = fxmlLoader.load();
        configureDisplayingForm(stage, fxmlLoader, formName);
        stage.setScene(new Scene(root));
        addStyle(stage);
        formStages.putIfAbsent(formName, stage);

        return stage;
    }

    public static void closeForm(String formName) {
        Stage stage = formStages.get(formName);
        if (stage != null && stage.isShowing()) {
            System.out.println("CLOSING " + formName + " ...");
            stage.close();
        }
    }

    public static Stage getStage(String formName) {
        return formStages.get(formName);
    }
}
