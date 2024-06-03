package view;

import dao.CustomerDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JPanel jPanel;
    private JTextField nameField;
    private JTextField phoneNumberField;
    private JButton loginButton;

    private CustomerDAO customerDao = CustomerDAO.getInstance();

    public App() {
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
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phoneNumber = phoneNumberField.getText();

                int result = customerDao.login(name, phoneNumber);
                switch (result) {
                    case 0:
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                        break;
                    case -1:
                        JOptionPane.showMessageDialog(null, "전화번호 오류");
                        break;
                    case -2:
                        JOptionPane.showMessageDialog(null, "등록되지 않은 이름");
                        break;
                    case -3:
                        JOptionPane.showMessageDialog(null, "데이터베이스 오류");
                        break;
                }
                if (result == 0) {
                    // 로그인 성공 시 영화 목록 창 띄우기
                    new MovieFrame();
                }

            }
        });
        jPanel.add(loginButton);
    }

    public static void run() {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new App().jPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                App.run();
            }
        });
    }
}
