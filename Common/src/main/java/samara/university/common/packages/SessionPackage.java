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
    private int ssid;

    private LocalDateTime sessionStartTime;
    private LocalDateTime phaseStartTime;
    private List<Player> players;
    private Player currentSeniorPlayer;
    private int currentPhase;
    private int currentMonth;

    public SessionPackage(LocalDateTime sessionStartTime, LocalDateTime phaseStartTime, List<Player> players, Player currentSeniorPlayer, int currentPhase, int currentMonth) {
        this.sessionStartTime = sessionStartTime;
        this.phaseStartTime = phaseStartTime;
        this.players = players;
        this.currentSeniorPlayer = currentSeniorPlayer;
        this.currentPhase = currentPhase;
        this.currentMonth = currentMonth;
    }

    public int getSsid() {
        return ssid;
    }

    public void setSsid(int ssid) {
        this.ssid = ssid;
    }

    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    public LocalDateTime getPhaseStartTime() {
        return phaseStartTime;
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
        return ssid == that.ssid &&
                currentPhase == that.currentPhase &&
                currentMonth == that.currentMonth &&
                Objects.equals(sessionStartTime, that.sessionStartTime) &&
                Objects.equals(phaseStartTime, that.phaseStartTime) &&
                Objects.equals(players, that.players) &&
                Objects.equals(currentSeniorPlayer, that.currentSeniorPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssid, sessionStartTime, phaseStartTime, players, currentSeniorPlayer, currentPhase, currentMonth);
    }
}
