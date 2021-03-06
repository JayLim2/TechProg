package samara.university.client.utils;

import samara.university.common.entities.Player;
import samara.university.common.enums.BankAction;
import samara.university.common.enums.Command;
import samara.university.common.packages.BankPackage;
import samara.university.common.packages.SessionPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class RequestSender {
    private static final int SERVER_PORT = 7777;
    private static RequestSender requestSender;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private boolean isConnected;

    private RequestSender() {
    }

    public static RequestSender getRequestSender() {
        if (requestSender == null) {
            requestSender = new RequestSender();
        }
        return requestSender;
    }

    /**
     * Установить соединение с сервером и открыть потоки ввода-вывода
     */
    public void connect() {
        if (!isConnected) {
            try {
                socket = new Socket(
                        //InetAddress.getLocalHost(),
                        Inet4Address.getByName("25.31.133.74"),
                        SERVER_PORT
                );
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                isConnected = true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean tryConnectRequest() {
        connect();
        return isConnected;
    }

    public void startGame() throws IOException {
        connect();
        sendCommand(Command.START_GAME);
    }

    public boolean checkLoginUniqueness(String name) throws IOException {
        connect();
        sendCommand(Command.CHECK_LOGIN_UNIQUENESS);
        objectOutputStream.reset();
        objectOutputStream.writeUTF(name);
        objectOutputStream.flush();
        return objectInputStream.readBoolean();
    }

    /**
     * Отправка команды авторизации
     *
     * @param name     имя игрока
     * @param avatarId id выбранного аватара
     * @throws IOException исключение ввода-вывода
     */
    public boolean authPlayer(String name, int avatarId) throws IOException {
        connect();
        sendCommand(Command.AUTH);
        objectOutputStream.reset();
        objectOutputStream.writeUTF(name);
        objectOutputStream.writeInt(avatarId);
        objectOutputStream.flush();
        return objectInputStream.readBoolean();
    }

    /**
     * Отправка запроса на получение ссылки на игрока текущего клиента
     *
     * @return ссылка на объект "Игрок"
     * @throws IOException            исключение ввода-вывода
     * @throws ClassNotFoundException исключение "класс не найден"
     */
    public Player me() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.ME);
        return (Player) objectInputStream.readObject();
    }

    /**
     * Безусловно получает пакет-оболочку данных сессии
     *
     * @return пакет с данными сессии
     * @throws IOException            исключение ввода-вывода
     * @throws ClassNotFoundException исключение "класс не найден"
     */
    public SessionPackage sessionInfo() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.UPDATE_SESSION_INFO);
        return (SessionPackage) objectInputStream.readObject();
    }

    /**
     * Производит следующие изменения:
     * - следующий по очереди игрок становится старшим
     * - изменяется номер фазы
     * - изменяется (в случае необходимости) номер хода
     *
     * @return пакет с данными о сессии
     * @throws IOException            исключение ввода-вывода
     * @throws ClassNotFoundException исключение "класс не найден"
     */
    public SessionPackage nextPhase() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.NEXT_PHASE);
        return (SessionPackage) objectInputStream.readObject();
    }

    public BankPackage bankInfo() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.RESERVES);
        return (BankPackage) objectInputStream.readObject();
    }

    public void sendBid(Player player, boolean type, int count, int price) throws IOException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.SEND_BID);
        objectOutputStream.reset();
        objectOutputStream.writeObject(player);
        objectOutputStream.writeBoolean(type);
        objectOutputStream.writeInt(count);
        objectOutputStream.writeInt(price);
        objectOutputStream.flush();
    }

    public void startProduction(Player player, int count, int totalCost) throws IOException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.START_PRODUCTION);
        objectOutputStream.reset();
        objectOutputStream.writeObject(player);
        objectOutputStream.writeInt(count);
        objectOutputStream.writeInt(totalCost);
        objectOutputStream.flush();
    }

    public void buildFactory(Player player, boolean isAutomated) throws IOException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.BUILD_FACTORY);
        objectOutputStream.reset();
        objectOutputStream.writeObject(player);
        objectOutputStream.writeBoolean(isAutomated);
        objectOutputStream.flush();
    }

    public void automateFactory(Player player) throws IOException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.AUTOMATE_FACTORY);
        objectOutputStream.reset();
        objectOutputStream.writeObject(player);
        objectOutputStream.flush();
    }

    public void newLoan(Player player, int amount, int amountAuto, int count, int countAuto) throws IOException {
        connect();
        sendCommand(Command.BANK_ACTION);
        sendBankAction(BankAction.NEW_LOAN);
        objectOutputStream.reset();
        objectOutputStream.writeObject(player);
        objectOutputStream.writeInt(amount);
        objectOutputStream.writeInt(amountAuto);
        objectOutputStream.writeInt(count);
        objectOutputStream.writeInt(countAuto);
        objectOutputStream.flush();
    }

    public void exit() throws IOException {
        connect();
        sendCommand(Command.EXIT);
        isConnected = false;
    }

    public void resetTurnTime() throws IOException {
        connect();
        sendCommand(Command.RESET_TURN_TIME);
    }

    public LocalDateTime getTurnTime() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.GET_TURN_TIME);
        return (LocalDateTime) objectInputStream.readObject();
    }

    public Player getWinner() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.WINNER);
        return (Player) objectInputStream.readObject();
    }

    public String getGameLog() throws IOException {
        connect();
        sendCommand(Command.GET_GAMELOG);
        return objectInputStream.readUTF();
    }

    /**
     * Вспомогательный метод отправки команды на сервер
     *
     * @param command       команда
     * @throws IOException  исключение ввода-вывода
     */
    private void sendCommand(Command command) throws IOException {
        objectOutputStream.writeInt(command.ordinal());
        objectOutputStream.flush();
    }

    /**
     * Вспомогательный метод отправки банковского действия на сервер
     *
     * @param bankAction банковское действие
     * @throws IOException исключение ввода-вывода
     */
    private void sendBankAction(BankAction bankAction) throws IOException {
        objectOutputStream.writeInt(bankAction.ordinal());
        objectOutputStream.flush();
    }
}
