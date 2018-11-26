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

    static {
        /*final Pattern pattern = Pattern.compile("([0-9]+).jpg");
        System.out.println("ROOT PATH ");
        System.out.println(Paths.get("/avatars").toAbsolutePath());
        System.out.println();
        try (Stream<Path> images = Files.walk(Paths.get("/avatars")).sorted(new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                Matcher matcher1 = pattern.matcher(o1.toString());
                Matcher matcher2 = pattern.matcher(o2.toString());
                String file1 = "";
                String file2 = "";
                if (matcher1.find()) {
                    file1 = matcher1.group(1);
                }
                if (matcher2.find()) {
                    file2 = matcher2.group(1);
                }
                int fileNum1 = !file1.isEmpty() ? Integer.parseInt(file1) : -1;
                int fileNum2 = !file2.isEmpty() ? Integer.parseInt(file2) : -1;
                return Integer.compare(fileNum1, fileNum2);
            }
        })) {
            images
                    .skip(1)
                    .forEachOrdered(new Consumer<Path>() {
                        @Override
                        public void accept(Path path) {
                            System.out.println(path);
                            defaultAvatars.add(createAvatar(path.toString()));
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

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

    public static Avatar getDefaultAvatar(int id) {
        return id >= 0 ? new Avatar("/avatars/" + (id + 1) + ".jpg") : null;
    }

    public String getPath() {
        return imagePath;
    }
}
