package samara.university.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Подсистема ведения журнала игры
 */
public class GameLog {
    private List<Turn.Action> actions;

    public GameLog() {
        actions = new ArrayList<>();
    }

    public void log(Turn.Action action) {
        actions.add(action);
    }

    /**
     * Генерация файла с журналом игры
     */
    public void export() {
        StringBuilder builder = new StringBuilder();

        for (Turn.Action action : actions) {
            builder.append(action);
        }
    }
}
