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
import java.io.IOException;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private ClientConnection connection;

    public LoginFrame(ClientConnection connection) {
        this.connection = connection;
        setTitle("Login");
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 30, 100, 25);
        add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(120, 30, 150, 25);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 70, 100, 25);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(120, 70, 150, 25);
        add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 110, 150, 30);
        add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(120, 150, 150, 30);
        add(btnRegister);

        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = txtUsername.getText();
                    String password = new String(txtPassword.getPassword());
                    connection.sendLogin(username, password);
                    String response = connection.receiveResponse();
                    if ("LOGIN_SUCCESS".equals(response)) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Login berhasil");
                        new ClientDashboard(connection);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Login gagal");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Terjadi kesalahan koneksi");
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame(connection);
                dispose();
            }
        });

        setVisible(true);
    }
}
