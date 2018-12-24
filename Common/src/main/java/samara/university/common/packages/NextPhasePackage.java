package samara.university.common.packages;

import java.io.Serializable;

public class NextPhasePackage implements Serializable {
    private SessionPackage sessionPackage;
    private boolean playerReady;

    public NextPhasePackage(SessionPackage sessionPackage, boolean playerReady) {
        this.sessionPackage = sessionPackage;
        this.playerReady = playerReady;
    }

    public SessionPackage getSessionPackage() {
        return sessionPackage;
    }

    public boolean isPlayerReady() {
        return playerReady;
    }
}
