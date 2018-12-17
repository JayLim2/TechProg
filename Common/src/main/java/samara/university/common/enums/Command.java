package samara.university.common.enums;

import java.io.Serializable;

/**
 * Спецификация команд сервера
 */
public enum Command implements Serializable {
    /**
     * AUTH
     * <p>
     * Авторизует игрока в сессии
     * <p>
     * read:
     * - String с именем игрока
     * - byte с номером аватара
     * <p>
     * write:
     * - Ничего не возвращает.
     */
    AUTH,

    /**
     * ME
     * <p>
     * Возвращает ссылку на игрока клиента
     * Т.е. позволяет определить, какой игрок относится к текущему соединению.
     * </p>
     * <p>
     * read:
     * - String с именем игрока
     * - byte с номером аватара
     * </p>
     * <p>
     * write:
     * - Ничего не возвращает.
     * </p>
     */
    ME,

    /**
     * UPDATE_SESSION_INFO
     * <p>
     * Получает актуальную информацию о текущей сессии.
     * <p>
     * read:
     * - Ничего не принимает.
     * <p>
     * write:
     * - Возвращает Session с достаточно актуальными сведениями
     */
    UPDATE_SESSION_INFO,

    /**
     * BANK_ACTION
     * <p>
     * Инициирует одно из игровых действий:
     * - покупка ЕСМ
     * - продажа ЕГП
     * - получение ссуды
     * - выплата ссуд
     * - выплата издержек
     * - начало строительства фабрики
     * - начало автоматизации фабрики
     * <p>
     * read:
     * - Объект BankAction, описывающий действие
     * <p>
     * write:
     * - Возвращает то, что возвращает принятые BankAction (см. спецификацию BankAction)
     */
    BANK_ACTION,

    /**
     * NEXT_PHASE
     * <p>
     * Команда, объявляющая, что игрок готов к следующей фазе
     * <p>
     * read:
     * - None
     * <p>
     * write:
     * - Флаг готовности
     */
    NEXT_PHASE,

    /**
     * EXIT
     * <p>
     * Безопасно выводит игрока из игровой сессии.
     * <p>
     * read:
     * - Ничего не принимает.
     * <p>
     * write:
     * - Ничего не возвращает.
     */
    EXIT,

    /**
     * RESET_TURN_TIME
     * <p>
     * Сбрасывает время начала фазы на текущее
     */
    RESET_TURN_TIME,

    GET_TURN_TIME,

    WINNER
}
