package server_file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain {
    private static final int PORT = 12345;
    // Daftar user (username -> password)
    public static Map<String, String> users = new HashMap<>();
    // Daftar client online (username -> output stream)
    public static Map<String, ClientHandler> onlineClients = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server berjalan di port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client terhubung: " + clientSocket);

                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Gagal menjalankan server: " + e.getMessage());
        }
    }
}
