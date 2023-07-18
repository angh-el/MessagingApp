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



    public static void main (String[] args){


        try{
        Scanner scanner = new Scanner (System.in);

        System.out.println("Enter Username");
        String username = scanner.nextLine();

        Socket socket = new Socket ("localhost", 1234);

        Client client = new Client (socket, username);

        client.waitMessage();
        client.sendMessage();

        }

        catch (IOException e){
            e.printStackTrace();
        }


    }



}