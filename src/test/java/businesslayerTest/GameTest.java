package businesslayerTest;

import at.fhtw.swen.mctg.businesslayer.Game;
import at.fhtw.swen.mctg.models.Card;
import at.fhtw.swen.mctg.models.Element;
import at.fhtw.swen.mctg.models.Stack;
import at.fhtw.swen.mctg.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void fightTest(){
        Stack stackUser1 = new Stack();


        Card card1 = new Card(1, "monster1", new Element(1, "water", 3), 10, "monster", "water");
        Card card2 = new Card(2, "monster2", new Element(2, "fire", 1), 10, "monster", "fire");
        Card card3 = new Card(3, "monster3", new Element(3, "normal", 2), 10, "monster", "normal");

        stackUser1.addCard(card1);
        stackUser1.addCard(card2);
        stackUser1.addCard(card3);

        Assertions.assertEquals(3, stackUser1.size());

        stackUser1.loseCard(card2);

        Assertions.assertEquals(2, stackUser1.size());

        stackUser1.addCard(card2);

        User testUser1 = new User(1, "user1", stackUser1);
        testUser1.addCardToStack(card1);

        Assertions.assertEquals(4, stackUser1.size());

        Stack stackUser2 = new Stack();
        Card card4 = new Card(4, "monster4", new Element(1, "water", 3), 11, "monster", "water");
        Card card5 = new Card(5, "monster5", new Element(2, "fire", 1), 15, "monster", "fire");
        Card card6 = new Card(6, "monster6", new Element(3, "normal", 2), 13, "monster", "normal");


        stackUser2.addCard(card4);
        stackUser2.addCard(card5);
        stackUser2.addCard(card6);
        stackUser2.addCard(card5);

        User testUser2 = new User(1, "user2name", stackUser2);

        Game testGame = new Game(testUser1, testUser2);

        String fight = testGame.fight();

        Assertions.assertNotNull(fight);

        System.out.println(fight);

    }

}
