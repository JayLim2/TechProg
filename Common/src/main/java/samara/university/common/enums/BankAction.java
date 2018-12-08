package samara.university.common.enums;

import java.io.Serializable;

/**
 * Спецификация дополнительных уточняющих команд, связанных с действиями банка
 */
public enum BankAction implements Serializable {
    RESERVES,

    SEND_BID,

    START_PRODUCTION,

    BUILD_FACTORY,

    AUTOMATE_FACTORY,

    GET_LOAN,

    PAY_LOAN,

    PAY_REGULAR_COSTS
}
