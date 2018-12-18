package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

public class SellFormController extends TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

    public void initialize() {
        super.initialize();
        try {
            int maxProductPrice = RequestSender.getRequestSender().bankInfo().getMaxProductPrice();
            priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    50, maxProductPrice, maxProductPrice, 50);

            spinnerCount.setValueFactory(countFactory);
            spinnerPrice.setValueFactory(priceFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ok(ActionEvent event) {
        try {
            if (RequestSender.getRequestSender().sessionInfo().getCurrentPhase() != Restrictions.SEND_BID_PRODUCTS_PHASE) {
                PredefinedAlerts.illegalPhaseAlert();
                close();
                return;
            }

            Player me = RequestSender.getRequestSender().me();
            super.sendBid(me, true);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel(ActionEvent event) {
        close();
    }

    private void close() {
        Forms.closeForm("SellUnitsOfProducts");
    }
}
