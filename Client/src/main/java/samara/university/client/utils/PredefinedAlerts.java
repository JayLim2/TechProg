package samara.university.client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class PredefinedAlerts {
    public static void notEnoughMoneyAlert() {
        errorAlert("Недостаточно средств.");
    }

    public static void illegalPhaseAlert() {
        errorAlert("Фаза, в которой возможно было совершить данное действие, уже закончилась.");
    }

    public static void connectionResetAlert() {
        errorAlert("Соединение с сервером потеряно.");
    }

    public static void helpNotFound() {
        errorAlert("Пакет со справочной системой поврежден или не найден.");
    }

    public static void errorAlert(String message) {
        new Alert(
                Alert.AlertType.ERROR,
                message,
                ButtonType.OK
        ).showAndWait();
    }

    public static boolean confirmAlert(String message) {
        Optional<ButtonType> buttonType = new Alert(
                Alert.AlertType.CONFIRMATION,
                message
        ).showAndWait();
        return buttonType.isPresent();
    }

    public static void messageAlert(String message) {
        new Alert(
                Alert.AlertType.INFORMATION,
                message
        ).showAndWait();
    }
}
