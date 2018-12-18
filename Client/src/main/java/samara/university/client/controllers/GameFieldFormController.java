package samara.university.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import samara.university.client.utils.Forms;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;
import samara.university.common.packages.BankPackage;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameFieldFormController implements DisplayingFormController {
    @FXML
    private AnchorPane mainPane;

    //bank
    @FXML
    private Text labelBankResourcesCount;
    @FXML
    private Text labelBankResourcesMinPrice;
    @FXML
    private Text labelBankProductsCount;
    @FXML
    private Text labelBankProductsMaxPrice;

    //others
    @FXML
    private Text labelMonth;
    @FXML
    private Text labelPhase;
    @FXML
    private Text labelMinutesLeft;
    @FXML
    private Text labelSecondsLeft;

    @FXML
    private TextArea gamelogArea;

    private Player me;
    private Player senior;

    private static final String IN_PROGRESS_SUFFIX = "_in_progress";
    private static final String[] TYPES = new String[]{
            "text", "image_view"
    };

    private static final String[] ENTITIES = new String[]{
            "player_profile",

            "money",
            "resources",
            "products",
            "factories",
            "auto_factories",

            "bank_resources",
            "bank_resources_min_price",
            "bank_products",
            "bank_products_max_price"
    };

    private static final String[] DESCRIPTIONS = new String[]{
            "login",
            "amount",
            "avatar",
            "senior"
    };

    private FXMLLoader loader;
    private Map<String, Object> formNamespace;

    public void setLoader(FXMLLoader loader) {
        this.loader = loader;
    }

    public Map<String, Object> getNamespace() {
        if (formNamespace == null) {
            if (loader != null) {
                formNamespace = loader.getNamespace();
            }
        }
        return formNamespace;
    }

    @Override
    public void showAction(WindowEvent event) {
        try {
            //gamelogArea.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            gamelogArea.setDisable(true);


            SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
            me = RequestSender.getRequestSender().me();
            senior = sessionPackage.getCurrentSeniorPlayer();

            //Настройка отображения кнопок
            updateMenuVisibility();

            //Пропустить первые 2 фазы
            //nextPhase(null);
            //nextPhase(null);

            //Обновить время начала фазы
            //getTurnTime();

            //Запустить циклическое обновление клиента
            cyclicalUpdater();

            //Запустить обратный отсчет времени хода
            phaseCountdown();

            //Заполняем профили игроков
            fillAllProfiles(sessionPackage);

            //Информация о ходе
            fillTurn(sessionPackage.getCurrentPhase(), sessionPackage.getCurrentMonth(), totalSeconds);

            //Информация о банковских резервах
            fillBankReserves();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    private void fillAllProfiles(SessionPackage sessionPackage) {
        try {
            List<Player> players = sessionPackage.getPlayers();
            me = RequestSender.getRequestSender().me();

            if (me.isBankrupt()) {
                stopPhaseCountdown();
                stopCyclicalUpdater();
                Forms.closeForm("GameField");
                Forms.openForm("GameOver");
            }

            players.remove(me);

            //Заполняем данные текущего игрока
            fillProfile(me, 0);

            //Заполняем данные остальных игроков
            for (int i = 0; i < players.size(); i++) {
                fillProfile(players.get(i), i + 1);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fillProfile(Player player, int i) {
        Text text;
        ImageView imageView;

        imageView = (ImageView) getElementById("image_view", "player_profile", "avatar", i);
        imageView.setImage(new Image(player.getAvatar().getPath()));

        imageView = (ImageView) getElementById("image_view", "player_profile", "senior", i);
        System.out.println("player: " + player);
        System.out.println("senior: " + senior);
        imageView.setVisible(Objects.equals(player, senior));

        text = (Text) getElementById("text", "player_profile", "login", i);
        text.setText(player.getName());

        text = (Text) getElementById("text", "money", "amount", i);
        text.setText(Integer.toString(player.getMoney()));

        text = (Text) getElementById("text", "resources", "amount", i);
        text.setText(Integer.toString(player.getUnitsOfResources()));

        text = (Text) getElementById("text", "products", "amount", i);
        text.setText(Integer.toString(player.getUnitsOfProducts()));

        text = (Text) getElementById("text", "products" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText(player.getInProduction() == 0 ? "" : "+" + player.getInProduction());

        text = (Text) getElementById("text", "factories", "amount", i);
        text.setText(Integer.toString(player.getWorkingFactories()));

        text = (Text) getElementById("text", "factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText(player.getUnderConstructionFactories() == 0 ? "" : "+" + player.getUnderConstructionFactories());

        text = (Text) getElementById("text", "auto_factories", "amount", i);
        text.setText(Integer.toString(player.getWorkingAutomatedFactories()));

        text = (Text) getElementById("text", "auto_factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText(player.getUnderConstructionAutomatedFactories() == 0 ? "" : "+" + player.getUnderConstructionAutomatedFactories());
    }

    public void fillTurn(int phase, int month, int timeLeftInSeconds) {
        labelPhase.setText(Integer.toString(phase));
        labelMonth.setText(Integer.toString(month));
    }

    public void fillBankReserves() {
        try {
            BankPackage bankPackage = RequestSender.getRequestSender().bankInfo();

            labelBankResourcesCount.setText(Integer.toString(bankPackage.getReserveUnitsOfResources()));
            labelBankResourcesMinPrice.setText(Integer.toString(bankPackage.getMinResourcePrice()));
            labelBankProductsCount.setText(Integer.toString(bankPackage.getReserveUnitsOfProducts()));
            labelBankProductsMaxPrice.setText(Integer.toString(bankPackage.getMaxProductPrice()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final Duration ONE_SECOND_DURATION = Duration.seconds(1);
    private int totalSeconds = Restrictions.PHASE_LENGTH_IN_SECONDS;
    private boolean phaseCountdownStopped = false;

    private void getTurnTime() {
        try {
            LocalDateTime time = null;
            while (time == null) {
                time = RequestSender.getRequestSender().getTurnTime();
            }

            long seconds = java.time.Duration.between(
                    time,
                    LocalDateTime.now()
            ).getSeconds();

            totalSeconds = Restrictions.PHASE_LENGTH_IN_SECONDS - (int) seconds;
            //phaseCountdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void phaseCountdown() {
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (totalSeconds <= 0 || phaseCountdownStopped) {
                    timeline.stop();
                    timeline.getKeyFrames().clear();
                }

                if (totalSeconds > 0) {
                    totalSeconds--;
                    updateTimeFields();
                } else {
                    nextPhase(null);
                    updateForm();
                    phaseCountdown();
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stopPhaseCountdown() {
        phaseCountdownStopped = true;
    }

    private void updateGamelog() {
        try {
            String gamelog = RequestSender.getRequestSender().getGameLog();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-------------------- Циклическое обновление клиента раз в N секунд ---------------
    private static final Duration CYCLE_PERIOD = Duration.seconds(3);
    private boolean cyclicalUpdateStopped = false;

    private void cyclicalUpdater() {
        Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(CYCLE_PERIOD, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateForm();

                if (cyclicalUpdateStopped) {
                    timeline.stop();
                    timeline.getKeyFrames().clear();
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopCyclicalUpdater() {
        cyclicalUpdateStopped = true;
    }

    private void updateForm() {
        try {
            //Если найден победитель - завершить игру
            if (RequestSender.getRequestSender().getWinner() != null) {
                stopPhaseCountdown();
                stopCyclicalUpdater();
                Forms.closeForm("GameField");
                Forms.openForm("GameResults");
                return;
            }

            SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();

            if (sessionPackage.getCurrentPhase() == Restrictions.REGULAR_COSTS_PHASE
                    || sessionPackage.getCurrentPhase() == Restrictions.CALCULATE_RESERVES_PHASE
                    || sessionPackage.getCurrentPhase() == Restrictions.PAY_LOAN_PERCENT_PHASE
                    || sessionPackage.getCurrentPhase() == Restrictions.PAY_LOAN_PHASE) {
                nextPhase(null);
            } else {
                fillAllProfiles(sessionPackage);
                labelMonth.setText(Integer.toString(sessionPackage.getCurrentMonth()));
                labelPhase.setText(Integer.toString(sessionPackage.getCurrentPhase()));
                updateMenuVisibility();
            }
            senior = sessionPackage.getCurrentSeniorPlayer();
            fillBankReserves();
            getTurnTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------

    private void updateTimeFields() {
        int minutes = totalSeconds / 60;
        labelMinutesLeft.setText(Integer.toString(minutes));
        labelSecondsLeft.setText(Integer.toString(totalSeconds - minutes * 60));
    }

    @FXML
    private Button buttonBuyResources;
    @FXML
    private Button buttonSellProducts;
    @FXML
    private Button buttonStartProduction;
    @FXML
    private Button buttonStartConstruction;
    @FXML
    private Button buttonGetLoan;

    private void updateMenuVisibility() {
        try {
            int phase = RequestSender.getRequestSender().sessionInfo().getCurrentPhase();
            updateMenuVisibility(phase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMenuVisibility(int phase) {
        buttonBuyResources.setDisable(true);
        buttonSellProducts.setDisable(true);
        buttonStartProduction.setDisable(true);
        buttonStartConstruction.setDisable(true);
        buttonGetLoan.setDisable(true);
        switch (phase) {
            case Restrictions.SEND_BID_RESOURCES_PHASE: {
                buttonBuyResources.setDisable(false);
            }
            break;
            case Restrictions.PRODUCTION_PHASE: {
                buttonStartProduction.setDisable(false);
            }
            break;
            case Restrictions.SEND_BID_PRODUCTS_PHASE: {
                buttonSellProducts.setDisable(false);
            }
            break;
            case Restrictions.NEW_LOAN_PHASE: {
                buttonGetLoan.setDisable(false);
            }
            break;
            case Restrictions.BUILDING_AND_AUTOMATION_PHASE: {
                buttonStartConstruction.setDisable(false);
            }
            break;
        }
    }

    private void updateSeniorMarker() {

    }

    public void showBuyResourcesForm(ActionEvent event) {
        Forms.openFormAsModal("BuyUnitsOfResources");
    }

    public void showSellProductsForm(ActionEvent event) {
        Forms.openFormAsModal("SellUnitsOfProducts");
    }

    public void showStartProductionForm(ActionEvent event) {
        Forms.openFormAsModal("Production");
    }

    public void showStartConstructionForm(ActionEvent event) {
        Forms.openFormAsModal("Build");
    }

    public void showGetLoanForm(ActionEvent event) {
        Forms.openFormAsModal("Loan");
    }

    public void showHelpForm(ActionEvent event) {

    }

    public void nextPhase(ActionEvent event) {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().nextPhase();
            labelMonth.setText(Integer.toString(sessionPackage.getCurrentMonth()));
            labelPhase.setText(Integer.toString(sessionPackage.getCurrentPhase()));
            updateMenuVisibility(sessionPackage.getCurrentPhase());
            fillAllProfiles(sessionPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interruptGameForMe(ActionEvent event) {
        //Прерывает игру и выводит игрока из сессии
    }

    /**
     * Возвращает элемент по его fxId, формируемому автоматически по следующему соглашению:
     * <br>
     * type_entityName_amount_number
     * <br>
     * где: <br>type - мнемоническое название элемента (imageView, text, ...)
     * <br>entityName - любое название сущности, которую описывает объект на форме
     * <br>number - номер на форме (1...MAX_INT), либо 0 (означает объект в области текущего игрока)
     *
     * @param type       тип поля (text, image_view)
     * @param entityName сущность, описываемая полем (например, money)
     * @param number     порядковый номер (0 - текущий игрок, 1...N - оставшиеся)
     * @param descr      дополнительная характеристика (amount, avatar, ...)
     * @return объект узла на форма
     */
    private Object getElementById(String type, String entityName, String descr, int number) {
        String fxId = type + '_' + entityName + '_' + descr + '_' + number;
        if (mainPane != null) {
            return getNamespace().get(fxId);
        }
        return null;
    }
}
