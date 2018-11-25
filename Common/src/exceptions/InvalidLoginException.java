package exceptions;

/**
 * Непроверяемое исключение
 * Выбрасывается в случае нарушения ограничений на имя игрока
 */
public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
