package samara.university.common.entities;

import java.io.Serializable;

/**
 * Сущность фабрики
 * <p>
 * constructionMonth - месяц постройки или начала автоматизации
 * isAutomated - является ли автоматизированной
 * processFinished - строительство/автоматизация завершены
 */
public class Factory implements Serializable {
    private static final int AUTOMATION_DURATION = 5;
    private static final int CONSTRUCTION_DURATION = 5;

    private int constructionMonth;
    private boolean isAutomated;

    private boolean processFinished;

    public Factory(int constructionMonth, boolean isAutomated) {
        this.constructionMonth = constructionMonth;
        this.isAutomated = isAutomated;
    }

    public int getConstructionMonth() {
        return constructionMonth;
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    public int remainingMonths(int currentMonth, boolean isAutomation) {
        if (processFinished) return 0;

        int remaining = isAutomation ?
                AUTOMATION_DURATION - currentMonth :
                CONSTRUCTION_DURATION - currentMonth;
        if (remaining == 0) {
            processFinished = true;
        }
        return remaining;
    }

    public void startAutomation(int month) {
        constructionMonth = month;
        isAutomated = true;
    }
}
