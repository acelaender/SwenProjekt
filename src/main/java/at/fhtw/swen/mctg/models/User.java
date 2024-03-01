package at.fhtw.swen.mctg.models;

import java.util.ArrayList;

public class User {
    int id;
    public String username;
    private Stack stack;

    public User(int id, String username, Stack stack) {
        this.id = id;
        this.username = username;
        this.stack = stack;
    }

    public User(String username) {
        this.username = username;
    }

    public User(){}
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

    public int getId() {
        return id;
    }


    @Override
    public String toString(){
        return this.username;
    }
}


//Models