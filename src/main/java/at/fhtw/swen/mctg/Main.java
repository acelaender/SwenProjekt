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
        Card card1 = new Card("monster1", 1, 10);
        Card card2 = new Card("monster2", 1, 12);
        Card card3 = new Card("monster3", 1, 14);
        stackUser1.addCard(card1);
        stackUser1.addCard(card2);
        stackUser1.addCard(card3);
        User testUser1 = new User("user1name", "xyz", 20, stackUser1, new Stack());

        Stack stackUser2 = new Stack();
        Card card4 = new Card("monster4", 1, 11);
        Card card5 = new Card("monster5", 1, 15);
        Card card6 = new Card("monster6", 1, 13);
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