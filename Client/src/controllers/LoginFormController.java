package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import utils.Forms;
import utils.RequestSender;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LoginFormController {
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

    public void startSearchAction() {
        String login = loginInput.getText();
        if (login.length() >= MIN_NAME_LENGTH && login.length() <= MAX_NAME_LENGTH) {
            try {
                RequestSender.getRequestSender().authPlayer(login, avatarId);
            } catch (IOException e) {
                e.printStackTrace();
            }

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
