package samara.university.common.constants;

/**
 * Статический вспомогательный класс данных
 * <p>
 * По количеству игроков возвращает:
 * - минимальную цену ЕСМ, за которую может продать банк
 * - количество ЕСМ, которое может продать банк
 */
public class ResourcePriceLevelTable {
    private static int playersCount;

    public static final int LEVEL_1_MIN_PRICE = 800;
    public static final int LEVEL_2_MIN_PRICE = 650;
    public static final int LEVEL_3_MIN_PRICE = 500;
    public static final int LEVEL_4_MIN_PRICE = 400;
    public static final int LEVEL_5_MIN_PRICE = 300;

    public static void setPlayersCount(int count) {
        playersCount = count > 0 ? count : 0;
    }

    @SuppressWarnings("Duplicates")
    public static int getMinPrice(int level) {
        if (playersCount == 0) return 0;

        switch (level) {
            case 1:
                return LEVEL_1_MIN_PRICE;
            case 2:
                return LEVEL_2_MIN_PRICE;
            case 3:
                return LEVEL_3_MIN_PRICE;
            case 4:
                return LEVEL_4_MIN_PRICE;
            case 5:
                return LEVEL_5_MIN_PRICE;
            default:
                return 0;
        }
    }

    public static int getUnitsCount(int level) {
        if (playersCount == 0) return 0;

        switch (level) {
            case 1:
                return playersCount;
            case 2:
                return Math.floorDiv(3 * playersCount, 2);
            case 3:
                return 2 * playersCount;
            case 4:
                return Math.floorDiv(5 * playersCount, 2);
            case 5:
                return 3 * playersCount;
            default:
                return 0;
        }
    }
}
