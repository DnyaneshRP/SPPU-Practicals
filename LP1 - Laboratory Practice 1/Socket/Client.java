import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        BufferedReader consoleReader = null;

        try {
            socket = new Socket("localhost", 5000);
            System.out.println("Connected to server.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String msgFromServer = reader.readLine();
                    while (msgFromServer != null) {
                        System.out.println("Server: " + msgFromServer);
                        msgFromServer = reader.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readThread.start();

            // Main thread to send messages to server
            String msgToServer;
            while ((msgToServer = consoleReader.readLine()) != null) {
                writer.println(msgToServer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

