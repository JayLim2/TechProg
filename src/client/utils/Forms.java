package client.utils;

import client.controllers.WaitingPlayersFormController;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Forms {
    private static Map<String, Stage> formStages;
    private static final Pattern pattern = Pattern.compile("([\\w]*).fxml");
    private static final String CSS_PATH = "/client/assets/css/main.css";

    public static void initialize() {
        if (formStages == null)
            formStages = new HashMap<>();

        Path root = Paths.get("src/client");
        Path start = Paths.get(root + "/forms");
        try (Stream<Path> pathStream = Files.walk(start, 1)) {
            pathStream.skip(1).forEach(new Consumer<Path>() {
                @Override
                public void accept(Path path) {
                    String pathStr = "..\\" + root.relativize(path).toString();
                    System.out.println(pathStr);
                    try {
                        Matcher matcher = pattern.matcher(path.toString());
                        if (matcher.find()) {
                            String formName = matcher.group(1);
                            URL resourceURL = getClass().getResource(pathStr);
                            if (formName != null) {
                                Stage stage = new Stage();
                                Parent root;
                                if (formName.equals("WaitingPlayers")) {
                                    FXMLLoader fxmlLoader = new FXMLLoader(resourceURL);
                                    root = fxmlLoader.load();
                                    WaitingPlayersFormController controller = fxmlLoader.getController();
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
                                } else {
                                    root = FXMLLoader.load(resourceURL);
                                }
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                addStyle(stage);
                                formStages.putIfAbsent(formName, stage);

                                System.out.println("FORM NAME: " + formName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addStyle(Stage stage) {
        stage.getScene().getStylesheets().add(CSS_PATH);
    }

    public static void registerForm(String formName, Stage stage) {
        if (formStages == null)
            formStages = new HashMap<>();
        formStages.putIfAbsent(formName, stage);
    }

    public static void openForm(String formName) {
        Stage stage = formStages.get(formName);
        if (stage != null) {
            stage.show();
        }
    }

    public static void closeForm(String formName) {
        Stage stage = formStages.get(formName);
        if (stage != null) {
            stage.close();
        }
    }

    public static Stage getStage(String formName) {
        return formStages.get(formName);
    }
}
