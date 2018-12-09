package samara.university.server;

import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Подсистема хода
 */
public class Turn {
    private Map<Player, Action> playersActions;
    private GameLog gameLog;
    private int currentMonth;
    private int currentPhase;

    public Turn(GameLog log) {
        currentPhase = 1;
        currentMonth = 1;
        gameLog = log;
        playersActions = new HashMap<>();
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void toNextMonth() {
        if (currentMonth < Restrictions.MAX_MONTHS_COUNT && currentPhase == Restrictions.MAX_PHASES_COUNT) {
            currentMonth++;
            currentPhase = 1;
            Session session = Session.getSession();
            session.setSeniorPlayer(session.getBank().nextSeniorPlayer(session.getPlayers(), session.getSeniorPlayer()));
        }
    }

    public void toNextPhase() {
        if (currentPhase < Restrictions.MAX_PHASES_COUNT) {
            currentPhase++;
        } else if (currentMonth < Restrictions.MAX_MONTHS_COUNT) {
            currentPhase = 1;
        }
        /*
        При завершении фазы все данные выгружаются в лог.

        Т.к. есть данные, которые следует отслеживать на протяжении
        еще нескольких ходов, сюда нужно добавить доп.функции,
        которые будут изменять состояние игрока, если истекло время
        ожидания события (например, увеличить число фабрик, если прошло
        5 месяцев).
         */
    }

    /**
     * Вспомогательный класс, описывающих хранилище данных о действии игрока
     * в текущей фазе хода.
     */
    public static class Action {
        /*
        В данном классе описываются добавляемые данные, а не новые значения счетов игрока.

        Т.е. если у него было 5 фабрик, а 1 новую он начал строить, в данном объекте
        будет храниться значение 1, а не 6!
         */
        private String playerName;

        private int currentMonth;
        private int currentPhase;

        private int unitsOfProducts;    //ЕГП
        private int unitsOfResources;   //ЕСМ
        private int unitOfProductsPrice;    //цена, за которую продает ЕГП
        private int unitOfResourcePrice;    //цена, за которую покупает ЕСМ
        private int money;                  //деньги
        private int workingFactories;                       //работающие обычные фабрики
        private int workingAutomatedFactories;              //работающие авто-фабрики
        private int underConstructionFactories;             //строящиеся обычные фабрики
        private int underConstructionAutomatedFactories;    //строящиеся авто-фабрики
        private int inAutomationNowFactories;               //автоматизируемые сейчас фабрики

        //Игрок ничего не делает в данной фазе хода
        public Action() {
        }

        //Игрок совершает хотя бы 1 из действий
        public Action(int unitsOfProducts,
                      int unitsOfResources,
                      int money,
                      int workingFactories,
                      int workingAutomatedFactories,
                      int underConstructionFactories,
                      int underConstructionAutomatedFactories,
                      int inAutomationNowFactories) {
            this.unitsOfProducts = unitsOfProducts;
            this.unitsOfResources = unitsOfResources;
            this.money = money;
            this.workingFactories = workingFactories;
            this.workingAutomatedFactories = workingAutomatedFactories;
            this.underConstructionFactories = underConstructionFactories;
            this.underConstructionAutomatedFactories = underConstructionAutomatedFactories;
            this.inAutomationNowFactories = inAutomationNowFactories;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Action action = (Action) o;
            return unitsOfProducts == action.unitsOfProducts &&
                    unitsOfResources == action.unitsOfResources &&
                    money == action.money &&
                    workingFactories == action.workingFactories &&
                    workingAutomatedFactories == action.workingAutomatedFactories &&
                    underConstructionFactories == action.underConstructionFactories &&
                    underConstructionAutomatedFactories == action.underConstructionAutomatedFactories &&
                    inAutomationNowFactories == action.inAutomationNowFactories;
        }

        @Override
        public int hashCode() {
            return Objects.hash(unitsOfProducts, unitsOfResources, money, workingFactories, workingAutomatedFactories, underConstructionFactories, underConstructionAutomatedFactories, inAutomationNowFactories);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(playerName).append("] ");
            builder.append("Ход ").append(currentMonth);
            builder.append(", ");
            builder.append("фаза ").append(currentPhase);
            builder.append(": ");
            switch (currentPhase) {
                case 1: //Постоянные издержки
                    builder.append("");
                    break;
                case 2: //Банк предлагает количество и мин.цену ЕСМ

                    break;
                case 3: //Банк принимает заявки на ЕСМ

                    break;
                case 4: //Игроки производят ЕГП

                    break;
                case 5: //Игроки продают продукцию

                    break;
                case 6: //Выплата ссудного процента

                    break;
                case 7: //Погашение ссуд

                    break;
                case 8: //Получение ссуд

                    break;
                case 9: //Заявки на строительство

                    break;
            }
            return builder.toString();
        }
    }
}
