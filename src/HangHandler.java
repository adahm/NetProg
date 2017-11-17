import java.io.*;
import java.net.Socket;
import java.util.Random;


public class HangHandler extends Thread {
    private static Object[] words;
    private Random rand = new Random();
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    //Create array for the words from the supplied file.

    //create a input and out stream.
    public HangHandler(Socket Socket){
        try{
            BufferedReader readFile = new BufferedReader(new FileReader("/Users/Andreas/IdeaProjects/hang/src/words.txt"));
            words = readFile.lines().toArray();
        }catch (IOException error) {
            error.printStackTrace();
        }
        this.clientSocket = Socket;
        try{
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(),true);
        }catch (IOException error) {
            error.printStackTrace();
        }
    }

    @Override
    public void run(){
        String guess;
        boolean correct = false;
        Gamestate gamestate = new Gamestate();  //create a new gamestate that keeps the state of the client.
        try{
            output.println("Hello write start to play hangman"); //Send the first gamestate contating score 0 and nothing els
            while((guess = input.readLine()) != null){

                if((gamestate.getAttempt() == 0 && guess.equals("START"))){
                    int randomnumber = rand.nextInt(words.length);
                    System.out.println((String) words[randomnumber]);
                    gamestate.newgame((String) words[randomnumber]); //create a new game and send a new word
                }
                else if(gamestate.getAttempt()>0){ //make sure that if user doesn´t send start it will not decrese or add the score when round is done.
                    if(guess.length()>1){
                        correct = gamestate.tryWordGuess(guess);  //if the guess has more than one letter it is a asumed to be a word guess.
                    }
                    else {
                        correct = gamestate.tryGuess(guess); //otherwise try the letter
                    }
                    gamestate.decreseAttemps(); //decreset the number of attemps.
                    if(correct){
                        gamestate.addScore();
                        gamestate.setAttemptToZero();
                        //if the word is solved increse score bu one
                    }
                    else if(gamestate.getAttempt() == 0) {
                        gamestate.decreseScore();   //if no atemtps left and the word is not solved lower the score.
                    }


                }
                output.println(gamestate.getOutputString(correct));
            }
        }catch (IOException error){
            error.printStackTrace();
        }



    }


}
