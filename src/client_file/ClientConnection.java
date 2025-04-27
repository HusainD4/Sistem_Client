package client_file;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientConnection {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    // Koneksi ke server
    public boolean connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);  // Membuka koneksi ke server
            input = new DataInputStream(socket.getInputStream());  // Input stream
            output = new DataOutputStream(socket.getOutputStream());  // Output stream
            return true;
        } catch (IOException e) {
            System.err.println("Gagal koneksi ke server: " + e.getMessage());
            return false;
        }
    }

    // === LOGIN ===
    public void sendLogin(String username, String password) throws IOException {
        output.writeUTF("LOGIN");  // Kirim perintah LOGIN ke server
        output.writeUTF(username);  // Kirim username
        output.writeUTF(password);  // Kirim password
    }

    // === REGISTER ===
    public void sendRegister(String username, String password) throws IOException {
        output.writeUTF("REGISTER");  // Kirim perintah REGISTER ke server
        output.writeUTF(username);  // Kirim username
        output.writeUTF(password);  // Kirim password
    }

    // === RESPON ===
    public String receiveResponse() throws IOException {
        return input.readUTF();  // Menerima respon dari server
    }

    // === AMBIL CLIENT YANG ONLINE ===
    public List<String> getActiveClients() {
        List<String> clients = new ArrayList<>();
        try {
            output.writeUTF("GET_CLIENTS");  // Kirim perintah untuk mengambil daftar client
            int count = input.readInt();  // Jumlah client online
            for (int i = 0; i < count; i++) {
                clients.add(input.readUTF());  // Terima nama client
            }
        } catch (IOException e) {
            System.err.println("Gagal mengambil daftar client: " + e.getMessage());
        }
        return clients;
    }

    // === KIRIM FILE KE CLIENT TUJUAN ===
    public boolean sendFile(File file, String targetClient) {
        try {
            output.writeUTF("SEND_FILE");  // Kirim perintah untuk mengirim file
            output.writeUTF(targetClient);  // Kirim nama client tujuan
            output.writeUTF(file.getName());  // Kirim nama file
            output.writeLong(file.length());  // Kirim ukuran file

            // Membaca file dan mengirimkan ke server
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int count;
                while ((count = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, count);  // Kirim buffer data file
                }
            }

            // Terima respon dari server jika file diterima
            String status = input.readUTF();
            return status.equals("FILE_RECEIVED");

        } catch (IOException e) {
            System.err.println("Gagal mengirim file: " + e.getMessage());
            return false;
        }
    }

    // === TERIMA FILE DARI CLIENT LAIN ===
    public boolean receiveFile(String targetDirectory) {
        try {
            String fileName = input.readUTF();  // Nama file yang diterima
            long fileSize = input.readLong();  // Ukuran file yang diterima

            // Buat file tujuan untuk menyimpan file yang diterima
            File file = new File(targetDirectory + File.separator + fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int count;
                long bytesReceived = 0;

                // Terima file secara bertahap
                while (bytesReceived < fileSize) {
                    count = input.read(buffer);
                    bytesReceived += count;
                    fos.write(buffer, 0, count);
                }
            }

            // Kirim status sukses
            output.writeUTF("FILE_RECEIVED");
            return true;

        } catch (IOException e) {
            System.err.println("Gagal menerima file: " + e.getMessage());
            return false;
        }
    }

    // Tutup koneksi
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            System.err.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }

    // Mengecek apakah koneksi masih aktif
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}
