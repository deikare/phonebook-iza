import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;


public class PhoneBookClient {
    private String hostname = "";
    private int port = 0;

    public PhoneBookClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void runClient() {
        try (Socket socket = new Socket(hostname, port)) { //chcemy wejść klientem na serwer o adresie hostname i porcie port

            OutputStream output = socket.getOutputStream(); //strumień danych na wejście serwera - strumień danych wysyłany serwerowi
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream(); //strumień danych odbierany od serwera - strumień danych wysyłany klientowi
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String commandResult;

            Scanner scanner = new Scanner(System.in); //obiekt służący do odczytywania wejścia z konsoli

            do {
                String command = scanner.nextLine();

                writer.println(command); //wysyłamy tą linię do socketu - wysyłamy do serwera

                commandResult = reader.readLine();

                System.out.println(commandResult);

            } while (!commandResult.equals("CLOSE") && !commandResult.equals("BYE"));

            scanner.close();
        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 9000;

        PhoneBookClient phoneBookClient = new PhoneBookClient(hostname, port);

        phoneBookClient.runClient();
    }
}
