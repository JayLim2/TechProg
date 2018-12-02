package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.Forms;

public class BuyFormController {
    @FXML
    private Spinner<Integer> spinnerResourcesCount;
    @FXML
    private Spinner<Integer> spinnerResourcesPrice;

    public void initialize() {
        // TODO: 03.12.2018 проверить ограничения !!! 
        SpinnerValueFactory<Integer> countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 10, 0, 5);
        SpinnerValueFactory<Integer> priceFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 300, 0, 50);

        spinnerResourcesCount.setValueFactory(countFactory);
        spinnerResourcesPrice.setValueFactory(priceFactory);
    }

    public void ok(ActionEvent event) {
        //сформировать заявку и отправить банку
    }

    public void cancel(ActionEvent event) {
        Forms.closeForm("BuyUnitsOfResources");
    }
}
