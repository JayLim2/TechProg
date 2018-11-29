package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.util.List;

public class GameFieldFormController {
    private AnchorPane mainPane;

    //others
    private Text labelMonth;
    private Text labelPhase;
    private Text labelTimeLeft;

    private Player me;

    private static final String IN_PROGRESS_SUFFIX = "in_progress";
    private static final String[] TYPES = new String[]{
            "text", "image_view"
    };

    private static final String[] ENTITIES = new String[]{
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

    public void showAction(ActionEvent event) {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
            List<Player> players = sessionPackage.getPlayers();
            me = RequestSender.getRequestSender().me();

            //Считываем данные текущего игрока
            for (int i = 0; i < players.size() - 1; i++) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
     * @param type
     * @param entityName
     * @param number
     * @return
     */
    private Node getElementById(String type, String entityName, int number, boolean isAmount) {
        String fxId = type + '_' + entityName + (isAmount ? "_amount_" : '_') + (number > 0 ? number : "my");
        System.out.println(fxId);
        if (mainPane != null) {
            return mainPane.lookup(fxId);
        }
        return null;
    }
}
