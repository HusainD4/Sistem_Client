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
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class ClientDashboard extends JFrame {
    private ClientConnection connection;
    private JList<String> clientList;
    private DefaultListModel<String> listModel;

    public ClientDashboard(ClientConnection connection) {
        this.connection = connection;
        setTitle("Client Dashboard");
        setSize(400, 300);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        clientList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(clientList);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnSendFile = new JButton("Kirim File");
        add(btnSendFile, BorderLayout.SOUTH);
        btnSendFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String targetClient = clientList.getSelectedValue();
                if (targetClient != null) {
                    new InteraksiClientFrame(connection, targetClient);
                } else {
                    JOptionPane.showMessageDialog(ClientDashboard.this, "Pilih client untuk kirim file");
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Ambil daftar client online
        List<String> activeClients = connection.getActiveClients();
        for (String client : activeClients) {
            listModel.addElement(client);
        }
    }
}
