import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

    private ServerSocket socket;

    public Server(ServerSocket socket) {
        this.socket = socket;
    }


    //keep the server running while socket is not clossed
    public void openServer(){
        try{
            while (!socket.isClosed()){
                Socket clientSocket = socket.accept();
                System.out.println("user has connected");
                ClientHandler clientHandler = new ClientHandler(clientSocket);


                Thread thread = new Thread(clientHandler);
                thread.start();    
            }
        }
        catch(IOException e){
            e.printStackTrace();
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

    public static String encryptIP(String ipAddress){
        // System.out.println("Your IP address: " + ipAddress);
        
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < ipAddress.length(); i++){
            char chr = ipAddress.charAt(i);
            if (chr == '0'){
                stringBuilder.append('f');
            }
            if (chr == '1'){
                stringBuilder.append('D');
            }
            if (chr == '2'){
                stringBuilder.append('4');
            }
            if (chr == '3'){
                stringBuilder.append('Z');
            }
            if (chr == '4'){
                stringBuilder.append('L');
            }
            if (chr == '5'){
                stringBuilder.append('U');
            }
            if (chr == '6'){
                stringBuilder.append('y');
            }
            if (chr == '7'){
                stringBuilder.append('e');
            }
            if (chr == '8'){
                stringBuilder.append('Q');
            }
            if (chr == '9'){
                stringBuilder.append('o');
            }
            if (chr == '.'){
                stringBuilder.append('R');
            }
            // else{
            //     stringBuilder.append(chr);
            // }




        }
        ipAddress = stringBuilder.toString();

        // System.out.println("Your IP address: " + ipAddress);

        return ipAddress;
    }


    //main
    public static void main (String[] args){
        try{
        

        InetAddress localHost = InetAddress.getLocalHost();
        String ipAddress = localHost.getHostAddress();

        System.out.println("This is the room code: " + encryptIP(ipAddress));



        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.openServer();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}