package at.fhtw.swen.mctg;

import java.util.ArrayList;

public class User {
    public String username;
    private String password;
    private int coins;
    private Stack stack;
    private Stack cardCollection;

    public User(String username, String password, int coins, Stack stack, Stack cardCollection) {
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.stack = stack;
        this.cardCollection = cardCollection;
    }

    public boolean hasCards(){
        if(this.stack.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public Card drawCard(){
        return this.stack.takeCard();
    }

    public void loseCard(Card card){
        this.stack.loseCard(card);
    }

    public void addCardToStack(Card card){
        this.stack.addCard(card);
    };

    public void subtractCoins(int coins){
        this.coins -= coins;
    }

    public void addCardsToCardCollection(ArrayList<Card> cards){
        for (int i = 0; i < cards.size(); i++) {
            this.cardCollection.addCard(cards.get(i));
        }
    }
}


//Models