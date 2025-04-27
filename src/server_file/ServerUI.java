/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server_file;

/**
 *
 * @author HUSAIN
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ServerUI extends JFrame {
    private JTextArea textAreaLog;
    private JButton btnStartStop;
    private ServerSocket serverSocket;
    private boolean isServerRunning = false;

    public ServerUI() {
        setTitle("Server UI");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout());

        // Log area
        textAreaLog = new JTextArea();
        textAreaLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaLog);
        add(scrollPane, BorderLayout.CENTER);

        // Start/Stop button
        JPanel panel = new JPanel();
        btnStartStop = new JButton("Start Server");
        panel.add(btnStartStop);
        add(panel, BorderLayout.SOUTH);

        // Button action
        btnStartStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isServerRunning) {
                    stopServer();
                } else {
                    startServer();
                }
            }
        });

        setVisible(true);
    }

    private void startServer() {
        try {
            // Menampilkan log bahwa server dimulai
            textAreaLog.append("Starting server...\n");
            serverSocket = new ServerSocket(12345); // Port yang digunakan server
            isServerRunning = true;
            btnStartStop.setText("Stop Server");
            textAreaLog.append("Server started on port 12345.\n");

            // Thread untuk menerima koneksi client
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isServerRunning) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            textAreaLog.append("New client connected: " + clientSocket.getInetAddress() + "\n");
                            new ClientHandler(clientSocket).start();
                        } catch (IOException e) {
                            if (isServerRunning) {
                                textAreaLog.append("Error accepting client connection: " + e.getMessage() + "\n");
                            }
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            textAreaLog.append("Error starting server: " + e.getMessage() + "\n");
        }
    }

    private void stopServer() {
        try {
            isServerRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                textAreaLog.append("Server stopped.\n");
            }
            btnStartStop.setText("Start Server");
        } catch (IOException e) {
            textAreaLog.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }

    // ClientHandler untuk menangani komunikasi dengan client
    class ClientHandler extends Thread {
        private Socket socket;
        private DataInputStream input;
        private DataOutputStream output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                textAreaLog.append("Error setting up client streams: " + e.getMessage() + "\n");
            }
        }

        @Override
        public void run() {
            try {
                String clientAddress = socket.getInetAddress().toString();
                textAreaLog.append("Client connected: " + clientAddress + "\n");

                // Menunggu data dari client
                String message;
                while ((message = input.readUTF()) != null) {
                    textAreaLog.append("Message from client: " + message + "\n");

                    // Menanggapi client (dapat diubah sesuai protokol komunikasi)
                    output.writeUTF("Server response: " + message);
                }

                // Menutup koneksi jika client disconnect
                socket.close();
                textAreaLog.append("Client disconnected: " + clientAddress + "\n");

            } catch (IOException e) {
                textAreaLog.append("Error in communication with client: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerUI();  // Membuka GUI Server
            }
        });
    }
}
