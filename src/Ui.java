import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Ui extends Thread{
    private final Scanner in = new Scanner(System.in);
    private Boolean go = true;
    private String input;
    private ClientControll control = new ClientControll();
    private PrintOut out = new PrintOut();
    @Override
    public void run(){
        ForkJoinPool.commonPool().execute(() -> control.connect("localhost",20000,out));
        //Assume that clienct allways want to connect when starting the programm
        do {
            System.out.println("AT any time write QUIT to quit the app:" +
            "or Write START to start a new game");
            input = in.next();
            switch(input){
                case "START":
                    ForkJoinPool.commonPool().execute(() -> control.start()); //use commonpool to run on a thread
                    while(go){
                        System.out.println("Guess letter or word:");
                        input = in.next();
                        switch (input){
                            case "QUIT":
                                go = false;
                                control.quit();
                            default:
                                ForkJoinPool.commonPool().execute(() -> control.sendGuess(input));  //use commonpool to run a seperat thread
                        }
                    }
                case "QUIT":
                    go = false;
                    control.quit();
                    break;


                default:
                    System.out.println("not known command");

            }

        }while(go);
    }
    private class PrintOut implements OutObserver{
        @Override
        public void getServerInput(String input){
            System.out.println(input);
        }
    }

}
