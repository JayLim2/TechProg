package samara.university.server;

import samara.university.common.constants.Restrictions;
import samara.university.common.entities.Player;

import java.time.LocalDateTime;

/**
 * Подсистема хода
 */
public class Turn {
    private LocalDateTime phaseStartTime;
    private int currentMonth;
    private int currentPhase;

    public Turn() {
        currentPhase = 1;
        currentMonth = 1;
    }

    public synchronized void resetTurnTime() {
        phaseStartTime = LocalDateTime.now();
    }

    public LocalDateTime getPhaseStartTime() {
        return phaseStartTime;
    }

    public void setPhaseStartTime(LocalDateTime phaseStartTime) {
        this.phaseStartTime = phaseStartTime;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public void toNextMonth() {
        /*if (currentMonth < Restrictions.MAX_MONTHS_COUNT && currentPhase == Restrictions.MAX_PHASES_COUNT) {
            currentMonth++;
            currentPhase = 1;
            Session session = Session.getSession();
            session.setSeniorPlayer(session.getBank().nextSeniorPlayer(session.getPlayers(), session.getSeniorPlayer()));
        }*/
    }

    public void toNextPhase() {
        //System.out.println("__-_ current phase : " + currentPhase);
        if (currentPhase < Restrictions.MAX_PHASES_COUNT) {
            currentPhase++;
        } else if (currentMonth < Restrictions.MAX_MONTHS_COUNT) {
            currentMonth++;
            Session session = Session.getSession();
            System.out.println("old senior: " + session.getSeniorPlayer());
            Player nextSenior = session.getBank().nextSeniorPlayer(session.getPlayers(), session.getSeniorPlayer());
            System.out.println(nextSenior);
            session.setSeniorPlayer(nextSenior);
            System.out.println("new senior: " + session.getSeniorPlayer());
            System.out.println();
            currentPhase = 1;
        }
        /*
        При завершении фазы все данные выгружаются в лог.

        Т.к. есть данные, которые следует отслеживать на протяжении
        еще нескольких ходов, сюда нужно добавить доп.функции,
        которые будут изменять состояние игрока, если истекло время
        ожидания события (например, увеличить число фабрик, если прошло
        5 месяцев).
         */
    }
}
