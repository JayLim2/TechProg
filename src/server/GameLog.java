package server;

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
}
