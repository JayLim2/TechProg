package samara.university.server;

import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Подсистема игровой сессии
 */
public class Session {
    private static Session session;
    private static int lastId = 0;

    private LocalDateTime startTime;
    private Set<Player> players;
    private Player seniorPlayer;
    private Turn turn;
    private GameLog gameLog;
    private Bank bank;

    private Player winner;

    private int countPlayersReadyForNextPhase;

    private Session() {
        players = ConcurrentHashMap.newKeySet();
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
    public static void terminateSession() {
        session = null;
    }

    public int playersCount() {
        return players.size();
    }

    public boolean isAvailable() {
        return playersCount() <= Restrictions.MAX_PLAYERS_COUNT;
    }

    //----------------------- Ready to next phase --------------------------
    public boolean isAllReady() {
        return countPlayersReadyForNextPhase == players.size() - 1;
    }

    public void makeReady() {
        countPlayersReadyForNextPhase++;
    }

    public void resetReady() {
        countPlayersReadyForNextPhase = 0;
    }
    //---------------------------------------------------------------------

    public void register(Player player) {
        if (player != null && isAvailable()) {
            player.setId(++lastId);
            players.add(player);
            if (seniorPlayer == null) {
                seniorPlayer = player;
            }
        }
    }

    /**
     * Удаление игрока из игры
     *
     * @param player игрок
     * @return признак того, закончилась ли игра
     */
    public boolean unregister(Player player) {
        players.remove(player);
        //Если остался один игрок - он объявляется победителем
        if (players.size() == 1) {
            winner = players.iterator().next();
            return true;
        }
        return false;
    }

    public List<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>(this.players);
        return players;
    }

    public Set<Player> getPlayersSet() {
        return players;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Player getSeniorPlayer() {
        return seniorPlayer;
    }

    public void setSeniorPlayer(Player nextSeniorPlayer) {
        this.seniorPlayer = nextSeniorPlayer;
    }

    public Turn getTurn() {
        return turn;
    }

    public Bank getBank() {
        if (bank == null) {
            bank = new Bank();
        }
        return bank;
    }

    public boolean isSeniorPlayer(Player player) {
        return Objects.equals(player, seniorPlayer);
    }
}
