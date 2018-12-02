package samara.university.server;

import samara.university.common.constants.ProductPriceLevelTable;
import samara.university.common.constants.ResourcePriceLevelTable;
import samara.university.common.entities.Factory;
import samara.university.common.entities.Player;

import java.util.ArrayList;
import java.util.Comparator;
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
        Bid bid = new Bid(player, type, count, price);
        bids.add(bid);
    }

    /**
     * [Вспомогательный метод]
     * Объявляет банку, что все заявки получены
     * Предполагается, что банку сообщать об этом будет игровая сессия (samara.university.server.Session)
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
        bids.sort(new Comparator<Bid>() {
            @Override
            public int compare(Bid o1, Bid o2) {
                int val = o1.price - o2.price;
                if (val == 0) {
                    val = Session.getSession().isSeniorPlayer(o1.player) ? 1
                            : Session.getSession().isSeniorPlayer(o2.player) ? -1
                            : 0;
                }
                return val;
            }
        });
        for (int i = 0; i < bids.size() || reserveUnitsOfResources > 0; i++) {
            Bid bid = bids.get(i);
            if (bid.count == 0 || bid.price < minResourcePrice) {
                continue;
            }
            Player player = bid.player;
            if (bid.count >= reserveUnitsOfResources) {
                player.setMoney(reserveUnitsOfResources * bid.price);
                player.setUnitsOfResources(player.getUnitsOfResources() + reserveUnitsOfResources);
                reserveUnitsOfResources = 0;
                bids.clear();
                return;
            } else {
                player.setMoney(bid.count * bid.price);
                player.setUnitsOfResources(player.getUnitsOfResources() + bid.count);
                reserveUnitsOfResources -= bid.count;
            }
        }
        bids.clear();
    }

    /**
     * Покупка ЕГП (продажа - со стороны игрока)
     * <p>
     * Выполняется по алгоритму в соответствии с правилами.
     */
    public void sell() {
        bids.sort(new Comparator<Bid>() {
            @Override
            public int compare(Bid o1, Bid o2) {
                int val = o2.price - o1.price;
                if (val == 0) {
                    val = Session.getSession().isSeniorPlayer(o1.player) ? 1
                            : Session.getSession().isSeniorPlayer(o2.player) ? -1
                            : 0;
                }
                return val;
            }
        });
        for (int i = 0; i < bids.size() || reserveUnitsOfProducts > 0; i++) {
            Bid bid = bids.get(i);
            if (bid.count == 0 || bid.price > maxProductPrice) {
                continue;
            }
            Player player = bid.player;
            if (bid.count >= reserveUnitsOfProducts) {
                player.setMoney(reserveUnitsOfProducts * bid.price);
                player.setUnitsOfProducts(player.getUnitsOfProducts() + reserveUnitsOfProducts);
                reserveUnitsOfProducts = 0;
                bids.clear();
                return;
            } else {
                player.setMoney(bid.count * bid.price);
                player.setUnitsOfProducts(player.getUnitsOfProducts() + bid.count);
                reserveUnitsOfProducts -= bid.count;
            }
        }
        bids.clear();
    }

    public void calculateReserves() {
        int playersCount = Session.getSession().playersCount();
        //Рассчитать количество ЕСМ
        ResourcePriceLevelTable.setPlayersCount(playersCount);

        //Рассчитать минимальную цену ЕСМ

        //Рассчитать количество ЕГП
        ProductPriceLevelTable.setPlayersCount(playersCount);


        //Рассчитать максимальную цену ЕГП

    }

    /**
     * Построить фабрику для игрока player (автоматизированную или обычную)
     *
     * @param player      игрок
     * @param isAutomated свойство "автоматизированная"
     * @param month       месяц начала строительства
     */
    public void buildFactory(Player player, boolean isAutomated, int month) {
        Factory factory = new Factory(month, isAutomated);
        if (!isAutomated) {
            player.getFactories().add(factory);
        } else {
            player.getAutoFactories().add(factory);
        }
    }

    /**
     * Автоматизировать одну обычную фабрику игроку player.
     *
     * @param player игрок
     * @param month  месяц начала автоматизации
     */
    public void automateExistingFactory(Player player, int month) {
        List<Factory> factories = player.getFactories();
        if (factories.size() > 0) {
            Factory factory = factories.get(0);
            factory.startAutomation(month);
            factories.remove(0);
            player.getAutoFactories().add(factory);
        }
    }

    /**
     * Определение следующего старшего игрока
     *
     * @param allPlayers    список всех игроков
     * @param currentSenior текущий старший игрок
     * @return следующий старшик игрок
     */
    public Player nextSeniorPlayer(List<Player> allPlayers, Player currentSenior) {
        int index = allPlayers.indexOf(currentSenior);
        if (index >= 0 && index < allPlayers.size()) {
            if (index == allPlayers.size() - 1) {
                return allPlayers.get(0);
            } else {
                return allPlayers.get(++index);
            }
        }
        return null;
    }

    private static class Bid {
        private Player player;
        private boolean type; //FALSE - покупка ЕСМ, TRUE - продажа ЕГП
        private int count;
        private int price;

        private Bid(Player player, boolean type, int count, int price) {
            this.player = player;
            this.type = type;
            this.count = count;
            this.price = price;
        }

        public static Bid createBid(Player player, boolean type, int count, int price) {
            if (count != 0) {
                return new Bid(player, type, count, price);
            }
            return null;
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
