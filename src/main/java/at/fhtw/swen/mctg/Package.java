package at.fhtw.swen.mctg;

import java.util.ArrayList;
import java.util.Random;

public class Package {

    public String name;
    public int cost;
    private ArrayList<Card> cards;

    public Package(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public void buy(User user){
        user.subtractCoins(cost);
        this.cards = instantiateCards();
        //TODO: Maybe just return the package for now direct import into users inventory
        user.addCardsToCardCollection(this.cards);
    }

    private ArrayList<Card> instantiateCards(){
        Random rand = new Random();
        ArrayList<Card> cards = new ArrayList<Card>();
        //TODO: get List of possible Cards from Database;
        //for now just a hardcoded List of possible Cards:
        ArrayList<Card> possibleCards = new ArrayList<>();
        possibleCards.add(new Card("Orc", "normal", 5, "monster"));
        possibleCards.add(new Card("Elven Warrior", "water", 10, "monster"));
        possibleCards.add(new Card("Dragon", "fire", 30, "monster"));
        possibleCards.add(new Card("Human Warrior", "normal", 10, "monster"));
        possibleCards.add(new Card("Water Spell", "water", 15, "spell"));
        possibleCards.add(new Card("Fire Spell", "fire", 15, "spell"));
        possibleCards.add(new Card("Earth Spell", "normal", 15, "spell"));

        for (int i = 0; i < 5; i++) {
            cards.add(possibleCards.get(rand.nextInt(possibleCards.size())));
        }
        return cards;
    }
}
