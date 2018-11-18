package server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Подсистема хода
 */
public class Turn {
    private static final int MAX_COUNT_MONTHS = 36;
    private static final int MAX_COUNT_PHASES = 6;

    private Map<Player, Action> playersActions;
    private GameLog gameLog;
    private int currentMonth;
    private int currentPhase;

    public Turn(GameLog log) {
        gameLog = log;
        playersActions = new HashMap<>();
    }

    public void toNextMonth() {
        if (currentMonth < MAX_COUNT_MONTHS && currentPhase == MAX_COUNT_PHASES) {
            currentMonth++;
            currentPhase = 1;
        }
    }

    public void toNextPhase() {
        if (currentPhase < MAX_COUNT_PHASES) {
            currentPhase++;
        } else if (currentMonth < MAX_COUNT_MONTHS) {
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

        // FIXME: 18.11.2018 сделать столько методов, сколько фаз, с соответствующими аргумнтами
        private void runPhase1() {

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
    }
}
