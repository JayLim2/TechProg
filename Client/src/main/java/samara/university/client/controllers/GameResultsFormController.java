package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

public class GameResultsFormController implements DisplayingFormController {
    @FXML
    private Text labelWinnerName;

    @Override
    public void showAction(WindowEvent event) {
        try {
            Player player = RequestSender.getRequestSender().getWinner();
            if (player != null) {
                labelWinnerName.setText(player.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    public void newGame(ActionEvent event) {

    }

    public void saveGamelog(ActionEvent event) {

    }

    public void exit(ActionEvent event) {

    }
}
