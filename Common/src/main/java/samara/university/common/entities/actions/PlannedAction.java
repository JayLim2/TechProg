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

    public int getCount() {
        return count;
    }

    public int getMoney() {
        return money;
    }

    public boolean isSign() {
        return sign;
    }

    /**
     * Создание отложенного действия
     *
     * @param type          тип отложенного действия
     * @param delayingMonth месяц, в которое создано действие
     * @param delayingPhase фаза, в которую создано действие
     * @param countMonths   количество месяцев до исполнения действия
     * @param countPhases   количество фаз до исполнения действия
     */
    public PlannedAction(PlannedActionType type, Player player, int delayingMonth, int delayingPhase, int countMonths, int countPhases) {
        this.type = type;
        this.player = player;
        this.executionMonth = delayingMonth + countMonths;
        this.executionPhase = delayingPhase + countPhases;
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

    public enum PlannedActionType {
        BUY_RESOURCES,
        SELL_PRODUCTS,
        COMPLETE_BUILDING_FACTORY,
        COMPLETE_AUTOMATION_FACTORY,
        COMPLETE_PRODUCTION,
        PAY_LOANS
    }
}
