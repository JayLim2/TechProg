package server;

import server.enums.BankAction;
import server.enums.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Обработчик запросов к серверу
 */
public class RequestHandler {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        List<Socket> sockets = new ArrayList<>();
        while (true) {
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            new Thread(new ClientThread(socket)).start();
        }
    }

    private static class ClientThread implements Runnable {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        private Session session;

        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream objectInputStream = null;
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                objectInputStream = new ObjectInputStream(inputStream);
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            //Запрашиваем сессию
            session = Session.getSession();
            if (!session.isAvailable()) {
                return;
            }

            //Если сессия доступна - продолжаем
            while (true) {
                try {
                    Command command = (Command) objectInputStream.readObject();
                    switch (command) {
                        case AUTH:
                            authPlayer();
                            break;
                        case UPDATE_SEARCH_PLAYERS_INFO:
                            updateSearchPlayersInfo();
                            break;
                        case BANK_ACTION:
                            bankAction(objectInputStream);
                            break;
                        case NEXT_TURN:
                            nextTurn();
                            break;
                        case NEXT_PHASE:
                            nextPhase();
                            break;
                        case EXIT:
                            exit();
                            return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public void authPlayer() throws IOException {
            if (session == null || !session.isAvailable()) return;

            String name = inputStream.readUTF();
            byte defaultAvatarId = inputStream.readByte();
            Avatar avatar = Avatar.getDefaultAvatar(defaultAvatarId);
            session.register(new Player(name, avatar));
        }

        public void updateSearchPlayersInfo() {

        }

        public void bankAction(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            BankAction bankAction = (BankAction) objectInputStream.readObject();
            switch (bankAction) {
                case BUY_RESOURCE:
                    break;
                case SELL_PRODUCT:
                    break;
                case BUILD_FACTORY:
                    break;
                case AUTOMATE_FACTORY:
                    break;
                case GET_LOAN:
                    break;
                case PAY_LOAN:
                    break;
                case PAY_REGULAR_COSTS:
                    break;
            }
        }

        public void nextTurn() {

        }

        public void nextPhase() {

        }

        public void exit() {

        }
    }
}
