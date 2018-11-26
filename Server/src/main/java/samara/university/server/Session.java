package samara.university.server;

import samara.university.common.entities.Player;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Подсистема игровой сессии
 */
public class Session {
    private static Session session;
    private static final int PHASE_LENGTH_IN_SECONDS = 120; //длительность фазы 120 сек
    private static final int MAX_COUNT_PLAYERS = 4;

    private LocalDateTime startTime;
    private Set<Player> players;
    private Player seniorPlayer;
    private Turn turn;
    private GameLog gameLog;

    private Session() {
        players = new HashSet<>();
        gameLog = new GameLog();
        turn = new Turn(gameLog);
        startTime = LocalDateTime.now();
    }

    /**
     * Данная игра поддерживает только одну игровую сессию.
     * Поэтому данный метод создает новую сессию только в первый раз.
     *
     * @return текущая сессия
     */
    public static Session getSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    /**
     * Закрытие сессии
     */
    public static void terminate() {
        session = null;
    }

    public int playersCount() {
        return players.size();
    }

    public boolean isAvailable() {
        return playersCount() < MAX_COUNT_PLAYERS;
    }

    public void register(Player player) {
        if (player != null && isAvailable()) {
            System.out.println("REGISTRATION PLAYER");
            players.add(player);
            if (seniorPlayer == null) {
                seniorPlayer = player;
            }
        }
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isSeniorPlayer(Player player) {
        return Objects.equals(player, seniorPlayer);
    }
}
