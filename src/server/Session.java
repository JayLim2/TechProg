package server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Подсистема игровой сессии
 */
public class Session {
    private static final int PHASE_LENGTH_IN_SECONDS = 120; //длительность фазы 120 сек

    private Set<Player> players;
    private Player seniorPlayer;
    private Turn turn;
    private GameLog gameLog;

    public Session() {
        players = new HashSet<>();
        gameLog = new GameLog();
        turn = new Turn(gameLog);
    }

    public int playersCount() {
        return players.size();
    }

    public void register(Player player) {
        if (player != null) {
            players.add(player);
            if (seniorPlayer == null) {
                seniorPlayer = player;
            }
        }
    }
}
