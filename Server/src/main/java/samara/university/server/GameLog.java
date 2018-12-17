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
            builder.append(action).append("\n");
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

        public static String logConstruction(boolean start, boolean isAutomated) {
            String string = start ? "Начал" : "Завершил";
            string += " строительство " + (isAutomated ? "автоматизированной" : "обычной") + " фабрики за " + (isAutomated ? Restrictions.BUILDING_AUTOMATED_FACTORY_PRICE : Restrictions.BUILDING_FACTORY_PRICE) + " у.е.";
            return string;
        }

        public static String logAutomation(boolean start) {
            String string = start ? "Начал" : "Завершил";
            string += " автоматизацию фабрики. Оплачена ";
            string += start ? "первая" : "вторая";
            string += " половина: " + (Restrictions.AUTOMATION_FACTORY_PRICE / 2);
            return string;
        }

        public static String logProduction(boolean start, int count, boolean mode) {
            String string = start ? "Начал" : "Завершил";
            string += " производство " + count + " ЕГП в режиме: " + (mode ? "1 ЕГП за 2000 у.е. " : "2 ЕГП за 3000 у.е.");
            return string;
        }

        public static String logBankrupt() {
            return "Стал банкротом.";
        }
    }
}
