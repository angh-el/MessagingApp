import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class Server {

    private ServerSocket socket;

    public Server(ServerSocket socket) {
        this.socket = socket;
    }


    //keep the server running while socket is not clossed
    public void openServer(){
        try{
            while (!socket.isClosed()){
                Socket clientSocket = Socket (socket.accept());
                System.out.println("user has connected");
                ClientHandler clientHandler = new ClientHandler(clientSocket);


                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch(IOException e){

        }

    }

    //server is closed if and error occurs
    public void closeServer(){
        try{
            if (socket != null){
                socket.close();
            }
        }
        catch (IOException e){
            // e.printStackTrace();
        }
    }


    //main
    public static void main (String[] args){
        ServerSocket serverSocket = new ServerSocket(1515);
        Server server = new Server(serverSocket);
        server.openServer();
    }


}