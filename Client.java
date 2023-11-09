import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private JFrame connectFrame;
    private JTextField roomCodeField;
    private JTextField usernameField;

    private JFrame mainChatFrame;
    private JTextArea topTextArea;
    private JTextField bottomTextField;
    private JButton sendMessageButton;

    private boolean firstMessageSent = false;

    public Client() {
        //smaller connection window
        createConnectWindow();
    }

    private void createConnectWindow() {
        connectFrame = new JFrame("Connect to Chat");
        connectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectFrame.setSize(300, 150);

        JPanel connectPanel = new JPanel(new GridLayout(3, 2));

        roomCodeField = new JTextField();
        usernameField = new JTextField();

        connectPanel.add(new JLabel("Room Code:"));
        connectPanel.add(roomCodeField);
        connectPanel.add(new JLabel("Nickname:"));
        connectPanel.add(usernameField);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomCode = roomCodeField.getText();
                String username = usernameField.getText();

                if (!roomCode.isEmpty() && !username.isEmpty()) {
                    //connect to the chat server
                    connectToServer(roomCode, username);

                    //close the connection window
                    connectFrame.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(connectFrame, "Room code and nickname cannot be empty");
                }
            }
        });
        connectPanel.add(connectButton);

        connectFrame.getContentPane().add(connectPanel);
        connectFrame.setVisible(true);
    }

    private void connectToServer(String roomCode, String username) {
        try {
            String ipAddress = decryptIP(roomCode);

            //connect to the server
            socket = new Socket(ipAddress, 1234);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.username = username;


            createAndShowChatWindow();

            //start listening for incoming messages
            waitMessage();
        } catch (IOException e) {
            e.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    private void createAndShowChatWindow() {
        mainChatFrame = new JFrame("Chat Window - " + username);
        mainChatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainChatFrame.setSize(400, 300);

        topTextArea = new JTextArea();
        topTextArea.setEditable(false);
        topTextArea.setLineWrap(true); 
        topTextArea.setWrapStyleWord(true); 

        JScrollPane scrollPane = new JScrollPane(topTextArea);
        mainChatFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        bottomTextField = new JTextField();
        bottomTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomTextField, BorderLayout.CENTER);
        bottomPanel.add(sendMessageButton, BorderLayout.EAST);

        mainChatFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        mainChatFrame.setVisible(true);
    }


    private void sendMessage() {
        try {
            String message = bottomTextField.getText();
            if (!message.isEmpty()) {
                if (!firstMessageSent) {
                    
                    bufferedWriter.write(username + " has joined the chat.");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();




                    appendMessageToTopTextArea(username + " has joined the chat.");

                    firstMessageSent = true;
                } else {


                    bufferedWriter.write(username + ": " + message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    
                    appendMessageToTopTextArea(username + ": " + message);
                }

                
                bottomTextField.setText("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }   
    }


    private void waitMessage() {
        new Thread(() -> {
            String incomingMessage;

            while (socket.isConnected()) {
                try {
                    incomingMessage = bufferedReader.readLine();

                    if (incomingMessage != null) {
                        //show the incoming message
                        appendMessageToTopTextArea(incomingMessage);
                    }

                } catch (IOException e) {
                    close(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    private void appendMessageToTopTextArea(String message) {
        SwingUtilities.invokeLater(() -> {
            topTextArea.append(message + "\n");
        });
    }

    private void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String decryptIP(String ipAddress) {
        Map<Character, Character> charMap = new HashMap<>();
        charMap.put('f', '0');
        charMap.put('D', '1');
        charMap.put('4', '2');
        charMap.put('Z', '3');
        charMap.put('L', '4');
        charMap.put('U', '5');
        charMap.put('y', '6');
        charMap.put('e', '7');
        charMap.put('Q', '8');
        charMap.put('o', '9');
        charMap.put('R', '.');

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < ipAddress.length(); i++) {
            char chr = ipAddress.charAt(i);
            char replacement = charMap.getOrDefault(chr, chr);
            stringBuilder.append(replacement);
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        //start client and create window
        SwingUtilities.invokeLater(() -> new Client());
    }
}
