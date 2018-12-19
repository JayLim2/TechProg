package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
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
            if (RequestSender.getRequestSender().sessionInfo().getCurrentPhase() != Restrictions.BUILDING_AND_AUTOMATION_PHASE) {
                PredefinedAlerts.illegalPhaseAlert();
                close();
                return;
            }

            int myMoney = me.getMoney();
            if (buildFactory.isSelected()) {
                if (myMoney < Restrictions.BUILDING_FACTORY_PRICE) {
                    PredefinedAlerts.notEnoughMoneyAlert();
                } else {
                    RequestSender.getRequestSender().buildFactory(me, false);
                    close();
                }
            } else if (buildAutomatedFactory.isSelected()) {
                if (myMoney < Restrictions.BUILDING_AUTOMATED_FACTORY_PRICE) {
                    PredefinedAlerts.notEnoughMoneyAlert();
                } else {
                    RequestSender.getRequestSender().buildFactory(me, true);
                    close();
                }
            } else {
                if (myMoney < (Restrictions.AUTOMATION_FACTORY_PRICE / 2)) {
                    PredefinedAlerts.notEnoughMoneyAlert();
                } else {
                    RequestSender.getRequestSender().automateFactory(me);
                    close();
                }
            }
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
