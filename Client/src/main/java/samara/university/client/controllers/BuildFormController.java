package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

public class BuildFormController {
    private ToggleGroup toggleGroup;

    @FXML
    private RadioButton buildFactory;
    @FXML
    private RadioButton buildAutomatedFactory;
    @FXML
    private RadioButton automateFactory;

    private Player me;

    public void initialize() {
        try {
            me = RequestSender.getRequestSender().me();
        } catch (Exception e) {
            e.printStackTrace();
        }
        toggleGroup = new ToggleGroup();
        buildFactory.setToggleGroup(toggleGroup);
        buildFactory.setSelected(true);
        buildAutomatedFactory.setToggleGroup(toggleGroup);
        automateFactory.setToggleGroup(toggleGroup);
    }

    public void ok(ActionEvent event) {
        try {
            if (buildFactory.isSelected()) {
                RequestSender.getRequestSender().buildFactory(me, false);
            } else if (buildAutomatedFactory.isSelected()) {
                RequestSender.getRequestSender().buildFactory(me, true);
            } else {
                RequestSender.getRequestSender().automateFactory(me);
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        close();
    }

    //------------------ Вспомогательные методы -----------------

    private void close() {
        Forms.closeForm("Build");
    }
}
