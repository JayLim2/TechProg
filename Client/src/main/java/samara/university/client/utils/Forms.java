package samara.university.client.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import samara.university.client.Main;
import samara.university.client.controllers.GameFieldFormController;
import samara.university.client.controllers.WaitingPlayersFormController;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Forms {
    private static Map<String, Stage> formStages;
    private static final Pattern pattern = Pattern.compile("([\\w]*).fxml");
    private static final String CSS_PATH = "/css/main.css";

    public static void initialize() {
        /*if (formStages == null)
            formStages = new HashMap<>();

        //Path root = Paths.get("");

        Path start = Paths.get(Main.class.getResource("/forms").getPath().replace("file:/", ""));
        System.out.println(start);

        try (Stream<Path> pathStream = Files.walk(start, 1)) {
            pathStream.skip(1).forEach(new Consumer<Path>() {
                @Override
                public void accept(Path path) {
                    String pathStr = path.toString();
                    //String pathStr = "..\\" + root.relativize(path).toString();
                    System.out.println("==PATH==");
                    System.out.println(pathStr);
                    System.out.println();
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
            System.out.println("=== PATH ===");
            System.out.println(start.toAbsolutePath());
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("=== PATH 2 ===");

            System.out.println(start.toAbsolutePath());
        }*/
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
            return;
        }

        try {
            if (formName != null) {
                URL resourceURL = Main.class.getResource("/forms/" + formName + ".fxml");
                stage = new Stage();
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
                } else if (formName.equals("GameField")) {
                    FXMLLoader fxmlLoader = new FXMLLoader(resourceURL);
                    root = fxmlLoader.load();

                    GameFieldFormController controller = fxmlLoader.getController();
                    controller.setLoader(fxmlLoader);
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
                URL resourceURL = Main.class.getResource("/forms/" + formName + ".fxml");
                stage = new Stage();
                Parent root = FXMLLoader.load(resourceURL);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                addStyle(stage);
                formStages.putIfAbsent(formName, stage);

                stage.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
