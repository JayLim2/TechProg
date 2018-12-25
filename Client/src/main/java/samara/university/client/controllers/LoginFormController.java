package samara.university.client.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Avatar;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LoginFormController implements DisplayingFormController {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 15;

    @FXML
    private GridPane avatarsPane;
    @FXML
    private TextField loginInput;
    @FXML
    private Label errorMsgLabel;

    //Данные будущего игрока
    private int avatarId = 0;

    public void initialize() {
        Random random = new Random();
        int len = 0;
        while (len < 3 || len > 15) {
            len = Math.abs(random.nextInt(16));
        }
        char[] chars = new char[len];
        for (int i = 0; i < chars.length; i++) {
            while (!(chars[i] >= 97 && chars[i] <= 122)) {
                chars[i] = (char) random.nextInt(123);
            }
        }

        loginInput.setText(new String(chars));

        avatarId = 0;
    }

    @Override
    public void showAction(WindowEvent event) {
        initialize();
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    public void startSearchAction() {
        String login = loginInput.getText().trim();
        if (login.length() >= MIN_NAME_LENGTH && login.length() <= MAX_NAME_LENGTH) {
            try {
                boolean isUniqueLogin = RequestSender.getRequestSender().checkLoginUniqueness(login);
                if (isUniqueLogin) {
                    boolean authorized = RequestSender.getRequestSender().authPlayer(login, avatarId);
                    if (!authorized) {
                        PredefinedAlerts.errorAlert("Игра уже идет, либо найдены все 4 игрока.");
                        goBackAction();
                    } else {
                        Forms.openForm("WaitingPlayers");
                        Forms.closeForm("Login");
                    }
                } else {
                    errorMsgLabel.setText("Такое имя уже занято.");
                    errorMsgLabel.setVisible(true);
                }
            } catch (SocketException e) {
                PredefinedAlerts.connectionResetAlert();
                System.exit(-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorMsgLabel.setText("Длина логина должна быть\nот " + MIN_NAME_LENGTH + " до " + MAX_NAME_LENGTH + " символов.");
            errorMsgLabel.setVisible(true);
        }
    }

    public void goBackAction() {
        Forms.openForm("Main");
        Forms.closeForm("Login");
    }

    private boolean avatarPaneInitialized;
    private List<HBox> boxes = new LinkedList<>();

    public void avatarClickAction(MouseEvent event) {
        if (!avatarPaneInitialized) {
            avatarPaneInit();
            avatarPaneInitialized = true;
        }

        Object source = event.getSource();

        if (source instanceof HBox) {
            HBox hBox = (HBox) source;
            int index = boxes.indexOf(hBox);
            if (index != -1) {
                this.avatarId = index;
                boxes.get(index).requestFocus();
            }
        }
    }

    private void avatarPaneInit() {
        ObservableList<Node> nodesInPane = avatarsPane.getChildren();
        for (Node node : nodesInPane) {
            if (node instanceof HBox) {
                boxes.add((HBox) node);
            }
        }
    }
}
