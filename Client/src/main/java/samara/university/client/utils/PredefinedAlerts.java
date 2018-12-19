package samara.university.client.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class PredefinedAlerts {
    public static void notEnoughMoneyAlert() {
        errorAlert("Недостаточно средств.");
    }

    public static void illegalPhaseAlert() {
        errorAlert("Фаза, в которой возможно было совершить данное действие, уже закончилась.");
    }

    public static void errorAlert(String message) {
        new Alert(
                Alert.AlertType.ERROR,
                message,
                ButtonType.OK
        ).showAndWait();
    }
}
