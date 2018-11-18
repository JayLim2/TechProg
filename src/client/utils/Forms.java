package client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
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

    public static void initialize() {
        if (formStages == null)
            formStages = new HashMap<>();

        Path root = Paths.get("src/client");
        Path start = Paths.get(root + "/forms");
        try (Stream<Path> pathStream = Files.walk(start)) {
            pathStream.skip(1).forEach(new Consumer<Path>() {
                @Override
                public void accept(Path path) {
                    String pathStr = "..\\" + root.relativize(path).toString();
                    System.out.println(pathStr);
                    try {
                        Stage stage = new Stage();
                        Parent root = FXMLLoader.load(Forms.class.getResource(pathStr));
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        Matcher matcher = pattern.matcher(path.toString());
                        if (matcher.find()) {
                            formStages.putIfAbsent(matcher.group(1), stage);
                            System.out.println("FORM NAME: " + matcher.group(1));
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
}
