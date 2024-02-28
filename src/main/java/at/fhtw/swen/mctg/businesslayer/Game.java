package at.fhtw.swen.mctg.businesslayer;

import at.fhtw.swen.mctg.models.Card;
import at.fhtw.swen.mctg.models.User;

public class Game {
    private int round;
    private final User player1;
    private final User player2;

    int ROUNDMAX = 100;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = 1;
    }

    public int fight() {
        while (player1.hasCards() && player2.hasCards() && this.round <= ROUNDMAX) {

            Card card1 = drawCard(player1);
            Card card2 = drawCard(player2);

            //TODO: Fight-Logic needs to be compared with database
            //TODO: Fight-Logic needs to be improved

            //BATTLE OF MONSTERS
            if(card1.isMonster() && card2.isMonster()){
                if (card1.getDamage() < card2.getDamage()) {
                    //Card 2 wins -> so player 1 looses his Card
                    player1.loseCard(card1);
                    //And the Card goes to player 2s Stack
                    player2.addCardToStack(card1);

                    System.out.println(round + ": player 2 won! he got the card " + card1.print());

                } else if (card1.getDamage() > card2.getDamage()) {
                    //Card 1 wins -> so player 2 looses his card
                    player2.loseCard(card2);
                    //and the Card goes to player 1s stack
                    player1.addCardToStack(card2);

                    System.out.println(round + ": player 1 won! he got the card " + card2.print());
                } else {
                    System.out.println("Draw!");
                }
            } else {
                int damage1 = 0;
                int damage2 = 0;
                if(card1.isSpell() && card2.isVulnerable(card1.getElement())){
                    damage1 = card1.getDamage() * 2;
                }else if(card2.isSpell() && card1.isVulnerable(card2.getElement())){
                    damage2 = card2.getDamage() * 2;
                }
            }

            this.round++;
        }
        return 1; //TODO: Set working return value
    }
    private Card drawCard(User player) {
        return player.drawCard();
    }
}
//Business-Layer