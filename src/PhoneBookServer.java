import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PhoneBookServer {
    private PhoneBook phoneBook = null;
    private int port = 0;

    public PhoneBookServer(int port) {
        this.port = port;

        phoneBook = new PhoneBook();
        this.setInitialPhoneBook();
    }

    private void setInitialPhoneBook() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        Random rand = new Random();

        for (char letter : alphabet) {
            phoneBook.put(Character.toString(letter), Integer.toString(rand.nextInt(500)));
        }
        System.out.println(phoneBook.save("data.properties"));
    }

    public void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) { //tworzymy gniazdo całego serwera
            System.out.println("Server is up on port " + port);

            while (true) {
                Socket socket = serverSocket.accept(); //tworzymy sockety dla poszczególnych klientów
                    // i blokujemy działanie serwera, aż wejdzie klient do socketa
                System.out.println("New client accepted");

                new ServerThread(phoneBook, socket, serverSocket).start(); //uruchamiamy wątek nowego klienta
                // ten wątek obsługuje połączenie z danym klientem
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        int port = 9000;
        PhoneBookServer server = new PhoneBookServer(port);

        server.runServer();
    }
}
