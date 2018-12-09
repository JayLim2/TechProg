package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

public class TradeFormController {
    @FXML
    protected Spinner<Integer> spinnerCount;
    @FXML
    protected Spinner<Integer> spinnerPrice;

    protected SpinnerValueFactory.IntegerSpinnerValueFactory countFactory;
    protected SpinnerValueFactory.IntegerSpinnerValueFactory priceFactory;

    public void initialize() {
        countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, Integer.MAX_VALUE, 0, 1);
    }

    public void ok(ActionEvent event) {
        try {
            Player me = RequestSender.getRequestSender().me();
            RequestSender.getRequestSender().sendBid(
                    me,
                    false,
                    spinnerCount.getValue(),
                    spinnerPrice.getValue()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
    }
}
