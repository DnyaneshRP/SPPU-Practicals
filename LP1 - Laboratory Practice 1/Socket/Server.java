import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        BufferedReader consoleReader = null;

        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server started. Waiting for client...");
            socket = serverSocket.accept();
            System.out.println("Client connected.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Thread to read messages from client
            Thread readThread = new Thread(() -> {
                try {
                    String msgFromClient = reader.readLine();
                    while (msgFromClient != null) {
                        System.out.println("Client: " + msgFromClient);
                        msgFromClient = reader.readLine();
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            readThread.start();

            // Main thread to send messages to client
            String msgToClient;
            while ((msgToClient = consoleReader.readLine()) != null) {
                writer.println(msgToClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

