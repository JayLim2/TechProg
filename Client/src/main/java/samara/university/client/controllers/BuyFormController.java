package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;

public class BuyFormController extends TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

    public void initialize() {
        super.initialize();
        try {
            int minResourcePrice = RequestSender.getRequestSender().bankInfo().getMinResourcePrice();
            priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    minResourcePrice, Integer.MAX_VALUE, minResourcePrice, 50);

            spinnerCount.setValueFactory(countFactory);
            spinnerPrice.setValueFactory(priceFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ok(ActionEvent event) {
        super.ok(event);
        close();
    }

    @Override
    public void cancel(ActionEvent event) {
        close();
    }

    private void close() {
        Forms.closeForm("BuyUnitsOfResources");
    }
}
