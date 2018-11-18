package client.controllers;

import client.utils.Forms;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainFormController {
    @FXML
    private Button startGameButton;

    @FXML
    public void startGameAction(ActionEvent event) {
        Forms.openForm("Login");
        Forms.closeForm("Main");
    }

    @FXML
    public void exitApplicationAction(ActionEvent event) {
        System.exit(0);
    }
}
