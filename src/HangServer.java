import java.io.IOException;
import java.net.ServerSocket;

public class HangServer {
    public static void main(String[] args){
        int port = 20000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            try {
                while(true){
                    new HangHandler(serverSocket.accept()).start();
                }
            }catch (IOException error){
                error.printStackTrace();
            }
        }catch (IOException error){
            error.printStackTrace();
        }
    }
}
