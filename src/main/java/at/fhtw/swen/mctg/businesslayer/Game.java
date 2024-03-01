package at.fhtw.swen.mctg.businesslayer;

import at.fhtw.swen.mctg.models.Card;
import at.fhtw.swen.mctg.models.User;

public class Game {
    private int round;
    private final User player1;
    private final User player2;

    public boolean draw = false;

    public int winner = 0;

    int ROUNDMAX = 101;

    public Game(User player1, User player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = 1;
    }

    public String fight() {

        String gameProtocol = "";

        while (player1.hasCards() && player2.hasCards() && this.round <= ROUNDMAX) {

            Card card1 = drawCard(player1);
            Card card2 = drawCard(player2);


            int damage1 = 0;
            int damage2 = 0;

            if(card1.isSpell() && card2.isVulnerable(card1.getElementName())){
                damage1 = card1.getDamage() * 2;
            }else if(card2.isSpell() && card1.isVulnerable(card2.getElementName())){
                damage2 = card2.getDamage() * 2;
            }else{
                damage1 = card1.getDamage();
                damage2 = card2.getDamage();
            }
            if (card1.getDamage() < card2.getDamage()) {
                //Card 2 wins -> so player 1 looses his Card
                if(round != ROUNDMAX){
                    gameProtocol = gameProtocol + "Round " + round + ": " + "Player 2's " + card2.name + " beats Player 1's " + card1.name + " and goes into his stack!\n";
                }
                player1.loseCard(card1);
                //And the Card goes to player 2s Stack
                player2.addCardToStack(card1);

            } else if (card1.getDamage() > card2.getDamage()) {
                if(round != ROUNDMAX){
                    gameProtocol = gameProtocol + "Round " + round + ": " + "Player 1's " + card1.name + " beats Player 2's " + card2.name + " and goes into his stack!\n";
                }
                //Card 1 wins -> so player 2 looses his card
                player2.loseCard(card2);
                //and the Card goes to player 1s stack
                player1.addCardToStack(card2);
            } else {
                if(round!=ROUNDMAX){
                    gameProtocol = gameProtocol + "Round " + round + ": " + "Round is a Draw! No-one wins.\n";
                }
            }

            this.round++;
        }

        System.out.println(round);
        System.out.println(ROUNDMAX);

        if(round-1 == ROUNDMAX){
            gameProtocol = gameProtocol + "Game ends in a Draw!\n";
        }else if(!player1.hasCards()){
            gameProtocol = gameProtocol + "Player 2 won the match!\n";
            winner = 2;
        }else if(!player2.hasCards()){
            gameProtocol = gameProtocol + "Player 1 won the match!\n";
            winner = 1;
        }
        return gameProtocol;
    }
    private Card drawCard(User player) {
        return player.drawCard();
    }
}
//Business-Layer