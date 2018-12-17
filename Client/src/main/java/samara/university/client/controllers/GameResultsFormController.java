package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        Forms.closeForm("GameResults");
        Forms.openForm("Main");
    }

    public void saveGamelog(ActionEvent event) {
        try {
            String gamelog = RequestSender.getRequestSender().getGameLog();
            String dateStr = DateTimeFormatter.ofPattern("YYYY-MM-DD-HH-MM-SS").format(LocalDateTime.now());
            String fileName = "gamelog-" + dateStr + ".log";
            File file = new File(fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    PrintWriter out = new PrintWriter(new FileWriter(file));
                    out.print(gamelog);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit(ActionEvent event) {
        Forms.closeForm("GameResults");
    }
}
