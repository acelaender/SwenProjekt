package at.fhtw.swen.mctg;

import java.io.*;
import java.lang.System.*;
import java.net.ServerSocket;
import java.net.Socket;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        /*
            This is just for testing
            TODO: move this into the test resource
         */
        /*
        Stack stackUser1 = new Stack();
        Card card1 = new Card("monster1", "water", 10, "monster");
        Card card2 = new Card("monster2", "fire", 12, "monster");
        Card card3 = new Card("monster3", "normal", 14, "monster");
        stackUser1.addCard(card1);
        stackUser1.addCard(card2);
        stackUser1.addCard(card3);
        User testUser1 = new User("user1name", "xyz", 20, stackUser1, new Stack());

        Stack stackUser2 = new Stack();
        Card card4 = new Card("monster4", "water", 11, "monster");
        Card card5 = new Card("monster5", "fire", 15, "monster");
        Card card6 = new Card("monster6", "normal", 13, "monster");
        stackUser2.addCard(card4);
        stackUser2.addCard(card5);
        stackUser2.addCard(card6);
        User testUser2 = new User("user2name", "xyz", 20, stackUser2, new Stack());

        Game testGame = new Game(testUser1, testUser2);
        int fight = testGame.fight();

        /*
            End of testing section
         */

        try ( ServerSocket listener = new ServerSocket(1234)) {
            while (true) {
                Socket serviceSocket = listener.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
                PrintStream output = new PrintStream(serviceSocket.getOutputStream());
                while(true){
                    String line = input.readLine();
                    if(line == null || line.equalsIgnoreCase("quit")){
                        break;
                    }
                    if(line.startsWith("POST")){
                        System.out.println("EchoServer called Post");
                    }

                    System.out.println("EchoServer: echo " + line);
                    output.println(line);
                }
            }
        } catch (IOException e) {
            System.err.println("EchoServer: ERROR when connecting: " + e);
        }
        /*
                ServerSocket serverSocket = new ServerSocket(1234);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {

        }
        */


        return;
    }
}