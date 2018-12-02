package samara.university.common.entities;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, описывающий аватары игроков
 * <p>
 * Объект хранит путь до файла изображения.
 * Создается с помощью фабричного метода createAvatar.
 * <p>
 * В классе также имеется статическая коллекция стандартных
 * предустановленных аватаров, доступ к которой осуществляется
 * с помощью метода getDefaultAvatar.
 */
public class Avatar implements Serializable {
    private static final List<Avatar> defaultAvatars = new ArrayList<>();
    private String imagePath;

    private Avatar(String imagePath) {
        this.imagePath = imagePath;
    }

    public static Avatar createAvatar(String imagePath) {
        Path path = Paths.get(imagePath);
        File file = path.toFile();
        if (file.exists()) {
            return new Avatar(imagePath);
        }
        return null;
    }

    public static Avatar getEmptyAvatar() {
        return getDefaultAvatar(10);
    }

    public static Avatar getDefaultAvatar(int id) {
        return id >= 0 ? new Avatar("/avatars/" + (id + 1) + ".jpg") : null;
    }

    public String getPath() {
        return imagePath;
    }
}
