package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

public class LoanFormController implements DisplayingFormController {
    @FXML
    private Spinner<Integer> spinnerAutoFactoriesCount;
    @FXML
    private Spinner<Integer> spinnerFactoriesCount;
    @FXML
    private Text labelTotalLoan;
    @FXML
    private Text labelMonth;

    private Player me;
    private int amount;
    private int amountAuto;
    private int count;
    private int countAuto;

    public void initialize() {
        try {
            me = RequestSender.getRequestSender().me();

            SpinnerValueFactory<Integer> countAutoFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, me.getWorkingAutomatedFactories() - me.getBailedAutoFactories(), 0, 1);
            SpinnerValueFactory<Integer> countFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, me.getWorkingFactories() - me.getBailedFactories(), 0, 1);
            spinnerFactoriesCount.setValueFactory(countFactory);
            spinnerAutoFactoriesCount.setValueFactory(countAutoFactory);

            labelMonth.setText(Integer.toString(RequestSender.getRequestSender().sessionInfo().getCurrentMonth() + Restrictions.LOAN_MONTHS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAction(WindowEvent event) {
        initialize();
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    public void ok(ActionEvent event) {
        try {
            //me = RequestSender.getRequestSender().me();
            RequestSender.getRequestSender().newLoan(me, amount, amountAuto, count, countAuto);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        close();
    }

    public void spin(MouseEvent event) {
        count = spinnerFactoriesCount.getValue();
        amount = count * Restrictions.LOAN_AMOUNT_BY_AUTOMATED_FACTORY;

        countAuto = spinnerAutoFactoriesCount.getValue();
        amountAuto = countAuto * Restrictions.LOAN_AMOUNT_BY_FACTORY;

        labelTotalLoan.setText(Integer.toString(amount + amountAuto));
    }

    private void close() {
        Forms.closeForm("Loan");
    }
}
