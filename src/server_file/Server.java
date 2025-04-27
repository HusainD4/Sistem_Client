package server_file;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server berjalan di port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.input = new DataInputStream(socket.getInputStream());
                this.output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String command;
                while ((command = input.readUTF()) != null) {
                    switch (command) {
                        case "LOGIN":
                            username = input.readUTF();
                            String password = input.readUTF();
                            if ("admin".equals(username) && "password".equals(password)) {
                                output.writeUTF("LOGIN_SUCCESS");
                            } else {
                                output.writeUTF("LOGIN_FAILED");
                            }
                            break;
                        case "REGISTER":
                            String regUsername = input.readUTF();
                            String regPassword = input.readUTF();
                            // Logic to save user registration (skipping for simplicity)
                            output.writeUTF("REGISTER_SUCCESS");
                            break;
                        case "SEND_FILE":
                            String targetClient = input.readUTF();
                            String fileName = input.readUTF();
                            long fileSize = input.readLong();
                            byte[] buffer = new byte[4096];
                            try (FileOutputStream fos = new FileOutputStream("received_" + fileName)) {
                                int bytesRead;
                                while (fileSize > 0 && (bytesRead = input.read(buffer)) != -1) {
                                    fos.write(buffer, 0, bytesRead);
                                    fileSize -= bytesRead;
                                }
                            }
                            output.writeUTF("FILE_RECEIVED");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
