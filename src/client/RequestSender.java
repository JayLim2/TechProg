package client;

import server.enums.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RequestSender {
    private static final int SERVER_PORT = 7777;
    private static RequestSender requestSender;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectOutputStream commandStream;

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
        try {
            socket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            commandStream = new ObjectOutputStream(outputStream);

            authPlayer("TEST_PLAYER", 1);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                inputStream.close();
                commandStream.close();
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
     * @throws IOException
     */
    public void authPlayer(String name, int avatarId) throws IOException {
        commandStream.writeObject(Command.AUTH);
        outputStream.writeUTF(name);
        outputStream.writeByte(avatarId);
        commandStream.flush();
    }
}
