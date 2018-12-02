package samara.university.common.packages;

import java.io.Serializable;

/**
 * Вспомогательный класс пакета информации о состоянии резервов банка
 */
public class BankPackage implements Serializable {
    private int reserveUnitsOfResources;    //сколько в этом месяце продает ЕСМ
    private int minResourcePrice;           //минимальная цена за 1 ЕСМ

    private int reserveUnitsOfProducts;     //сколько в этом месяце покупает ЕГП
    private int maxProductPrice;            //максимальная цена за 1 ЕГП

    public BankPackage(int reserveUnitsOfResources, int minResourcePrice, int reserveUnitsOfProducts, int maxProductPrice) {
        this.reserveUnitsOfResources = reserveUnitsOfResources;
        this.minResourcePrice = minResourcePrice;
        this.reserveUnitsOfProducts = reserveUnitsOfProducts;
        this.maxProductPrice = maxProductPrice;
    }

    public int getReserveUnitsOfResources() {
        return reserveUnitsOfResources;
    }

    public int getMinResourcePrice() {
        return minResourcePrice;
    }

    public int getReserveUnitsOfProducts() {
        return reserveUnitsOfProducts;
    }

    public int getMaxProductPrice() {
        return maxProductPrice;
    }
}
