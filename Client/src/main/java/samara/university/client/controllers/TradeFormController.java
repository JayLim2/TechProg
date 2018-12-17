package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

import java.io.IOException;

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
    }

    public void cancel(ActionEvent event) {
    }

    //---------------- Вспомогательные методы -----------------------

    protected void sendBid(Player player, boolean bidType) throws IOException {
        RequestSender.getRequestSender().sendBid(
                player,
                bidType,
                spinnerCount.getValue(),
                spinnerPrice.getValue()
        );
    }
}
