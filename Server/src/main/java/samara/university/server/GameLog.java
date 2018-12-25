package samara.university.server;

import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Подсистема ведения журнала игры
 */
public class GameLog {
    private List<String> actions;

    public GameLog() {
        actions = new ArrayList<>();
    }

    public void log(Player player, int currentMonth, int currentPhase, String action) {
        actions.add(Actions.build(player, currentMonth, currentPhase, action));
    }

    /**
     * Генерация файла с журналом игры
     */
    public String export() {
        StringBuilder builder = new StringBuilder();
        for (String action : actions) {
            builder.append(action).append("\r\n");
        }
        return builder.toString();
        /*String dateStr = DateTimeFormatter.ofPattern("YYYY-MM-DD-HH-MM-SS").format(LocalDateTime.now());
        String fileName = "gamelog-"+dateStr+".log";
        File file = new File(fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
                PrintWriter out = new PrintWriter(new FileWriter(file));
                out.print(builder.toString());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    /**
     * Вспомогательный класс, описывающих хранилище данных о действии игрока
     * в текущей фазе хода.
     */
    public static class Actions {

        public static String build(Player player, int currentMonth, int currentPhase, String action) {
            return main(player, currentMonth, currentPhase) + action;
        }

        private static String main(Player player, int currentMonth, int currentPhase) {
            String string = "[" + player.getName() + "] ";
            string += "Ход " + currentMonth + ", фаза " + currentPhase + ": ";
            return string;
        }

        public static String logRegularCosts(int regularCosts) {
            return "Игрок заплатил налоги в размере: " + regularCosts + " у.е.";
        }

        public static String logApprovingBid(boolean isApproved, boolean type, int count, int price) {
            String string = isApproved ? "Одобрена " : "Не одобрена ";
            string += "заявка на";
            if (!type) {
                string += "покупку " + count + " ЕСМ по цене " + price + " (итого: -" + (count * price) + ")";
            } else {
                string += "продажу " + count + " ЕГП по цене " + price + " (итого: +" + (count * price) + ")";
            }
            return string;
        }

        public static String logProducted(int count) {
            return "Произведено " + count + " ЕГП";
        }

        //------------- Строительство и автоматизация фабрик
        public static String logStartConstruction(boolean isAutomated) {
            return "Начал строительство " + (isAutomated ? "автоматизированной" : "обычной") + " фабрики за " + (isAutomated ? Restrictions.BUILDING_AUTOMATED_FACTORY_PRICE : Restrictions.BUILDING_FACTORY_PRICE) + " у.е.";
        }

        public static String logEndConstruction(boolean isAutomated) {
            return "Завершил строительство " + (isAutomated ? "автоматизированной" : "обычной") + " фабрики";
        }

        public static String logAutomation(boolean start) {
            String string = start ? "Начал" : "Завершил";
            string += " автоматизацию фабрики. Оплачена ";
            string += start ? "первая" : "вторая";
            string += " половина: " + (Restrictions.AUTOMATION_FACTORY_PRICE / 2);
            return string;
        }
        //--------------------------------------------------

        //--------- loans
        public static String logNewLoan(int amount) {
            return "Взял ссуду на сумму " + amount + " у.е.";
        }

        public static String logPayLoan(int amount) {
            return "Выплатил ссуду на сумму " + amount + " у.е.";
        }

        public static String logPayLoanPercent(int amount) {
            return "Выплатил ссудный процент на сумму " + amount + " у.е.";
        }
        //----------------

        //-------- Производство ЕГП
        public static String logStartProduction(int count, int price) {
            return "Начал производство " + count + " ЕГП по цене: " + price + " у.е.";
        }

        public static String logEndProduction(int count) {
            return "Завершил производство " + count + " ЕГП";
        }
        //-------------------------

        public static String logBankrupt() {
            return "Стал банкротом.";
        }
    }
}
