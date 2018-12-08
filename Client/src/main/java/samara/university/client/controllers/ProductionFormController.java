package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

public class ProductionFormController implements DisplayingFormController {
    @FXML
    private Spinner<Integer> spinnerProductionCount;
    @FXML
    private Text labelAvailable;
    @FXML
    private Text labelPrice;
    @FXML
    private CheckBox productionMode;

    private Player me;

    private static final int NORMAL_MODE_COUNT = 1;
    private static final int NORMAL_MODE_PRICE = 2000;
    private static final int OPTIMIZED_MODE_COUNT = 2;
    private static final int OPTIMIZED_MODE_PRICE = 3000;

    private int totalResources;
    private int price;

    private int countProduction;

    public void initialize() {
        SpinnerValueFactory<Integer> countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, Integer.MAX_VALUE, 0, 1
        );
        spinnerProductionCount.setValueFactory(countFactory);
    }

    public void ok(ActionEvent event) {
        countProduction = spinnerProductionCount.getValue();
        if (countProduction > totalResources) {
            alert("Недостаточно ЕСМ.");
            return;
        }

        int countFactories = me.getWorkingFactories();
        int countAutoFactories = me.getWorkingAutomatedFactories();
        boolean mode = productionMode.isSelected();
        int totalCanBeProduced = !mode ?
                countFactories + countAutoFactories :
                countFactories + countAutoFactories * 2;
        if (countProduction > totalCanBeProduced) {
            alert("Недостаточно фабрик.");
            return;
        }
    }

    public void cancel(ActionEvent event) {
        close();
    }

    public void spin(MouseEvent event) {
        countProduction = spinnerProductionCount.getValue();
        int countFactories = me.getWorkingFactories();
        int countAutoFactories = me.getWorkingAutomatedFactories();
        boolean mode = productionMode.isSelected();
        int totalCanBeProduced = !mode ?
                countFactories + countAutoFactories :
                countFactories + countAutoFactories * 2;
        int totalCost = 0;
        int totalCostOnAutoFactories = !mode ?
                countAutoFactories * 2000 :
                (countAutoFactories / 2) * 2000;
        int totalCostOnFactories = 0;
        System.out.println("SPINNED");
    }

    @Override
    public void showAction(WindowEvent event) {
        me();
        totalResources = me.getUnitsOfResources();
        labelAvailable.setText(Integer.toString(totalResources));
        labelPrice.setText(Integer.toString(100 * totalResources));
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    //------------------ Вспомогательные методы -----------------

    private void close() {
        Forms.closeForm("Production");
    }

    private void me() {
        if (me == null) {
            try {
                me = RequestSender.getRequestSender().me();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void alert(String message) {
        new Alert(
                Alert.AlertType.ERROR,
                message,
                ButtonType.OK
        ).showAndWait();
    }
}
