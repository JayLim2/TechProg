package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import samara.university.client.utils.Forms;

public class SellFormController extends TradeFormController {
    @FXML
    private Spinner<Integer> spinnerCount;
    @FXML
    private Spinner<Integer> spinnerPrice;

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
