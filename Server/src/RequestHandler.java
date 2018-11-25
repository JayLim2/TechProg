import entities.Avatar;
import entities.Player;
import enums.BankAction;
import enums.Command;
import packages.SessionPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

        private Session session;

        public ClientThread(Socket socket) {
            this.socket = socket;
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
                        case UPDATE_SEARCH_PLAYERS_INFO:
                            updateSearchPlayersInfo();
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
            System.out.println("PLAYERS COUNT: " + session.playersCount());
        }

        public void updateSearchPlayersInfo() throws IOException {
            if (session == null || !session.isAvailable()) {

            }

            objectOutputStream.writeObject(new SessionPackage(session.getStartTime(), session.getPlayers()));

            /*objectOutputStream.writeObject(session.getStartTime());
            outputStream.writeInt(session.playersCount());
            Set<Player> players = session.getPlayers();
            for (Player player : players) {
                objectOutputStream.writeObject(player);
            }*/
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
