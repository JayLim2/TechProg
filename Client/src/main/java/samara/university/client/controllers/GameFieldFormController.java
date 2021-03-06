package samara.university.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import samara.university.client.utils.Forms;
import samara.university.client.utils.PredefinedAlerts;
import samara.university.client.utils.RequestSender;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Avatar;
import samara.university.common.entities.Player;
import samara.university.common.packages.BankPackage;
import samara.university.common.packages.SessionPackage;

import java.awt.*;
import java.io.File;
import java.net.SocketException;
import java.net.URI;
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

    private boolean initialized = false;

    @Override
    public void showAction(WindowEvent event) {
        try {
            //gamelogArea.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            //gamelogArea.setDisable(true);
            RequestSender.getRequestSender().startGame();

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

            //Заполняем профили игроков
            fillAllProfiles(sessionPackage);

            //Информация о ходе
            fillTurn(sessionPackage.getCurrentPhase(), sessionPackage.getCurrentMonth(), totalSeconds);

            //Информация о банковских резервах
            fillBankReserves();

            //Запустить обратный отсчет времени хода
            phaseCountdown();

            //Запустить циклическое обновление клиента
            cyclicalUpdater();
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideAction(WindowEvent event) {

    }

    private void clearProfile(int i) {
        Text text;
        ImageView imageView;

        imageView = (ImageView) getElementById("image_view", "player_profile", "avatar", i);
        imageView.setImage(new Image(Avatar.getEmptyAvatar().getPath()));

        imageView = (ImageView) getElementById("image_view", "player_profile", "senior", i);
        imageView.setVisible(false);

        text = (Text) getElementById("text", "player_profile", "login", i);
        text.setText("-");

        text = (Text) getElementById("text", "money", "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "resources", "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "products", "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "products" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "factories", "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "auto_factories", "amount", i);
        text.setText("-");

        text = (Text) getElementById("text", "auto_factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText("-");
    }

    private void fillAllProfiles(SessionPackage sessionPackage) {
        try {
            List<Player> players = sessionPackage.getPlayers();

            me = RequestSender.getRequestSender().me();

            if (me.isBankrupt()) {
                updaterThread.interrupt();
                stopPhaseCountdown();
                stopCyclicalUpdater();
                Forms.closeForm("GameField");
                Forms.openForm("GameOver");
                return;
            }

            players.remove(me);

            //Очистка профилей несуществующих игроков
            for (int i = players.size(); i < Restrictions.MAX_PLAYERS_COUNT; i++) {
                clearProfile(i);
            }

            //Заполняем данные текущего игрока
            fillProfile(me, 0);

            //Заполняем данные остальных игроков
            for (int i = 0; i < players.size(); i++) {
                fillProfile(players.get(i), i + 1);
            }
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillProfile(Player player, int i) {
        Text text;
        ImageView imageView;

        imageView = (ImageView) getElementById("image_view", "player_profile", "avatar", i);
        imageView.setImage(new Image(player.getAvatar().getPath()));

        imageView = (ImageView) getElementById("image_view", "player_profile", "senior", i);
        imageView.setVisible(senior != null && player != null && Objects.equals(player, senior));

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
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
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
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean gameFinished = false;

    private void phaseCountdown() {
        //Timeline timeline = new Timeline();

        KeyFrame frame = new KeyFrame(ONE_SECOND_DURATION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (gameFinished) {
                    stopPhaseCountdown();
                    stopCyclicalUpdater();
                    Forms.closeForm("GameField");
                    Forms.openForm("GameResults");
                    return;
                }

                if (totalSeconds <= 0 || phaseCountdownStopped) {
                    countdown.stop();
                    countdown.getKeyFrames().clear();
                }

                if (totalSeconds > 0) {
                    totalSeconds--;
                    updateTimeFields();
                } else {
                    nextPhase(null);
                    phaseCountdown();
                }
            }
        });

        countdown.getKeyFrames().add(frame);
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    public void stopPhaseCountdown() {
        phaseCountdownStopped = true;
    }

    //-------------------- Циклическое обновление клиента раз в N секунд ---------------

    /*
    FIXME здесь есть баг: если выходит текущий старший игрок, неверно обновляется форма
    еще возможно есть баг с логгированием банкротства, возможно также только в этой ситуации
     */

    private static final Duration CYCLE_PERIOD = Duration.seconds(3);
    private boolean cyclicalUpdateStopped = false;

    private Timeline updater = new Timeline();
    private Timeline countdown = new Timeline();

    private Thread updaterThread = null;

    private void cyclicalUpdater() {
        UpdateFormTask updateFormTask = new UpdateFormTask();

        if (updaterThread == null) {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            Platform.runLater(updateFormTask);
                            Thread.sleep(3000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            updaterThread = new Thread(updater);
            updaterThread.setDaemon(true);
            updaterThread.start();
        }
    }

    private void stopCyclicalUpdater() {
        cyclicalUpdateStopped = true;
    }

    private class UpdateFormTask implements Runnable {
        @Override
        public void run() {
            try {
                //Если найден победитель - завершить игру
                if (RequestSender.getRequestSender().getWinner() != null) {
                    stopPhaseCountdown();
                    stopCyclicalUpdater();
                    gameFinished = true;
                    return;
                }

                //Обновить лог игры
                String gamelog = RequestSender.getRequestSender().getGameLog();
                gamelogArea.setText(gamelog);
                //------------------

                SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
                senior = sessionPackage.getCurrentSeniorPlayer();
                if (sessionPackage.getCurrentPhase() == Restrictions.REGULAR_COSTS_PHASE
                        || sessionPackage.getCurrentPhase() == Restrictions.CALCULATE_RESERVES_PHASE
                        || sessionPackage.getCurrentPhase() == Restrictions.PAY_LOAN_PERCENT_PHASE
                        || sessionPackage.getCurrentPhase() == Restrictions.PAY_LOAN_PHASE) {
                    nextPhase(null);
                } else {
                    fillAllProfiles(sessionPackage);
                    labelMonth.setText(Integer.toString(sessionPackage.getCurrentMonth()));
                    labelPhase.setText(Integer.toString(sessionPackage.getCurrentPhase()));
                    updateMenuVisibility(sessionPackage.getCurrentPhase(), sessionPackage.isCurrentPlayerReady());
                }
                fillBankReserves();
                getTurnTime();

                //Thread.sleep(duration);
            } catch (SocketException e) {
                updaterThread.interrupt();
                stopPhaseCountdown();
                stopCyclicalUpdater();
                PredefinedAlerts.connectionResetAlert();
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    @FXML
    private Button buttonNextPhase;

    @Deprecated
    private void updateMenuVisibility() {
        try {
            int phase = RequestSender.getRequestSender().sessionInfo().getCurrentPhase();
            updateMenuVisibility(phase, buttonNextPhase.isDisabled());
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMenuVisibility(int phase, boolean playerIsReady) {
        buttonBuyResources.setDisable(true);
        buttonSellProducts.setDisable(true);
        buttonStartProduction.setDisable(true);
        buttonStartConstruction.setDisable(true);
        buttonGetLoan.setDisable(true);
        buttonNextPhase.setDisable(true);
        switch (phase) {
            case Restrictions.SEND_BID_RESOURCES_PHASE: {
                buttonBuyResources.setDisable(false);
                buttonNextPhase.setDisable(playerIsReady);
            }
            break;
            case Restrictions.PRODUCTION_PHASE: {
                buttonStartProduction.setDisable(false);
                buttonNextPhase.setDisable(playerIsReady);
            }
            break;
            case Restrictions.SEND_BID_PRODUCTS_PHASE: {
                buttonSellProducts.setDisable(false);
                buttonNextPhase.setDisable(playerIsReady);
            }
            break;
            case Restrictions.NEW_LOAN_PHASE: {
                buttonGetLoan.setDisable(false);
                buttonNextPhase.setDisable(playerIsReady);
            }
            break;
            case Restrictions.BUILDING_AND_AUTOMATION_PHASE: {
                buttonStartConstruction.setDisable(false);
                buttonNextPhase.setDisable(playerIsReady);
            }
            break;
        }
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
        try {
            URI uri = new File("help/help.html").toURI();
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            PredefinedAlerts.helpNotFound();
        }
    }

    public void nextPhase(ActionEvent event) {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().nextPhase();
            labelMonth.setText(Integer.toString(sessionPackage.getCurrentMonth()));
            labelPhase.setText(Integer.toString(sessionPackage.getCurrentPhase()));
            updateMenuVisibility(sessionPackage.getCurrentPhase(), sessionPackage.isCurrentPlayerReady());
            fillAllProfiles(sessionPackage);
        } catch (SocketException e) {
            PredefinedAlerts.connectionResetAlert();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interruptGameForMe(ActionEvent event) {
        //Прерывает игру и выводит игрока из сессии
        if (PredefinedAlerts.confirmAlert("Вы действительно хотите выйти из игры?")) {
            try {
                RequestSender.getRequestSender().exit();
            } catch (SocketException e) {
                PredefinedAlerts.connectionResetAlert();
                System.exit(-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updaterThread.interrupt();
            countdown.stop();
            updater.stop();
            Forms.closeForm("GameField");
            Forms.openForm("GameOver");
        }
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
