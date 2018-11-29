package samara.university.client.utils;

import samara.university.common.entities.Player;
import samara.university.common.enums.Command;
import samara.university.common.packages.SessionPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestSender {
    private static final int SERVER_PORT = 7777;
    private static RequestSender requestSender;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectInputStream objectInputStream;

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
     * Соединение установлено
     *
     * @return флаг
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Установить соединение с сервером и открыть потоки ввода-вывода
     */
    public void connect() {
        if (!isConnected) {
            try {
                socket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());

                isConnected = true;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Отправка команды авторизации
     *
     * @param name     имя игрока
     * @param avatarId id выбранного аватара
     * @throws IOException исключение ввода-вывода
     */
    public void authPlayer(String name, int avatarId) throws IOException {
        connect();
        sendCommand(Command.AUTH);
        outputStream.writeUTF(name);
        outputStream.writeInt(avatarId);
        outputStream.flush();
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
     * Получает пакет-оболочку данных сессии
     *
     * @return пакет с данными сессии (дата создания и список игроков)
     * @throws IOException            исключение ввода-вывода
     * @throws ClassNotFoundException исключение "класс не найден"
     */
    public SessionPackage sessionInfo() throws IOException, ClassNotFoundException {
        connect();
        sendCommand(Command.UPDATE_SESSION_INFO);
        return (SessionPackage) objectInputStream.readObject();
    }

    /**
     * Вспомогательный метод отправки команды на сервер
     *
     * @param command команда
     * @throws IOException исключение ввода-вывода
     */
    private void sendCommand(Command command) throws IOException {
        outputStream.writeInt(command.ordinal());
        outputStream.flush();
    }
}
