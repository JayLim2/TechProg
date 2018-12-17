package samara.university.server;

import samara.university.common.constants.ProbabilityTable;
import samara.university.common.constants.ProductPriceLevelTable;
import samara.university.common.constants.ResourcePriceLevelTable;
import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Bid;
import samara.university.common.entities.Player;
import samara.university.common.entities.actions.PlannedAction;

import java.util.*;

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

    private List<PlannedAction> plannedActions; //отложенные действия

    public Bank() {
        bids = new ArrayList<>();
        plannedActions = new LinkedList<>();
        //calculateReserves();
    }

    /**
     * Метод, который вызывается в начале каждой фазы.
     * <p>
     * Проверяется, существуют ли запланированные действия.
     * Среди них ищутся действия, которые должны быть выполнены в текущую
     * фазу и выполняются.
     */
    public void handlePlannedActions() {
        int currentMonth = Session.getSession().getTurn().getCurrentMonth();
        int currentPhase = Session.getSession().getTurn().getCurrentPhase();

        //Other planned actions
        if (plannedActions.size() > 0) {
            Player player;
            PlannedAction.PlannedActionType type;
            Iterator<PlannedAction> iterator = plannedActions.iterator();
            while (iterator.hasNext()) {
                PlannedAction action = iterator.next();
                System.out.println("planned action " + action);
                if (action.isActionDone(currentPhase, currentMonth)) {
                    player = action.getPlayer();
                    type = action.getType();
                    System.out.println("\tplayer: " + player);
                    System.out.println("\ttype: " + type);
                    switch (type) {
                        case BUY_RESOURCES: {
                            buy();
                        }
                        break;
                        case SELL_PRODUCTS: {
                            sell();
                        }
                        break;
                        case COMPLETE_BUILDING_FACTORY: {
                            if (action.isSign()) {
                                player.setWorkingAutomatedFactories(
                                        player.getWorkingAutomatedFactories() + action.getCount()
                                );
                            } else {
                                player.setWorkingFactories(
                                        player.getWorkingFactories() + action.getCount()
                                );
                            }
                        }
                        break;
                        case COMPLETE_AUTOMATION_FACTORY: {
                            player.setWorkingFactories(
                                    player.getWorkingFactories() - action.getCount()
                            );
                            player.setWorkingAutomatedFactories(
                                    player.getWorkingAutomatedFactories() + action.getCount()
                            );
                        }
                        break;
                        case PAY_SECOND_PART_FOR_AUTOMATION: {
                            player.setMoney(
                                    player.getMoney() - action.getMoney()
                            );
                        }
                        break;
                        case COMPLETE_PRODUCTION: {
                            player.setUnitsOfProducts(
                                    player.getUnitsOfProducts() + action.getCount()
                            );
                        }
                        break;
                        case PAY_LOAN: {
                            player.setMoney(
                                    player.getMoney() - action.getMoney()
                            );
                            player.setTotalLoans(
                                    player.getTotalLoans() - action.getMoney()
                            );
                        }
                        break;
                        case PAY_LOAN_PERCENT: {
                            player.setMoney(
                                    player.getMoney() - Math.round(player.getTotalLoans() * Restrictions.LOAN_PERCENT)
                            );
                        }
                        break;
                    }
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Переход на следующую фазу
     * <p>
     * Банк в начале каждой фазы пересчитывает свои резервы, выставляет цены
     * и обрабатывает запланированные действия.
     */
    public void nextPhase() {
        int currentPhase = Session.getSession().getTurn().getCurrentPhase();
        Session.getSession().getTurn().resetTurnTime();
        switch (currentPhase) {
            case Restrictions.REGULAR_COSTS_PHASE: {
                List<Player> players = Session.getSession().getPlayers();
                for (Player player : players) {
                    player.setMoney(player.getMoney() - getRegularCosts(player));
                }
            }
            break;
            case Restrictions.CALCULATE_RESERVES_PHASE: {
                calculateReserves();
            }
            break;
            case Restrictions.SEND_BID_RESOURCES_PHASE: {
                planHandlingBids(Restrictions.BUY_RESOURCES_BID);
            }
            break;
            case Restrictions.PRODUCTION_PHASE: { //4
                // TODO: 16.12.2018
            }
            break;
            case Restrictions.SEND_BID_PRODUCTS_PHASE: {
                planHandlingBids(Restrictions.SELL_PRODUCTS_BID);
            }
            break;
            case Restrictions.PAY_LOAN_PERCENT_PHASE: {
                // TODO: 16.12.2018
                /*List<Player> players = Session.getSession().getPlayers();
                for (Player player : players) {
                    int loan = player.getTotalLoans();
                    float percentLoan = 0.01F * loan;
                }*/
            }
            break;
            case Restrictions.PAY_LOAN_PHASE: { //7
                // TODO: 16.12.2018
            }
            break;
            case Restrictions.NEW_LOAN_PHASE: {
                // TODO: 16.12.2018  
            }
            break;
            case Restrictions.MAX_PHASES_COUNT: { //9
                // TODO: 16.12.2018
            }
            break;
        }

        Session.getSession().getTurn().toNextPhase();
        Session.getSession().getTurn().toNextMonth();

        currentPhase = Session.getSession().getTurn().getCurrentPhase();

        if (currentPhase >= Restrictions.PRODUCTION_PHASE
                && currentPhase <= Restrictions.MAX_PHASES_COUNT) {
            handlePlannedActions();
        }
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
     * Запланировать обрабаотку заявок
     *
     * @param type тип заявок
     */
    public void planHandlingBids(boolean type) {
        plannedActions.add(
                new PlannedAction(
                        !type ? PlannedAction.PlannedActionType.BUY_RESOURCES
                                : PlannedAction.PlannedActionType.SELL_PRODUCTS,
                        null,
                        Session.getSession().getTurn().getCurrentMonth(),
                        Session.getSession().getTurn().getCurrentPhase() + 1
                )
        );
    }

    /**
     * Подача заявки на начало производства ЕГП
     *
     * @param player игрок
     * @param count  количество, введенное игроком
     * @param price  общая стоимость производства
     */
    public void startProduction(Player player, int count, int price) {
        player.setMoney(
                player.getMoney() - price
        );
        PlannedAction plannedAction = new PlannedAction(
                PlannedAction.PlannedActionType.COMPLETE_PRODUCTION,
                player,
                Session.getSession().getTurn().getCurrentMonth(),
                Session.getSession().getTurn().getCurrentPhase() + 1
        );
        plannedAction.setCount(count);
        reconcilePlannedActionPlayer(plannedAction);
        plannedActions.add(plannedAction);
    }

    /**
     * Построить фабрику для игрока player (автоматизированную или обычную)
     *
     * @param player      игрок
     * @param isAutomated свойство "автоматизированная"
     */
    public void buildFactory(Player player, boolean isAutomated) {
        int totalFactories = player.getWorkingFactories()
                + player.getWorkingAutomatedFactories()
                + player.getUnderConstructionFactories()
                + player.getUnderConstructionAutomatedFactories();
        if (totalFactories < Restrictions.MAX_FACTORIES_COUNT) {
            if (isAutomated) {
                player.setUnderConstructionAutomatedFactories(
                        player.getUnderConstructionAutomatedFactories() + 1
                );
            } else {
                player.setUnderConstructionFactories(
                        player.getUnderConstructionFactories() + 1
                );
            }
            player.setMoney(
                    player.getMoney() - (isAutomated ? Restrictions.BUILDING_AUTOMATED_FACTORY_PRICE : Restrictions.BUILDING_FACTORY_PRICE)
            );
            PlannedAction plannedAction = new PlannedAction(
                    PlannedAction.PlannedActionType.COMPLETE_BUILDING_FACTORY,
                    player,
                    Session.getSession().getTurn().getCurrentMonth() + (isAutomated ? Restrictions.BUILDING_AUTOMATED_FACTORY_MONTHS : Restrictions.BUILDING_FACTORY_MONTHS),
                    1
            );
            plannedAction.setSign(isAutomated);
            plannedAction.setCount(1);
            reconcilePlannedActionPlayer(plannedAction);
            plannedActions.add(plannedAction);
        }
    }

    /**
     * Автоматизировать одну обычную фабрику игроку player.
     *
     * @param player игрок
     */
    public void automateExistingFactory(Player player) {
        if (player.getWorkingFactories() > 0) {
            //Строительство фабрики
            PlannedAction plannedAction = new PlannedAction(
                    PlannedAction.PlannedActionType.COMPLETE_AUTOMATION_FACTORY,
                    player,
                    Session.getSession().getTurn().getCurrentMonth() + Restrictions.AUTOMATION_FACTORY_MONTHS,
                    1
            );
            plannedAction.setCount(1);
            reconcilePlannedActionPlayer(plannedAction);
            plannedActions.add(plannedAction);

            //Оплата второй части суммы за автоматизацию
            plannedAction = new PlannedAction(
                    PlannedAction.PlannedActionType.PAY_SECOND_PART_FOR_AUTOMATION,
                    player,
                    Session.getSession().getTurn().getCurrentMonth() + Restrictions.AUTOMATION_FACTORY_MONTHS - 1,
                    1
            );
            plannedAction.setMoney(Restrictions.AUTOMATION_FACTORY_PRICE / 2);
            reconcilePlannedActionPlayer(plannedAction);
            plannedActions.add(plannedAction);
        }
    }

    /**
     * Получить ссуду на 12 месяцев
     *
     * @param player игрок
     * @param amount желаемый размер ссуды
     */
    public void newLoan(Player player, int amount) {
        int halfCapital = player.getCapital(minResourcePrice, maxProductPrice) / 2;
        if (amount <= halfCapital) {
            int currentMonth = Session.getSession().getTurn().getCurrentMonth();
            PlannedAction plannedAction;
            for (int i = 1; i <= Restrictions.LOAN_MONTHS; i++) {
                plannedAction = new PlannedAction(
                        PlannedAction.PlannedActionType.PAY_LOAN_PERCENT,
                        player,
                        currentMonth + i,
                        Restrictions.PAY_LOAN_PERCENT_PHASE
                );
                reconcilePlannedActionPlayer(plannedAction);
                plannedActions.add(plannedAction);
            }

            plannedAction = new PlannedAction(
                    PlannedAction.PlannedActionType.PAY_LOAN,
                    player,
                    currentMonth + Restrictions.LOAN_MONTHS,
                    Restrictions.PAY_LOAN_PHASE
            );
            reconcilePlannedActionPlayer(plannedAction);
            plannedActions.add(plannedAction);
        }
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
        bids.sort(ascBidCmp);
        for (int i = 0; i < bids.size() && reserveUnitsOfResources > 0; i++) {
            Bid bid = bids.get(i);
            reconcileBidPlayer(bid);

            if (bid.getCount() == 0 || bid.getPrice() < minResourcePrice) {
                continue;
            }
            handleBid(bid, Restrictions.BUY_RESOURCES_BID);
            if (bid.getCount() >= reserveUnitsOfResources) {
                bids.clear();
                return;
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
        bids.sort(descBidCmp);
        for (int i = 0; i < bids.size() && reserveUnitsOfProducts > 0; i++) {
            Bid bid = bids.get(i);
            reconcileBidPlayer(bid);

            if (bid.getCount() == 0 || bid.getPrice() > maxProductPrice) {
                continue;
            }
            handleBid(bid, Restrictions.SELL_PRODUCTS_BID);
            if (bid.getCount() >= reserveUnitsOfProducts) {
                bids.clear();
                return;
            }
        }
        bids.clear();
    }

    private void reconcileBidPlayer(Bid bid) {
        Player player = Session.getSession().getPlayerById(bid.getPlayer().getId());
        if (player != null) {
            bid.setPlayer(player);
        }
    }

    private void reconcilePlannedActionPlayer(PlannedAction plannedAction) {
        Player sessionPlayer = Session.getSession().getPlayerById(plannedAction.getPlayer().getId());
        if (sessionPlayer != null) {
            Player player = plannedAction.getPlayer();

            //Обновляем эквивалентного игрока в сессии
            sessionPlayer.setMoney(player.getMoney());
            sessionPlayer.setUnitsOfResources(player.getUnitsOfResources());
            sessionPlayer.setUnitsOfProducts(player.getUnitsOfProducts());
            sessionPlayer.setWorkingFactories(player.getWorkingFactories());
            sessionPlayer.setWorkingAutomatedFactories(player.getWorkingAutomatedFactories());
            sessionPlayer.setUnderConstructionFactories(player.getUnderConstructionFactories());
            sessionPlayer.setUnderConstructionAutomatedFactories(player.getUnderConstructionAutomatedFactories());
            sessionPlayer.setTotalLoans(player.getTotalLoans());

            //Привязываем запланированное действие к сессионному игроку
            plannedAction.setPlayer(sessionPlayer);
        }
    }

    private void handleBid(Bid bid, boolean type) {
        Player player = bid.getPlayer();
        int count;
        if (!type) {
            count = Math.min(bid.getCount(), reserveUnitsOfResources);
            player.setMoney(player.getMoney() - count * bid.getPrice());
            player.setUnitsOfResources(player.getUnitsOfResources() + count);
            reserveUnitsOfResources -= count;
        } else {
            count = Math.min(bid.getCount(), reserveUnitsOfProducts);
            player.setMoney(player.getMoney() + count * bid.getPrice());
            player.setUnitsOfProducts(player.getUnitsOfProducts() - count);
            reserveUnitsOfProducts -= count;
        }
    }

    private BidComparator ascBidCmp = new BidComparator(false);
    private BidComparator descBidCmp = new BidComparator(true);

    private class BidComparator implements Comparator<Bid> {
        private boolean isDescending;

        public BidComparator(boolean isDescending) {
            this.isDescending = isDescending;
        }

        @Override
        public int compare(Bid o1, Bid o2) {
            int val = isDescending ?
                    o2.getPrice() - o1.getPrice() :
                    o1.getPrice() - o2.getPrice();
            if (val == 0) {
                val = Session.getSession().isSeniorPlayer(o1.getPlayer()) ? 1
                        : Session.getSession().isSeniorPlayer(o2.getPlayer()) ? -1
                        : 0;
            }
            return val;
        }
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

        System.out.println("\n____ NEW TABLE ______");
        System.out.println(minResourcePrice);
        System.out.println(reserveUnitsOfResources);
        System.out.println();
        System.out.println(maxProductPrice);
        System.out.println(reserveUnitsOfProducts);
        System.out.println("_____________________\n");
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
