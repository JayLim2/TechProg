package samara.university.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import samara.university.common.entities.Avatar;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class WaitingPlayersFormController implements DisplayingFormController {
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

    @Override
    public void showAction(WindowEvent event) {
        ObservableList<Node> nodes = sessionPlayersPane.getChildren();
        //nodes.forEach(System.out::println);
        for (int i = 0, k = 0; k < size; k++) {
            avatarBlocks[k] = (ImageView) nodes.get(i++);
            labelBlocks[k] = (Text) nodes.get(i++);
        }
        updateInfo();
        updateTimeFields();
        playCountdown();
        waitPlayers();
    }

    @Override
    public void hideAction(WindowEvent event) {
        //resetTime();
    }

    public void interruptAction(ActionEvent event) {
        try {
            RequestSender.getRequestSender().exit();
            Forms.openForm("Main");
            Forms.closeForm("WaitingPlayers");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Duration ONE_SECOND_DURATION = Duration.seconds(1);
    private static final Duration UPDATE_INFO_INTERVAL = Duration.seconds(5);
    private Integer totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS;

    private void playCountdown() {
        //Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                totalSeconds--;
                updateTimeFields();

                if (totalSeconds <= 0) {
                    countdown.stop();
                    countdown.getKeyFrames().clear();
                    playGame();
                }
            }
        });

        countdown.getKeyFrames().add(frame);
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    Timeline countdown = new Timeline();
    Timeline waiter = new Timeline();

    // FIXME: 02.12.2018 баг с отрисовкой времени на форме

    private Thread waiterThread = null;

    private class WaitPlayersTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
                    long seconds = java.time.Duration.between(
                            sessionPackage.getSessionStartTime(),
                            LocalDateTime.now()
                    ).getSeconds();
                    List<Player> players = sessionPackage.getPlayers();

                    if (players.size() == Restrictions.MAX_PLAYERS_COUNT) {
                        totalSeconds = 0;
                    } else {
                        totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS - (int) seconds;
                        for (int i = 0; i < avatarBlocks.length; i++) {
                            if (i < players.size()) {
                                Player player = players.get(i);
                                avatarBlocks[i].setImage(new Image(player.getAvatar().getPath()));
                                labelBlocks[i].setText(player.getName());
                            } else {
                                avatarBlocks[i].setImage(new Image(Avatar.getEmptyAvatar().getPath()));
                                labelBlocks[i].setText("Логин");
                            }
                        }
                    }

                    Thread.sleep(5000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ожидание игроков
     */
    private void waitPlayers() {
        if (waiterThread == null) {
            waiterThread = new Thread(new WaitPlayersTask());
            waiterThread.start();
        }

        // TODO: 24.11.2018
        /*
        Регулярно (например, раз в 5 сек)
        проверять состояние сессии на сервере и в случае изменения
        обновлять интерфейс актуальными данными
         */
        //Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(UPDATE_INFO_INTERVAL, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (totalSeconds <= 0) {
                    waiterThread.interrupt();
                    waiter.stop();
                    waiter.getKeyFrames().clear();
                }
            }
        });

        waiter.getKeyFrames().add(frame);
        waiter.setCycleCount(Timeline.INDEFINITE);
        waiter.play();
    }

    private void resetTime() {
        totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS;
    }

    private void resetTime(int actualValue) {
        totalSeconds = actualValue;
    }

    private void updateTimeFields() {
        int minutes = totalSeconds / 60;
        minutesField.setText(Integer.toString(minutes));
        secondsField.setText(Integer.toString(totalSeconds - minutes * 60));
    }

    private void updateInfo() {

    }

    private void playGame() {
        waiterThread.interrupt();
        countdown.stop();
        waiter.stop();
        Forms.openForm("GameField");
        Forms.closeForm("WaitingPlayers");
    }
}
