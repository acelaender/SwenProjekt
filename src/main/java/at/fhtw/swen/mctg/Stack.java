package at.fhtw.swen.mctg;
import java.util.*;

public class Stack {

    private ArrayList<Card> cards;

    public Stack(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Stack(){
        this.cards = new ArrayList<Card>();
    }

    public int size(){
        return this.cards.size();
    }

    public Card takeCard(){
        Random rand = new Random();
        return this.cards.get(rand.nextInt(this.cards.size()));
    }

    public void loseCard(Card card){
        this.cards.remove(card);
    }

    public void addCard(Card card){
        this.cards.add(card);
    }

    public String print(){
        String string = "";
        for (int i = 0; i < cards.size(); i++) {
            string = string + cards.get(i) + ", ";
        }
        return string;
    }
}

//Models