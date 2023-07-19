import java.net.Socket;
import java.io.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.util.Scanner;


public class Client{
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;


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



    public static void main (String[] args){


        try{
        Scanner scanner = new Scanner (System.in);

        System.out.println("Enter the room code");
        String ipAddress = scanner.nextLine();

        ipAddress = decryptIP(ipAddress);

        System.out.println("Enter Username");
        String username = scanner.nextLine();

        
        
        // Socket socket = new Socket ("localhost", 1234);
        Socket socket = new Socket (ipAddress, 1234);
        // Socket socket = new Socket ("w232esd.303", 1234);

        Client client = new Client (socket, username);

        client.waitMessage();
        client.sendMessage();

        }

        catch (IOException e){
            e.printStackTrace();
        }


    }



}