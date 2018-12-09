package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;

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
        super.ok(event);
        close();
    }

    @Override
    public void cancel(ActionEvent event) {
        close();
    }

    private void close() {
        Forms.closeForm("SellUnitsOfProducts");
    }
}
