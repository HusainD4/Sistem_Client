package client_file;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class RegisterFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;
    private ClientConnection connection;

    public RegisterFrame(ClientConnection connection) {
        this.connection = connection;
        setTitle("Register");
        setSize(300, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        btnRegister = new JButton("Register");
        btnRegister.setBounds(120, 110, 150, 30);
        add(btnRegister);

        btnBack = new JButton("Back");
        btnBack.setBounds(120, 150, 150, 30);
        add(btnBack);

        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = txtUsername.getText();
                    String password = new String(txtPassword.getPassword());

                    // Pastikan koneksi ke server telah berhasil
                    if (connection != null && connection.isConnected()) {
                        // Mengirim data registrasi ke server
                        connection.sendRegister(username, password);
                        // Menerima respon dari server
                        String response = connection.receiveResponse();

                        // Jika server mengirimkan "REGISTER_SUCCESS"
                        if ("REGISTER_SUCCESS".equals(response)) {
                            JOptionPane.showMessageDialog(RegisterFrame.this, "Registrasi berhasil");
                            new LoginFrame(connection);  // Pindah ke login frame
                            dispose();  // Tutup register frame
                        } else {
                            JOptionPane.showMessageDialog(RegisterFrame.this, "Registrasi gagal");
                        }
                    } else {
                        JOptionPane.showMessageDialog(RegisterFrame.this, "Tidak terhubung ke server.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Terjadi kesalahan koneksi.");
                    ex.printStackTrace();
                }
            }
        });

        // Menambahkan aksi untuk tombol 'Back'
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame(connection);  // Pindah ke login frame
            }
        });

        setVisible(true);
    }
}
