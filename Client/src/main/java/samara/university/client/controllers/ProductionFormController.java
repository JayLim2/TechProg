package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;

import java.io.IOException;

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
    private static final int MODERNIZED_MODE_COUNT = 2;
    private static final int MODERNIZED_MODE_PRICE = 3000;

    private int totalResources;

    private int totalProductsCost;
    private int totalProductsCount;

    private SpinnerValueFactory.IntegerSpinnerValueFactory countFactory;

    public void initialize() {
        try {
            me = RequestSender.getRequestSender().me();
        } catch (Exception e) {
            e.printStackTrace();
        }
        countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, getTotalCanBeProduced(false), 0, 1
        );
        spinnerProductionCount.setValueFactory(countFactory);
    }

    private int getTotalCanBeProduced(boolean mode) {
        return Math.min(
                me.getUnitsOfResources(),
                me.getWorkingFactories() + (!mode ? me.getWorkingAutomatedFactories() : me.getWorkingAutomatedFactories() * 2)
        );
    }

    public void ok(ActionEvent event) {
        if (totalProductsCount == 0) {
            PredefinedAlerts.errorAlert("Количество ЕГП для производство должно быть > 0.");
        } else if (me.getMoney() < totalProductsCost) {
            PredefinedAlerts.notEnoughMoneyAlert();
        } else {
            try {
                RequestSender.getRequestSender().startProduction(me, totalProductsCount, totalProductsCost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            close();
        }
    }

    public void cancel(ActionEvent event) {
        close();
    }

    public void spin(MouseEvent event) {
        //Получаем количество производимого ЕГП
        totalProductsCount = spinnerProductionCount.getValue();

        //Рассчитываем суммарную стоимость
        int countAutoFactories = me.getWorkingAutomatedFactories();
        boolean mode = productionMode.isSelected();

        totalProductsCost = 0;
        int factoriesInFastMode = mode ? Math.min(totalProductsCount / MODERNIZED_MODE_COUNT, countAutoFactories) : 0;
        totalProductsCost += factoriesInFastMode * MODERNIZED_MODE_PRICE;
        totalProductsCount -= factoriesInFastMode * MODERNIZED_MODE_COUNT;
        totalProductsCost += totalProductsCount * NORMAL_MODE_PRICE;

        labelPrice.setText(Integer.toString(totalProductsCost));
    }

    public void setMode(ActionEvent event) {
        spin(null);
        int total = getTotalCanBeProduced(productionMode.isSelected());
        if (spinnerProductionCount.getValue() > total) {
            spinnerProductionCount.decrement(spinnerProductionCount.getValue() - total);
        }
        countFactory.setMax(getTotalCanBeProduced(productionMode.isSelected()));
    }

    @Override
    public void showAction(WindowEvent event) {
        totalResources = me.getUnitsOfResources();
        labelAvailable.setText(Integer.toString(totalResources));
        if (me.getWorkingAutomatedFactories() > 0) {
            productionMode.setDisable(false);
        }
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    //------------------ Вспомогательные методы -----------------

    private void close() {
        Forms.closeForm("Production");
    }
}
