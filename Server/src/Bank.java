import entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Подсистема банка
 */
public class Bank {
    private int reserveUnitsOfResources;    //сколько в этом месяце продает ЕСМ
    private int minResourcePrice;           //минимальная цена за 1 ЕСМ

    private int reserveUnitsOfProducts;     //сколько в этом месяце покупает ЕГП
    private int maxProductPrice;            //максимальная цена за 1 ЕГП

    private List<Bid> bids; //список заявок в банке
    private boolean allBidsReceived; //все заявки получены?

    public Bank() {
        bids = new ArrayList<>();
    }

    /**
     * Подача заявки на покупку ЕСМ или продажу ЕГП
     *
     * @param player ссылка на объект "Игрок"
     * @param type   FALSE - покупка ЕСМ, TRUE - продажа ЕГП
     * @param count  количество товара
     * @param price  цена
     */
    public void sendBid(Player player, boolean type, int count, int price) {
        bids.add(new Bid(player, type, count, price));
    }

    /**
     * [Вспомогательный метод]
     * Объявляет банку, что все заявки получены
     * Предполагается, что банку сообщать об этом будет игровая сессия (Session)
     */
    public void allBidsReceived() {
        allBidsReceived = true;
    }


    /**
     * Продажа ЕСМ (покупка - со стороны игрока)
     * <p>
     * Выполняется по алгоритму в соответствии с правилами.
     */
    public void buy() {

    }

    /**
     * Покупка ЕГП (продажа - со стороны игрока)
     * <p>
     * Выполняется по алгоритму в соответствии с правилами.
     */
    public void sell() {

    }

    private static class Bid {
        private Player player;
        private boolean type; //FALSE - покупка ЕСМ, TRUE - продажа ЕГП
        private int count;
        private int price;

        public Bid(Player player, boolean type, int count, int price) {
            this.player = player;
            this.type = type;
            this.count = count;
            this.price = price;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean isType() {
            return type;
        }

        public int getCount() {
            return count;
        }

        public int getPrice() {
            return price;
        }
    }
}
