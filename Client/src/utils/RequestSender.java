package utils;

import enums.Command;
import packages.SessionPackage;

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
        try {
            socket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(inputStream);

            isConnected = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        if (!isConnected) {
            connect();
        }
        sendCommand(Command.AUTH);
        outputStream.writeUTF(name);
        outputStream.writeInt(avatarId);
        outputStream.flush();
    }

    public SessionPackage updateInfo() throws IOException, ClassNotFoundException {
        if (!isConnected) {
            connect();
        }
        System.out.println();

        sendCommand(Command.UPDATE_SEARCH_PLAYERS_INFO);
        SessionPackage sessionPackage = (SessionPackage) objectInputStream.readObject();

        /*LocalDateTime startTime = (LocalDateTime)objectInputStream.readObject();
        int countPlayers = inputStream.readInt();
        List<Player> players = new ArrayList<>();
        System.out.println("date: " + startTime);
        for (int i = 0; i < countPlayers; i++) {
            players.add((Player) objectInputStream.readObject());
            System.out.println("player: " + players.get(i));
        }
        System.out.println();*/
        return sessionPackage;
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
