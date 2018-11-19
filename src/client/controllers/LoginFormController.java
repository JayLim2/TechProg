package client.controllers;

import client.utils.Forms;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginFormController {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 15;

    @FXML
    private TextField loginInput;
    @FXML
    private Label errorMsgLabel;

    public void startSearchAction() {
        String login = loginInput.getText();
        if (login.length() >= MIN_NAME_LENGTH && login.length() <= MAX_NAME_LENGTH) {
            Forms.openForm("WaitingPlayers");
            Forms.closeForm("Login");
        } else {
            errorMsgLabel.setText("Длина логина должна быть\nот " + MIN_NAME_LENGTH + " до " + MAX_NAME_LENGTH + " символов.");
            errorMsgLabel.setVisible(true);
        }
    }

    public void goBackAction() {
        Forms.openForm("Main");
        Forms.closeForm("Login");
    }
}
