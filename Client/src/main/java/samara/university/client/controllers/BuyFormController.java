package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Bid;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;

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
        try {
            Player me = RequestSender.getRequestSender().me();
            Bid bid = Bid.createBid(
                    me,
                    false,
                    spinnerResourcesCount.getValue(),
                    spinnerResourcesPrice.getValue()
            );
            //отправить банку
            RequestSender.getRequestSender().buyResources(bid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        Forms.closeForm("BuyUnitsOfResources");
    }
}
