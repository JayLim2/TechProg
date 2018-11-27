package samara.university.server;

import samara.university.common.entities.Avatar;
import samara.university.common.entities.Player;
import samara.university.common.enums.BankAction;
import samara.university.common.enums.Command;
import samara.university.common.packages.SessionPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Обработчик запросов к серверу
 */
public class RequestHandler {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientThread(socket)).start();
        }
    }

    private static class ClientThread implements Runnable {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        private ObjectOutputStream objectOutputStream;

        private int id;

        private Session session;

        public ClientThread(Socket socket) {
            this.socket = socket;
            id = new Random().nextInt(10);
        }

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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
            System.out.println("READY FOR COMMANDS");
            while (true) {
                try {
                    while (inputStream.available() <= 0) ;

                    System.out.println("GIVE ME COMMAND");

                    int cmdCode = inputStream.readInt();
                    Command command = Command.values()[cmdCode];
                    switch (command) {
                        case AUTH:
                            authPlayer();
                            break;
                        case UPDATE_SESSION_INFO:
                            updateSessionInfo();
                            break;
                        case BANK_ACTION:
                            bankAction(inputStream);
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
            int defaultAvatarId = inputStream.readInt();
            session.register(new Player(name, Avatar.getDefaultAvatar(defaultAvatarId)));
        }

        public void updateSessionInfo() throws IOException {
            if (session == null || !session.isAvailable()) {
                return;
            }

            SessionPackage sessionPackage = new SessionPackage(session.getStartTime(), session.getPlayers());
            objectOutputStream.writeObject(sessionPackage);
            objectOutputStream.flush();
        }

        public void bankAction(DataInputStream in) throws IOException, ClassNotFoundException {
            int cmdCode = in.readInt();
            BankAction bankAction = BankAction.values()[cmdCode];
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
