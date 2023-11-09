import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Message App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JTextArea topTextArea = new JTextArea();
        topTextArea.setEditable(false);

        JTextField bottomTextField = new JTextField();
        bottomTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = bottomTextField.getText();
                topTextArea.append(message + "\n");
                bottomTextField.setText("");
            }
        });

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = bottomTextField.getText();
                topTextArea.append(message + "\n");
                bottomTextField.setText("");
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topTextArea, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomTextField, BorderLayout.CENTER);
        bottomPanel.add(sendMessageButton, BorderLayout.EAST);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);

        frame.setVisible(true);
    }
}
