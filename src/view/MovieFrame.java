package view;

import javax.swing.*;

public class MovieFrame extends JFrame {
    public MovieFrame() {
        setTitle("Movie Frame");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("영화예매창입니당 >,<");
        add(label);

        setVisible(true);
    }
}
