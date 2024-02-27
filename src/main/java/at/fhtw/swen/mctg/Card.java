package at.fhtw.swen.mctg;
import java.lang.String;
public class Card {

    public String name;
    public int damage;
    public int elementType;

    public Card(String name, int type, int damage){
        this.name = name;
        this.damage = damage;
        this.elementType = type;
    }

    public int getDamage() {
        return damage;
    }

    public String print(){
        return this.name;
    }
}

//Models