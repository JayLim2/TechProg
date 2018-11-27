package samara.university.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.time.LocalDateTime;
import java.util.List;

public class WaitingPlayersFormController {
    @FXML
    private TextField minutesField;
    @FXML
    private TextField secondsField;

    @FXML
    private GridPane sessionPlayersPane;

    private ImageView[] avatarBlocks;
    private Text[] labelBlocks;

    final byte size = 4;
    public void initialize() {
        updateTimeFields();
        avatarBlocks = new ImageView[size];
        labelBlocks = new Text[size];
    }

    public void showAction(WindowEvent event) {
        ObservableList<Node> nodes = sessionPlayersPane.getChildren();
        nodes.forEach(System.out::println);
        for (int i = 0, k = 0; k < size; k++) {
            avatarBlocks[k] = (ImageView) nodes.get(i++);
            labelBlocks[k] = (Text) nodes.get(i++);
        }
        updateInfo();
        updateTimeFields();
        playCountdown();
        waitPlayers();
    }

    public void hideAction(WindowEvent event) {
        //resetTime();
    }

    private static final Duration ONE_SECOND_DURATION = Duration.seconds(1);
    private static final Duration UPDATE_INFO_INTERVAL = Duration.seconds(5);
    private Integer totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS;

    private void playCountdown() {
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                totalSeconds--;
                updateTimeFields();

                if (totalSeconds <= 0) {
                    timeline.stop();
                    timeline.getKeyFrames().clear();
                    playGame();
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
                updateInfo();

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
        totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS;
    }

    private void resetTime(int actualValue) {
        totalSeconds = actualValue;
    }

    private void updateTimeFields() {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds - minutes * 60;
        minutesField.setText(Integer.toString(minutes));
        secondsField.setText(Integer.toString(seconds));
    }

    private void updateInfo() {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().updateInfo();
            long seconds = java.time.Duration.between(
                    sessionPackage.getStartTime(),
                    LocalDateTime.now()
            ).getSeconds();
            List<Player> players = sessionPackage.getPlayers();

            if (players.size() == Restrictions.MAX_PLAYERS_COUNT) {
                totalSeconds = 0;
            } else {
                totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS - (int) seconds;
                int i = 0;
                for (Player player : players) {
                    System.out.println("PLAYER: " + player);
                    avatarBlocks[i].setImage(new Image(player.getAvatar().getPath()));
                    labelBlocks[i].setText(player.getName());
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playGame() {
        Forms.openForm("GameField");
        Forms.closeForm("WaitingPlayers");
    }
}
