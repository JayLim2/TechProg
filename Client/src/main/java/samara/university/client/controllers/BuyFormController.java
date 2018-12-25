package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;
import samara.university.common.packages.BankPackage;

public class BuyFormController extends TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

    public void initialize() {
        super.initialize();
        try {
            Player me = RequestSender.getRequestSender().me();
            BankPackage bankPackage = RequestSender.getRequestSender().bankInfo();
            int minResourcePrice = bankPackage.getMinResourcePrice();
            int maxCountToBuy = Math.min(bankPackage.getReserveUnitsOfResources(), me.getMoney() / minResourcePrice);
            countFactory.setMax(maxCountToBuy);
            priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    minResourcePrice, Integer.MAX_VALUE, minResourcePrice, 50);

            spinnerCount.setValueFactory(countFactory);
            spinnerPrice.setValueFactory(priceFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAction(WindowEvent event) {
        initialize();
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    @Override
    public void ok(ActionEvent event) {
        try {
            if (RequestSender.getRequestSender().sessionInfo().getCurrentPhase() != Restrictions.SEND_BID_RESOURCES_PHASE) {
                PredefinedAlerts.illegalPhaseAlert();
                close();
                return;
            }

            Player me = RequestSender.getRequestSender().me();
            if (me.getMoney() < spinnerCount.getValue() * spinnerPrice.getValue()) {
                PredefinedAlerts.notEnoughMoneyAlert();
            } else {
                sendBid(me, false);
                close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel(ActionEvent event) {
        close();
    }

    private void close() {
        Forms.closeForm("BuyUnitsOfResources");
    }
}
