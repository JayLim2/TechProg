package samara.university.server;

import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Подсистема игровой сессии
 */
public class Session {
    private static Session session;
    private static int lastId = 0;

    private LocalDateTime startTime;
    private List<Player> players;
    private Player seniorPlayer;
    private Turn turn;
    private GameLog gameLog;
    private Bank bank;

    private Player winner;

    private int countPlayersReadyForNextPhase;

    private boolean gameStarted;

    private Session() {
        players = new ArrayList<>();
        gameLog = new GameLog();
        turn = new Turn();
        startTime = LocalDateTime.now();
    }

    /**
     * Данная игра поддерживает только одну игровую сессию.
     * Поэтому данный метод создает новую сессию только в первый раз.
     *
     * @return текущая сессия
     */
    public synchronized static Session getSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    /**
     * Закрытие сессии
     */
    public synchronized static void terminateSession() {
        session = null;
    }

    public int playersCount() {
        return players.size();
    }

    public boolean isAvailable() {
        return !gameStarted && playersCount() <= Restrictions.MAX_PLAYERS_COUNT;
    }

    //----------------------- Ready to next phase --------------------------
    public boolean isAllReady() {
        return countPlayersReadyForNextPhase >= players.size() - 1;
    }

    public synchronized void makeReady() {
        countPlayersReadyForNextPhase++;
    }

    public synchronized void resetReady() {
        countPlayersReadyForNextPhase = 0;
    }
    //---------------------------------------------------------------------

    //Уникальный логин в рамках сессии
    public boolean isUniqueLogin(String login) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(login)) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean register(Player player) {
        if (player != null && isAvailable()) {
            player.setId(++lastId);
            players.add(player);
            if (seniorPlayer == null) {
                seniorPlayer = player;
            }
            return true;
        }
        return false;
    }

    /**
     * Удаление игрока из игры
     *
     * @param player игрок
     * @return признак того, закончилась ли игра
     */
    public synchronized boolean unregister(Player player) {
        players.remove(player);
        return true;
        //Если остался один игрок - он объявляется победителем
        /*if (players.size() == 1) {
            winner = players.iterator().next();
            return true;
        }
        return false;*/
    }

    /**
     * Определение самого обеспеченного на текущий момент игрока
     * <p>
     * Используется для определения победителя в случае, если игроков к концу игры осталось больше 1.
     *
     * @param minResourcesPrice мин.цена ЕСМ
     * @param maxProductsPrice  макс.цена ЕГП
     * @return самый обеспеченный игрок
     */
    public Player getMaxPlayer(int minResourcesPrice, int maxProductsPrice) {
        if (playersCount() > 0) {
            Player max = players.get(0);
            for (Player current : players) {
                if (current.getCapital(minResourcesPrice, maxProductsPrice) > max.getCapital(minResourcesPrice, maxProductsPrice)) {
                    max = current;
                }
            }
            return max;
        }
        return null;
    }

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    //-------------- стартовала ли игра
    public boolean isGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    //---------------------------------

    public GameLog getGameLog() {
        return gameLog;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Player getSeniorPlayer() {
        return seniorPlayer;
    }

    public synchronized void setSeniorPlayer(Player nextSeniorPlayer) {
        this.seniorPlayer = nextSeniorPlayer;
    }

    public Player getWinner() {
        return winner;
    }

    public synchronized void setWinner(Player winner) {
        this.winner = winner;
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
