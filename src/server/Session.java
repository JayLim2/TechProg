package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Игровая сессия
 */
public class Session {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 15;
    private static final int MAX_COUNT_MONTHS = 36;
    private static final int MAX_COUNT_PLAYERS = 4;
    private static final int PHASE_LENGTH_IN_SECONDS = 120; //длительность фазы 120 сек

    private List<Player> players;
    private int currentMonth;   //номер хода
    private int currentPhase;   //номер фазы

    public Session() {
        players = new LinkedList<>();
    }

    public void register(String name, int defaultAvatarId) {
        if (players.size() == MAX_COUNT_PLAYERS) return;

        if (name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH) {
            Avatar avatar = Avatar.getDefaultAvatar(defaultAvatarId);
            if (avatar != null) {
                players.add(new Player(name, avatar));
            }
        }
        //выброс ошибки
    }

    public void register(String name, String avatarPath) {
        if (players.size() == MAX_COUNT_PLAYERS) return;

        if (name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH) {
            Avatar avatar = Avatar.createAvatar(avatarPath);
            if (avatar != null) {
                players.add(new Player(name, avatar));
            }
        }
        //выброс ошибки
    }
}
