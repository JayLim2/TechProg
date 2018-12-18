package samara.university.common.constants;

public class Restrictions {
    public static final int MAX_PLAYERS_COUNT = 4;

    public static final int WAIT_TIME_LIMIT_SECONDS = 120;

    public static final int PHASE_LENGTH_IN_SECONDS = 120; //длительность фазы 120 сек

    //bid types

    public static final boolean BUY_RESOURCES_BID = false;

    public static final boolean SELL_PRODUCTS_BID = true;

    //turn

    public static final int MAX_MONTHS_COUNT = 36;

    public static final int MAX_PHASES_COUNT = 9;

    //phases_id

    public static final int REGULAR_COSTS_PHASE = 1;
    public static final int CALCULATE_RESERVES_PHASE = 2;
    public static final int SEND_BID_RESOURCES_PHASE = 3;
    public static final int PRODUCTION_PHASE = 4;
    public static final int SEND_BID_PRODUCTS_PHASE = 5;
    public static final int PAY_LOAN_PERCENT_PHASE = 6;
    public static final int PAY_LOAN_PHASE = 7;
    public static final int NEW_LOAN_PHASE = 8;
    public static final int BUILDING_AND_AUTOMATION_PHASE = 9;

    //factories

    public static final int BUILDING_FACTORY_PRICE = 5000;

    public static final int BUILDING_FACTORY_MONTHS = 5;

    public static final int BUILDING_AUTOMATED_FACTORY_PRICE = 10000;

    public static final int BUILDING_AUTOMATED_FACTORY_MONTHS = 7;

    public static final int AUTOMATION_FACTORY_PRICE = 7000;

    public static final int AUTOMATION_FACTORY_MONTHS = 9;

    public static final int MAX_FACTORIES_COUNT = 6;

    //loans

    public static final int LOAN_MONTHS = 12;

    public static final float LOAN_PERCENT = 0.99f;

    public static final int LOAN_AMOUNT_BY_FACTORY = 5000;

    public static final int LOAN_AMOUNT_BY_AUTOMATED_FACTORY = 5000;

}
