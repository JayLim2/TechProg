package packages;

import entities.Player;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Вспомогательный класс пакета данных о сессии
 * <p>
 * Требуется для передачи информаации о сессии клиенту
 */
public class SessionPackage implements Serializable {
    private LocalDateTime startTime;
    private Set<Player> players;

    public SessionPackage(LocalDateTime startTime, Set<Player> players) {
        this.startTime = startTime;
        this.players = players;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
