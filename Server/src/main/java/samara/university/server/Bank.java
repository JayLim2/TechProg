package samara.university.server;

import samara.university.common.constants.ProbabilityTable;
import samara.university.common.constants.ProductPriceLevelTable;
import samara.university.common.constants.ResourcePriceLevelTable;
import samara.university.common.entities.Bid;
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
        calculateReserves();
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
        Bid bid = Bid.createBid(player, type, count, price);
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


    private static final int NORMAL_MODE_COUNT = 1;
    private static final int NORMAL_MODE_PRICE = 2000;
    private static final int OPTIMIZED_MODE_COUNT = 2;
    private static final int OPTIMIZED_MODE_PRICE = 3000;

    /**
     * Подача заявки на начало производства ЕГП
     *
     * @param player игрок
     * @param count  количество, введенное игроком
     * @param price  общая стоимость производства
     */
    public void startProduction(Player player, int count, int price) {

    }

    /**
     * Получение суммы постоянных издержек
     * (удерживаются в начале каждого хода)
     *
     * @return сумма издержек
     */
    public int getRegularCosts(Player player) {
        return 300 * player.getUnitsOfResources() +
                500 * player.getUnitsOfProducts() +
                1000 * player.getWorkingFactories() +
                1500 * player.getWorkingAutomatedFactories();
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
                int val = o1.getPrice() - o2.getPrice();
                if (val == 0) {
                    val = Session.getSession().isSeniorPlayer(o1.getPlayer()) ? 1
                            : Session.getSession().isSeniorPlayer(o2.getPlayer()) ? -1
                            : 0;
                }
                return val;
            }
        });
        for (int i = 0; i < bids.size() || reserveUnitsOfResources > 0; i++) {
            Bid bid = bids.get(i);
            if (bid.getCount() == 0 || bid.getPrice() < minResourcePrice) {
                continue;
            }
            Player player = bid.getPlayer();
            if (bid.getCount() >= reserveUnitsOfResources) {
                player.setMoney(reserveUnitsOfResources * bid.getPrice());
                player.setUnitsOfResources(player.getUnitsOfResources() + reserveUnitsOfResources);
                reserveUnitsOfResources = 0;
                bids.clear();
                return;
            } else {
                player.setMoney(bid.getCount() * bid.getPrice());
                player.setUnitsOfResources(player.getUnitsOfResources() + bid.getCount());
                reserveUnitsOfResources -= bid.getCount();
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
                int val = o2.getPrice() - o1.getPrice();
                if (val == 0) {
                    val = Session.getSession().isSeniorPlayer(o1.getPlayer()) ? 1
                            : Session.getSession().isSeniorPlayer(o2.getPlayer()) ? -1
                            : 0;
                }
                return val;
            }
        });
        for (int i = 0; i < bids.size() || reserveUnitsOfProducts > 0; i++) {
            Bid bid = bids.get(i);
            if (bid.getCount() == 0 || bid.getPrice() > maxProductPrice) {
                continue;
            }
            Player player = bid.getPlayer();
            if (bid.getCount() >= reserveUnitsOfProducts) {
                player.setMoney(reserveUnitsOfProducts * bid.getPrice());
                player.setUnitsOfProducts(player.getUnitsOfProducts() + reserveUnitsOfProducts);
                reserveUnitsOfProducts = 0;
                bids.clear();
                return;
            } else {
                player.setMoney(bid.getCount() * bid.getPrice());
                player.setUnitsOfProducts(player.getUnitsOfProducts() + bid.getCount());
                reserveUnitsOfProducts -= bid.getCount();
            }
        }
        bids.clear();
    }

    public void calculateReserves() {
        int playersCount = Session.getSession().playersCount();
        int nextLevel = ProbabilityTable.nextLevel();
        System.out.println("players count: " + playersCount);
        System.out.println("next level: " + nextLevel);
        ResourcePriceLevelTable.setPlayersCount(playersCount);
        ProductPriceLevelTable.setPlayersCount(playersCount);

        //Рассчитать количество ЕСМ
        reserveUnitsOfResources = ResourcePriceLevelTable.getUnitsCount(nextLevel);

        //Рассчитать минимальную цену ЕСМ
        minResourcePrice = ResourcePriceLevelTable.getMinPrice(nextLevel);

        //Рассчитать количество ЕГП
        reserveUnitsOfProducts = ProductPriceLevelTable.getUnitsCount(nextLevel);

        //Рассчитать максимальную цену ЕГП
        maxProductPrice = ProductPriceLevelTable.getMaxPrice(nextLevel);
    }

    /**
     * @return количество ЕСМ
     */
    public int getResourcesCount() {
        return reserveUnitsOfResources;
    }

    /**
     * @return количество ЕГП
     */
    public int getProductsCount() {
        return reserveUnitsOfProducts;
    }

    /**
     * @return минимальная цена продажи банком ЕСМ
     */
    public int getMinResourcePrice() {
        return minResourcePrice;
    }

    /**
     * @return максимальная цена скупки банком ЕГП
     */
    public int getMaxProductPrice() {
        return maxProductPrice;
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
}
