package com.biblio.view.Authentication;

import com.biblio.app.Controllers.AuthenticationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import com.biblio.app.Models.Role;


public class Signing extends JFrame implements ActionListener {

    AuthenticationController auth;

    private JTextField username;
    private JPasswordField password;
    private JButton loginButton, resetButton;
    private JLabel usernameLabel, passwordLabel;
    private ImageIcon userIcon, passwordIcon;
    private Image logo;

    public Signing() {
        auth = new AuthenticationController();

        setTitle("Signing");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);


        userIcon = new ImageIcon("user.png");
        passwordIcon = new ImageIcon("password.png");

        ImageIcon logoIcon = new ImageIcon("assets/logo.png");
        Image logo = logoIcon.getImage();

        username = new JTextField();
        password = new JPasswordField();
        loginButton = new JButton("Login");
        resetButton = new JButton("R");
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");

        JLabel logoLabel = new JLabel(new ImageIcon(logo));
        logoLabel.setBounds(100, 30, 300, 173);

        usernameLabel.setBounds(50, 250, 400, 30);
        usernameLabel.setIcon(userIcon);
        username.setBounds(50, 300, 400, 30);

        passwordLabel.setBounds(50, 350, 400, 30);
        passwordLabel.setIcon(passwordIcon);
        password.setBounds(50, 400, 400, 30);

        loginButton.setBounds(165, 490, 170, 30);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        loginButton.addActionListener(this);
        resetButton.addActionListener(this);

        add(logoLabel);
        add(usernameLabel);
        add(username);
        add(passwordLabel);
        add(password);
        add(loginButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String enteredUsername = username.getText();
            String enteredPassword = new String(password.getPassword());

            if (auth.authenticate(enteredUsername, enteredPassword)) {
                JOptionPane.showMessageDialog(this, "You are successfully logged in", "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose();

                List<Role> roles = auth.getUserByCnieOrEmailOrPhone(enteredUsername).getRoles();
                roles.forEach(role -> {

                    switch (role.getRole()) {
                        case ADMIN -> new com.biblio.view.Admin.index();
                        case LIBRARIAN -> new com.biblio.view.Librairian.index();
                    }

                });

            } else {
                JOptionPane.showMessageDialog(this, "Username or Password Incorrect", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == resetButton) {
            username.setText("");
            password.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Signing().setVisible(true);
        });
    }
}