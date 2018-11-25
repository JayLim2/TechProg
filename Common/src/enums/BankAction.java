package enums;

import java.io.Serializable;

/**
 * Спецификация дополнительных уточняющих команд, связанных с действиями банка
 */
public enum BankAction implements Serializable {
    BUY_RESOURCE,

    SELL_PRODUCT,

    BUILD_FACTORY,

    AUTOMATE_FACTORY,

    GET_LOAN,

    PAY_LOAN,

    PAY_REGULAR_COSTS
}
