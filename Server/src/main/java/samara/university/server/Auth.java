package samara.university.server;

import samara.university.common.entities.Avatar;
import samara.university.common.entities.Player;

/**
 * Подсистема авторизации
 */
public class Auth {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 15;
    private static final int MAX_COUNT_PLAYERS = 4;

    public static void register(Session session, String name, int defaultAvatarId) {
        if (session.playersCount() == MAX_COUNT_PLAYERS) return;

        if (name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH) {
            Avatar avatar = Avatar.getDefaultAvatar(defaultAvatarId);
            if (avatar != null) {
                session.register(new Player(name, avatar));
            }
        }
        //выброс ошибки
    }

    public static void register(Session session, String name, String avatarPath) {
        if (session.playersCount() == MAX_COUNT_PLAYERS) return;

        if (name.length() >= MIN_NAME_LENGTH && name.length() <= MAX_NAME_LENGTH) {
            Avatar avatar = Avatar.createAvatar(avatarPath);
            if (avatar != null) {
                session.register(new Player(name, avatar));
            }
        }
        //выброс ошибки
    }
}
