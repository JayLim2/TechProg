package samara.university.common.entities.actions;

import samara.university.common.entities.Player;

public class PlannedAction {
    private PlannedActionType type;
    private Player player;
    private int executionMonth;
    private int executionPhase;

    private int count;
    private int money;
    private boolean sign;

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean isSign() {
        return sign;
    }

    /**
     * Создание запланированного действия
     *
     * @param type              тип действия
     * @param player            игрок
     * @param executionMonth    месяц, в который действие завершится
     * @param executionPhase    фаза месяца, в которой действие завершится
     */
    public PlannedAction(PlannedActionType type, Player player, int executionMonth, int executionPhase) {
        this.type = type;
        this.player = player;
        this.executionMonth = executionMonth;
        this.executionPhase = executionPhase;
    }

    /**
     * Проверка, завершилось ли действие (сейчас)
     *
     * @param phase текущая фаза
     * @param month текущий месяц
     * @return признак завершения действия
     */
    public boolean isActionDone(int phase, int month) {
        return (executionPhase == phase) && (executionMonth == month);
    }

    /**
     * @return тип отложенного действия
     */
    public PlannedActionType getType() {
        return type;
    }

    /**
     * @return игрок, с которым связано действие
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player новый игрок, связанный с действием
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public enum PlannedActionType {
        BUY_RESOURCES,
        SELL_PRODUCTS,
        COMPLETE_BUILDING_FACTORY,
        COMPLETE_AUTOMATION_FACTORY,
        PAY_SECOND_PART_FOR_AUTOMATION,
        COMPLETE_PRODUCTION,
        PAY_LOAN,
        PAY_LOAN_PERCENT
    }
}
