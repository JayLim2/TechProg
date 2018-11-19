package server.constants;

/**
 * Статический вспомогательный класс данных
 * <p>
 * По количеству игроков возвращает:
 * - максимальную цену ЕГП, за которую может купить банк
 * - количество ЕГП, которое может купить банк
 */
public class ProductPriceLevelTable {
    private static int playersCount;

    public static final int LEVEL_1_MAX_PRICE = 6500;
    public static final int LEVEL_2_MAX_PRICE = 6000;
    public static final int LEVEL_3_MAX_PRICE = 5500;
    public static final int LEVEL_4_MAX_PRICE = 5000;
    public static final int LEVEL_5_MAX_PRICE = 4500;

    public static void setPlayersCount(int count) {
        playersCount = count > 0 ? count : 0;
    }

    @SuppressWarnings("Duplicates")
    public int getMaxPrice(int level) {
        if (playersCount == 0) return 0;

        switch (level) {
            case 1:
                return LEVEL_1_MAX_PRICE;
            case 2:
                return LEVEL_2_MAX_PRICE;
            case 3:
                return LEVEL_3_MAX_PRICE;
            case 4:
                return LEVEL_4_MAX_PRICE;
            case 5:
                return LEVEL_5_MAX_PRICE;
            default:
                return 0;
        }
    }

    public int getUnitsCount() {
        if (playersCount == 0) return 0;

        switch (playersCount) {
            case 1:
                return 3 * playersCount;
            case 2:
                return Math.floorDiv(5 * playersCount, 2);
            case 3:
                return 2 * playersCount;
            case 4:
                return Math.floorDiv(3 * playersCount, 2);
            case 5:
                return playersCount;
            default:
                return 0;
        }
    }
}
