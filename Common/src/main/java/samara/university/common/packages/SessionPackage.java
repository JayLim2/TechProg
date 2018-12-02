package samara.university.common.packages;

import samara.university.common.entities.Player;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Вспомогательный класс пакета данных о сессии
 * <p>
 * Требуется для передачи информаации о сессии клиенту
 */
public class SessionPackage implements Serializable {
    private LocalDateTime startTime;
    private List<Player> players;
    private Player currentSeniorPlayer;
    private int currentPhase;
    private int currentMonth;

    public SessionPackage(LocalDateTime startTime, List<Player> players, Player currentSeniorPlayer, int currentPhase, int currentMonth) {
        this.startTime = startTime;
        this.players = players;
        this.currentSeniorPlayer = currentSeniorPlayer;
        this.currentPhase = currentPhase;
        this.currentMonth = currentMonth;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentSeniorPlayer() {
        return currentSeniorPlayer;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionPackage that = (SessionPackage) o;
        return Objects.equals(startTime, that.startTime) &&
                Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, players);
    }
}
