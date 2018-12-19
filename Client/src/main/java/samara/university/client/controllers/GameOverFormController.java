package samara.university.client.controllers;

import javafx.event.ActionEvent;
import samara.university.client.utils.Forms;

public class GameOverFormController {

    public void closeAction(ActionEvent event) {
        Forms.closeForm("GameOver");
    }
}
