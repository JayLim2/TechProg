package client.controllers;

import client.utils.Forms;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class WaitingPlayersFormController {
    @FXML
    private TextField minutesField;
    @FXML
    private TextField secondsField;

    private static final int WAIT_TIME_LIMIT_SECONDS = 10;

    public void initialize() {
        updateTimeFields();
    }

    public void showAction(WindowEvent event) {
        resetTime();
        playCountdown();
        waitPlayers();
    }

    public void hideAction(WindowEvent event) {
        //resetTime();
    }

    private static final Duration ONE_SECOND_DURATION = Duration.seconds(1);
    private static final Duration UPDATE_INFO_INTERVAL = Duration.seconds(5);
    private Integer totalSeconds = WAIT_TIME_LIMIT_SECONDS;

    private void playCountdown() {
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                totalSeconds--;
                updateTimeFields();

                if (totalSeconds <= 0) {
                    playGame();
                    timeline.stop();
                    timeline.getKeyFrames().clear();
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Ожидание игроков
     */
    private void waitPlayers() {
        // TODO: 24.11.2018
        /*
        Регулярно (например, раз в 5 сек)
        проверять состояние сессии на сервере и в случае изменения
        обновлять интерфейс актуальными данными
         */
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(UPDATE_INFO_INTERVAL, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(totalSeconds);

                if (totalSeconds <= 0) {
                    timeline.stop();
                    timeline.getKeyFrames().clear();
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void resetTime() {
        totalSeconds = WAIT_TIME_LIMIT_SECONDS;
    }

    private void updateTimeFields() {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds - minutes * 60;
        minutesField.setText(Integer.toString(minutes));
        secondsField.setText(Integer.toString(seconds));
    }

    private void updateInfo() {
        // TODO: 24.11.2018 реализовать
    }

    private void playGame() {
        Forms.openForm("GameField");
        Forms.closeForm("WaitingPlayers");
    }
}
