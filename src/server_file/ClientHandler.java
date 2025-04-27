package server_file;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String command = input.readUTF();

                switch (command) {
                    case "LOGIN":
                        handleLogin();
                        break;
                    case "REGISTER":
                        handleRegister();
                        break;
                    case "GET_CLIENTS":
                        sendClientList();
                        break;
                    case "SEND_FILE":
                        handleSendFile();
                        break;
                    default:
                        System.out.println("Perintah tidak dikenali: " + command);
                }
            }

        } catch (IOException e) {
            System.out.println("Client terputus: " + username);
        } finally {
            logout();
        }
    }

    private void handleLogin() throws IOException {
        String user = input.readUTF();
        String pass = input.readUTF();

        if (ServerMain.users.containsKey(user) && ServerMain.users.get(user).equals(pass)) {
            this.username = user;
            ServerMain.onlineClients.put(user, this);
            output.writeUTF("LOGIN_SUCCESS");
            System.out.println(user + " login berhasil");
        } else {
            output.writeUTF("LOGIN_FAILED");
        }
    }

    private void handleRegister() throws IOException {
        String user = input.readUTF();
        String pass = input.readUTF();

        if (ServerMain.users.containsKey(user)) {
            output.writeUTF("USERNAME_TAKEN");
        } else {
            ServerMain.users.put(user, pass);
            output.writeUTF("REGISTER_SUCCESS");
            System.out.println("User baru terdaftar: " + user);
        }
    }

    private void sendClientList() throws IOException {
        output.writeInt(ServerMain.onlineClients.size() - 1);
        for (String client : ServerMain.onlineClients.keySet()) {
            if (!client.equals(username)) {
                output.writeUTF(client);
            }
        }
    }

    private void handleSendFile() throws IOException {
        String targetClient = input.readUTF();
        String fileName = input.readUTF();
        long fileSize = input.readLong();

        ClientHandler target = ServerMain.onlineClients.get(targetClient);
        if (target != null) {
            // Kirim ke target
            target.output.writeUTF(fileName);
            target.output.writeLong(fileSize);

            byte[] buffer = new byte[4096];
            long bytesSent = 0;
            int count;

            while (bytesSent < fileSize) {
                count = input.read(buffer);
                bytesSent += count;
                target.output.write(buffer, 0, count);
            }

            // Tunggu konfirmasi dari penerima
            String status = target.input.readUTF();
            output.writeUTF(status); // Kirim balik ke pengirim
        } else {
            output.writeUTF("CLIENT_OFFLINE");
        }
    }

    private void logout() {
        try {
            if (username != null) {
                ServerMain.onlineClients.remove(username);
                System.out.println(username + " logout.");
            }
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Gagal logout: " + e.getMessage());
        }
    }
}
