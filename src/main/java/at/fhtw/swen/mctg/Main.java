package at.fhtw.swen.mctg;

import java.lang.System.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        /*
            This is just for testing
            TODO: move this into the test resource
         */
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



        return;
    }
}