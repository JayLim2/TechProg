package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

public class TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

    public void initialize() {
        // TODO: 03.12.2018 проверить ограничения !!!
        SpinnerValueFactory<Integer> countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 10, 0, 5);
        SpinnerValueFactory<Integer> priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 300, 0, 50);

        spinnerCount.setValueFactory(countFactory);
        spinnerPrice.setValueFactory(priceFactory);
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
