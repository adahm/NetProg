import java.io.*;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;
//fixa svar från server

public class ClientConnect {
    private  Socket client;

    private  BufferedReader input;
    private  PrintWriter output;
    public void connect(String host, int port, OutObserver out) throws IOException{
        client = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream(), true);
        ForkJoinPool.commonPool().execute(() -> readInput(out));
    }

    public void quit() throws IOException{
        client.close();
    }

    public void sendGuess(String guess){
        output.println(guess);
    }

    public void start(){
        output.println("START");
    }

//using the observer pattern shown on the coursewebb
    public void readInput(OutObserver out){
        String in;
        try {
            while ((in = input.readLine()) != null) {
                out.getServerInput(in);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //fixa så att out put vi får kanske gör en outputhandler interface så att det blir observer pattern.
}
