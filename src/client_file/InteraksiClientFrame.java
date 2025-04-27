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
import java.awt.event.*;
import java.io.*;

public class InteraksiClientFrame extends JFrame {
    private ClientConnection connection;
    private String targetClient;
    private JButton btnSendFile, btnBack;
    private JTextArea logArea;

    public InteraksiClientFrame(ClientConnection connection, String targetClient) {
        this.connection = connection;
        this.targetClient = targetClient;

        setTitle("Interaksi dengan " + targetClient);
        setSize(400, 300);
        setLayout(null);

        JLabel lblTargetClient = new JLabel("Client Tujuan: " + targetClient);
        lblTargetClient.setBounds(20, 20, 200, 25);
        add(lblTargetClient);

        logArea = new JTextArea();
        logArea.setBounds(20, 60, 340, 100);
        logArea.setEditable(false);
        add(new JScrollPane(logArea));

        btnSendFile = new JButton("Kirim File");
        btnSendFile.setBounds(20, 180, 150, 30);
        add(btnSendFile);

        btnBack = new JButton("Kembali");
        btnBack.setBounds(190, 180, 150, 30);
        add(btnBack);

        btnSendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(InteraksiClientFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (connection.sendFile(selectedFile, targetClient)) {
                        logArea.append("File berhasil dikirim ke " + targetClient + "\n");
                    } else {
                        logArea.append("Gagal mengirim file\n");
                    }
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
