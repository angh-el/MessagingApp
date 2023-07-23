import java.net.Socket;
import java.io.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Client{
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    
    
    private String username;
    private String roomCode;



    public Client (Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader ( new InputStreamReader(socket.getInputStream()));
            this.username = username;
        }
        catch (IOException e){
            close(socket, bufferedReader, bufferedWriter);
        }
    }


    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner (System.in);

            while (socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(username + ": " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch (IOException e){
            close(socket, bufferedReader, bufferedWriter);
        }



    }

    public void waitMessage(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String incomingMessage;

                while (socket.isConnected()){
                    try{
                        incomingMessage = bufferedReader.readLine();
                        System.out.println(incomingMessage);

                    }
                    catch (IOException e){
                        close(socket, bufferedReader, bufferedWriter);
                    }

                }


            }
        }).start();
    }


    public void close (Socket socker, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public static String decryptIP(String ipAddress){
        // System.out.println("Your IP address: " + ipAddress);
        
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < ipAddress.length(); i++){
            char chr = ipAddress.charAt(i);
            if (chr == 'f'){
                stringBuilder.append('0');
            }
            if (chr == 'D'){
                stringBuilder.append('1');
            }
            if (chr == '4'){
                stringBuilder.append('2');
            }
            if (chr == 'Z'){
                stringBuilder.append('3');
            }
            if (chr == 'L'){
                stringBuilder.append('4');
            }
            if (chr == 'U'){
                stringBuilder.append('5');
            }
            if (chr == 'y'){
                stringBuilder.append('6');
            }
            if (chr == 'e'){
                stringBuilder.append('7');
            }
            if (chr == 'Q'){
                stringBuilder.append('8');
            }
            if (chr == 'o'){
                stringBuilder.append('9');
            }
            if (chr == 'R'){
                stringBuilder.append('.');
            }
            // else{
            //     stringBuilder.append(chr);
            // }




        }
        ipAddress = stringBuilder.toString();

        // System.out.println("Your IP address: " + ipAddress);

        return ipAddress;
    }


    // public static String getRoomCode(){
    //     return roomCode;
    // }


    // public static String[] get_data(String username, String ipAddress){
    //     String[] data = new String[2]

    //     //index 0 is username
    //     //index 1 is ip

    //     data[0];

    // }

    // public static void setData(String username, String ipAddress){
    //     this.username = username;
    //     this.ipAddress = getRoomCode();
    // }



public static void loginWindow() {
    JFrame frame = new JFrame("Connect");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(300, 200); // Set window size

    // Create a JPanel to hold the components
    JPanel panel = new JPanel();

    // Create JLabels for the username and room code input boxes
    JLabel usernameLabel = new JLabel("Username:");
    JLabel roomCodeLabel = new JLabel("Room Code:");

    // Create JTextFields for username and room code
    JTextField usernameField = new JTextField(20);
    JTextField roomCodeField = new JTextField(20);

    // Create a JLabel for displaying the error message
    JLabel errorLabel = new JLabel();
    errorLabel.setForeground(Color.RED); // Set the color to red

    JButton connectButton = new JButton("Connect");
    connectButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String roomCode = roomCodeField.getText();

            if (username.isEmpty() || roomCode.isEmpty()) {
                // Display the error message and do not close the window
                errorLabel.setText("Error: Username and Room Code are required.");
            } else {
                // Clear the error message and close the window
                errorLabel.setText("");
                frame.dispose();
                connect(username, roomCode);
            }
        }
    });

    // Add components to the panel
    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(roomCodeLabel);
    panel.add(roomCodeField);
    panel.add(connectButton);

    // Add the error label to the panel
    panel.add(errorLabel);

    frame.add(panel);

    frame.setLocationRelativeTo(null);

    frame.setVisible(true);
}





    public static void connect(String username, String ipAddress){
        
        try{
        Socket socket = new Socket (decryptIP(ipAddress), 1234);
        Client client = new Client (socket, username);


        client.waitMessage();
        client.sendMessage();

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }


    public static void main (String[] args){


        // try{

        loginWindow();

        // Scanner scanner = new Scanner (System.in);

        // System.out.println("Enter the room code");
        // String ipAddress = scanner.nextLine();

        // ipAddress = decryptIP(ipAddress);


        // // roomCode = decryptIP(roomCode);

        // System.out.println("Enter Username");
        // String username = scanner.nextLine();

        
        
        // // Socket socket = new Socket ("localhost", 1234);
        // Socket socket = new Socket (ipAddress, 1234);
        // // Socket socket = new Socket ("w232esd.303", 1234);

        // // Socket socket = new Socket (roomCode, 1234);

        // Client client = new Client (socket, username);

        // client.waitMessage();
        // client.sendMessage();

        // }

        // catch (IOException e){
        //     e.printStackTrace();
        // }


    }



}