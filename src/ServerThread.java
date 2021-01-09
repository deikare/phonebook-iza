import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private PhoneBook phoneBook = null;
    private Socket socket = null;
    private ServerSocket serverSocket = null;

    public ServerThread(PhoneBook phoneBook, Socket socket, ServerSocket serverSocket) {
        this.phoneBook = phoneBook;
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            String readLine = "";
            String commandResult = "";
            do {
                readLine = reader.readLine(); //czytamy komendę podaną przez clienta

                String[] readLineSplitted = readLine.split(" ");
                commandResult = doCommand(readLineSplitted);

                writer.println(commandResult); //wysyłamy komunikat zwrotny clientowi
            }
            while (!readLine.equals("CLOSE") && !readLine.equals("BYE"));

            if (readLine.equals("BYE")) {
                socket.shutdownInput(); //zamykamy strumień wejścia
                //while (!socket.isInputShutdown()); //czekamy na zamknięcie

                socket.shutdownOutput(); //zamykamy strumień wyjścia
                //while (!socket.isOutputShutdown()); //czekamy na zamknięcie

                socket.close(); //zamykamy gniazdo
            }

            if (readLine.equals("CLOSE")) {
                serverSocket.close(); //zamyka połączenia na nowych klientów
                //while (!serverSocket.isClosed()); //czekamy, aż serwer zamknie połączenie
            }

        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }

    public String doCommand(String[] splittedCommand) {
        String result = "";

        if (splittedCommand.length > 0) {
            switch (splittedCommand[0]) {
                case "LOAD" -> {
                    if (splittedCommand.length != 2)
                        result = "ERROR wrong args";
                    result = phoneBook.load(splittedCommand[1]);
                }
                case "SAVE" -> {
                    if (splittedCommand.length != 2)
                        result = "ERROR wrong args";
                    else result = phoneBook.save(splittedCommand[1]);
                }
                case "GET" -> {
                    if (splittedCommand.length != 2)
                        result = "ERROR wrong args";
                    else result = phoneBook.get(splittedCommand[1]);
                }
                case "PUT" -> {
                    if (splittedCommand.length != 3)
                        result = "ERROR wrong args";
                    else result = phoneBook.put(splittedCommand[1], splittedCommand[2]);
                }
                case "REPLACE" -> {
                    if (splittedCommand.length != 3)
                        result = "ERROR wrong args";
                    else result = phoneBook.replace(splittedCommand[1], splittedCommand[2]);
                }
                case "DELETE" -> {
                    if (splittedCommand.length != 2)
                        result = "ERROR wrong args";
                    else result = phoneBook.delete(splittedCommand[1]);
                }
                case "LIST" -> {
                    if (splittedCommand.length != 1)
                        result = "ERROR wrong args";
                    else result = phoneBook.list();
                }
                case "CLOSE" -> {
                    if (splittedCommand.length != 1)
                        result = "ERROR wrong args";
                    else result = "OK";
                }
                case "BYE" -> {
                    if (splittedCommand.length != 1)
                        result = "ERROR wrong args";
                    else result = "OK";
                }
                default -> result = "ERROR wrong command";
            }
        }
        else result = "ERROR wrong command";

        return result;
    }

}
