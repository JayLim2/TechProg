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
     * UPDATE_SEARCH_PLAYERS_INFO
     * <p>
     * Получает актуальную информацию о текущей сессии.
     * <p>
     * read:
     * - Ничего не принимает.
     * <p>
     * write:
     * - Возвращает Session с достаточно актуальными сведениями
     */
    UPDATE_SEARCH_PLAYERS_INFO,

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
     * NEXT_TURN
     * <p>
     * Команда, объявляющая, что игрок готов к следующему ходу
     * <p>
     * read:
     * - None
     * <p>
     * write:
     * - Флаг готовности
     */
    NEXT_TURN,

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
}