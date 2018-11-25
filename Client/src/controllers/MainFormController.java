package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import utils.Forms;

public class MainFormController {
    /**
     * Обработка нажатия на кнопку "Старт"
     *
     * @param event события нажатия на кнопку
     */
    public void startGameAction(ActionEvent event) {
        Forms.openForm("Login");
        Forms.closeForm("Main");
    }

    /**
     * Обработка нажатия на кнопку "Справка"
     *
     * @param event события нажатия на кнопку
     */
    public void helpAction(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Справка будет добавлена позднее.").show();
    }

    /**
     * Обработка нажатия на кнопку "Выход"
     * @param event события нажатия на кнопку
     */
    public void exitApplicationAction(ActionEvent event) {
        System.exit(0);
    }
}
