package samara.university.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameFieldFormController {
    @FXML
    private AnchorPane mainPane;

    //others
    private Text labelMonth;
    private Text labelPhase;
    private Text labelTimeLeft;

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

    public void showAction(WindowEvent event) {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
            me = RequestSender.getRequestSender().me();
            senior = sessionPackage.getCurrentSeniorPlayer();
            List<Player> players = sessionPackage.getPlayers();
            players.remove(me);

            //Заполняем данные текущего игрока
            fillProfile(me, 0);

            //Заполняем данные остальных игроков
            for (int i = 0; i < players.size(); i++) {
                fillProfile(players.get(i), i + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void hideAction(WindowEvent event) {

    }

    private void fillProfile(Player player, int i) {
        Text text;
        ImageView imageView;

        imageView = (ImageView) getElementById("image_view", "player_profile", "avatar", i);
        imageView.setImage(new Image(player.getAvatar().getPath()));

        imageView = (ImageView) getElementById("image_view", "player_profile", "senior", i);
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
        text.setText(Integer.toString(player.getUnitsOfProducts()));

        text = (Text) getElementById("text", "factories", "amount", i);
        text.setText(Integer.toString(player.getWorkingFactories()));

        text = (Text) getElementById("text", "factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText(Integer.toString(player.getUnderConstructionFactories()));

        text = (Text) getElementById("text", "auto_factories", "amount", i);
        text.setText(Integer.toString(player.getWorkingAutomatedFactories()));

        text = (Text) getElementById("text", "auto_factories" + IN_PROGRESS_SUFFIX, "amount", i);
        text.setText(Integer.toString(player.getUnderConstructionAutomatedFactories()));
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
     * @param type
     * @param entityName
     * @param number
     * @return
     */
    private Object getElementById(String type, String entityName, String descr, int number) {
        String fxId = type + '_' + entityName + '_' + descr + '_' + number;
        if (mainPane != null) {
            return getNamespace().get(fxId);
        }
        return null;
    }
}
