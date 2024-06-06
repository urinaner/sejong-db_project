package view;

import dao.CustomerDAO;
import view.ManagerFrame;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class App {
    private JPanel jPanel;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JButton loginButton;

    private CustomerDAO customerDao = CustomerDAO.getInstance();

    public App() {
        initializeUserLogin();
    }

    public App(boolean isManager) {
        if (isManager) {
            initializeManagerLogin();
        } else {
            initializeUserLogin();
        }
    }

    private void initializeUserLogin() {
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel nameLabel = new JLabel("Name:");
        jPanel.add(nameLabel);

        nameField = new JTextField();
        jPanel.add(nameField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        jPanel.add(phoneNumberLabel);

        phoneNumberField = new JTextField();
        jPanel.add(phoneNumberField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phoneNumber = phoneNumberField.getText();

                int result = customerDao.login(name, phoneNumber);
                switch (result) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "Login successful");
                        // 로그인 성공 시 MovieReservationGUI를 생성하고 화면에 표시
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                new MovieReservationGUI().setVisible(true);
                            }
                        });
                        break;

                    case -1:
                        JOptionPane.showMessageDialog(null, "Invalid phone number");
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(null, "Unregistered name");
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(null, "Database error");
                        break;
                }
            }
        });
        jPanel.add(loginButton);
    }

    private void initializeManagerLogin() {
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel idLabel = new JLabel("ID:");
        jPanel.add(idLabel);

        nameField = new JTextField();
        jPanel.add(nameField);

        JLabel pwLabel = new JLabel("PW:");
        jPanel.add(pwLabel);

        phoneNumberField = new JTextField();
        jPanel.add(phoneNumberField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = nameField.getText();
                String pw = phoneNumberField.getText();

                if (id.equals("root") && pw.equals("1234")) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    new ManagerFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed");
                }
            }
        });
        jPanel.add(loginButton);
    }

    public static void run() {
        final JFrame frame = new JFrame("Movie Reservation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Choose your account");

        JButton startButton1 = new JButton("Manager");
        startButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new App(true).setVisible(true);
                    }
                });
            }
        });

        JButton startButton2 = new JButton("User");
        startButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new App(false).setVisible(true);
                    }
                });
            }
        });

        panel.add(titleLabel);
        panel.add(startButton1);
        panel.add(startButton2);

        frame.getContentPane().add(panel);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setVisible(boolean visible) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(visible);
    }
}
