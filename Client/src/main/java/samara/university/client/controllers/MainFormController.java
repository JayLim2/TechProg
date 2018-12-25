package samara.university.client.controllers;

import javafx.event.ActionEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;

import java.awt.*;
import java.io.File;
import java.net.URI;

public class MainFormController {
    /**
     * Обработка нажатия на кнопку "Старт"
     *
     * @param event события нажатия на кнопку
     */
    public void startGameAction(ActionEvent event) {
        if (RequestSender.getRequestSender().tryConnectRequest()) {
            Forms.openForm("Login");
            Forms.closeForm("Main");
        } else {
            PredefinedAlerts.errorAlert("Сервер не найден.");
        }
    }

    /**
     * Обработка нажатия на кнопку "Справка"
     *
     * @param event события нажатия на кнопку
     */
    public void helpAction(ActionEvent event) {
        try {
            URI uri = new File("help/help.html").toURI();
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            PredefinedAlerts.helpNotFound();
        }
    }

    /**
     * Обработка нажатия на кнопку "Выход"
     * @param event события нажатия на кнопку
     */
    public void exitApplicationAction(ActionEvent event) {
        System.exit(0);
    }
}
