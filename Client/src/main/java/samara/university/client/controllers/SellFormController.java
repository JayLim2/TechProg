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

import java.net.SocketException;

public class SellFormController extends TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

    private Player me;

    public void initialize() {
        //super.initialize();
        try {
            me = RequestSender.getRequestSender().me();

            countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, me.getUnitsOfProducts(), 0, 1);

            int maxProductPrice = RequestSender.getRequestSender().bankInfo().getMaxProductPrice();
            priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    50, maxProductPrice, maxProductPrice, 50);

            spinnerCount.setValueFactory(countFactory);
            spinnerPrice.setValueFactory(priceFactory);
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
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
            if (RequestSender.getRequestSender().sessionInfo().getCurrentPhase() != Restrictions.SEND_BID_PRODUCTS_PHASE) {
                PredefinedAlerts.illegalPhaseAlert();
                close();
                return;
            }

            super.sendBid(me, true);
            close();
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
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
