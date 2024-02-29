package at.fhtw.swen.mctg.models;

import java.util.ArrayList;

public class User {
    int id;
    public String username;
    private String password;
    private int coins;
    private Stack stack;
    private Stack cardCollection;

    public User(int id, String username, String password, int coins, Stack stack, Stack cardCollection) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.coins = coins;
        this.stack = stack;
        this.cardCollection = cardCollection;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){}
    public boolean hasCards(){
        if(this.stack.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public String getPassword() {
        return password;
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

    public int getId() {
        return id;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public String toString(){
        return "Username: " + this.username + ", " + "password: " + this.password + ", " + "coins: " + this.coins + ";";
    }
}


//Models