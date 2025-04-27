/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client_file;

/**
 *
 * @author HUSAIN
 */
import javax.swing.*;

public class MainClient {
    public static void main(String[] args) {
        // Membuat instance dari ClientConnection untuk koneksi ke server
        ClientConnection connection = new ClientConnection();

        // Menjalankan GUI Login dengan mengirimkan objek connection untuk komunikasi
        SwingUtilities.invokeLater(() -> new LoginFrame(connection));
    }
}
