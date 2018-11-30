package samara.university.client.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import samara.university.client.utils.RequestSender;
import samara.university.common.entities.Player;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.util.Comparator;
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

    public void showAction(ActionEvent event) {
        try {
            SessionPackage sessionPackage = RequestSender.getRequestSender().sessionInfo();
            me = RequestSender.getRequestSender().me();
            List<Player> players = sessionPackage.getPlayers().sort(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.equals(me) ? 1 : -1;
                }
            });

            //Считываем данные текущего игрока
            for (int i = 0; i < players.size() - 1; i++) {
                fillForm(players.get(i), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void fillForm(Player player, int i) {
        Text text;
        ImageView imageView;

        imageView = (ImageView) getElementById("image_view", "player_profile", "avatar", (i + 1));
        imageView.setImage(new Image(player.getAvatar().getPath()));

        text = (Text) getElementById("text", "money", "amount", (i + 1));
        text.setText(Integer.toString(player.getMoney()));

        text = (Text) getElementById("text", "resources", "amount", (i + 1));
        text.setText(Integer.toString(player.getUnitsOfResources()));

        text = (Text) getElementById("text", "products", "amount", (i + 1));
        text.setText(Integer.toString(player.getUnitsOfProducts()));

        text = (Text) getElementById("text", "factories", "amount", (i + 1));
        text.setText(Integer.toString(player.getWorkingFactories()));

        text = (Text) getElementById("text", "auto_factories", "amount", (i + 1));
        text.setText(Integer.toString(player.getWorkingAutomatedFactories()));

        text = (Text) getElementById("text", "factories" + IN_PROGRESS_SUFFIX, "amount", (i + 1));
        text.setText(Integer.toString(player.getUnderConstructionFactories()));

        text = (Text) getElementById("text", "auto_factories" + IN_PROGRESS_SUFFIX, "amount", (i + 1));
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
    private Node getElementById(String type, String entityName, String descr, int number) {
        String fxId = type + '_' + entityName + '_' + descr + '_' + (number > 0 ? number : "my");
        System.out.println(fxId);
        if (mainPane != null) {
            return mainPane.lookup(fxId);
        }
        return null;
    }
}
