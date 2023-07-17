import java.util.ArrayList;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;



public class ClientHandler implements Runnable{

    //stores the client handlers
    public static ArrayList <ClientHandler> clientHandlers = new ArrayList<> ();

    //esatblish connection between client and server
    private Socket socket;

    //read messages sent from the client
    private BufferedReader bufferedReader;
    //send messages to the other client
    private BufferedWriter bufferedWriter; 

    private String username;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader ( new InputStreamReader(socket.getInputStream()));
            this.username = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage(username + " has joined chat!");

        }
        catch (IOException e){
            close(socket, bufferedReader, bufferedWriter);
        }



    }

    public void broadcastMessage(String message){
        for (ClientHandler clientHandler: clientHandlers){
            try{
                if (!clientHandler.username.equals(username)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }

            }
            catch (IOException e){
                close(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage(username + " left the chat");
    }

    public void close (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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


    @Override
    public void run(){
        String message;

        while (socket.isConnected()){
            try{
                message = bufferedReader.readLine();
                broadcastMessage(message);
            }   

            catch (IOException e){
                close(socket, bufferedReader, bufferedWriter);
                break;
            }




        }
    }   


}