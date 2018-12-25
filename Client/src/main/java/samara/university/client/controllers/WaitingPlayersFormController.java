package samara.university.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Avatar;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.net.SocketException;
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
        for (int i = 0, k = 0; k < size; k++) {
            avatarBlocks[k] = (ImageView) nodes.get(i++);
            labelBlocks[k] = (Text) nodes.get(i++);
        }
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
            updaterThread.interrupt();
            updaterThread = null;
            Forms.openForm("Main");
            Forms.closeForm("WaitingPlayers");
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Duration ONE_SECOND_DURATION = Duration.seconds(1);
    private static final Duration UPDATE_INFO_INTERVAL = Duration.seconds(5);
    private Integer totalSeconds = Restrictions.WAIT_TIME_LIMIT_SECONDS;

    private boolean countdownStopped = false;
    private boolean updaterStopped = false;

    public void stopCountdown() {
        countdownStopped = true;
    }

    public void stopUpdater() {
        updaterStopped = true;
    }

    private void playCountdown() {
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(countdownStopped) {
                    timelineStop();
                } else {
                    totalSeconds--;
                    updateTimeFields();

                    if (totalSeconds <= 0) {
                        timelineStop();
                        playGame();
                    }
                }
            }

            private void timelineStop() {
                timeline.stop();
                timeline.getKeyFrames().clear();
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private Thread updaterThread = null;

    /**
     * Ожидание игроков
     */
    private void waitPlayers() {
        UpdateFormTask updateFormTask = new UpdateFormTask();

        if (updaterThread == null) {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Platform.runLater(updateFormTask);
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            updaterThread = new Thread(updater);
            updaterThread.setDaemon(true);
            updaterThread.start();
        }
    }

    private class UpdateFormTask implements Runnable {
        @Override
        public void run() {
            try {
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
            } catch (SocketException e) {
                updaterThread.interrupt();
                stopCountdown();
                stopUpdater();
                PredefinedAlerts.connectionResetAlert();
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void playGame() {
        updaterThread.interrupt();
        updaterThread = null;
        stopCountdown();
        stopUpdater();
        Forms.openForm("GameField");
        Forms.closeForm("WaitingPlayers");
    }
}
