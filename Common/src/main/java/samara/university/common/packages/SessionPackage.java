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

    public SessionPackage(LocalDateTime startTime, List<Player> players) {
        this.startTime = startTime;
        this.players = players;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public List<Player> getPlayers() {
        return players;
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
