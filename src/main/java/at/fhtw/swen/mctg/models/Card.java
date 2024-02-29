package at.fhtw.swen.mctg.models;
import java.lang.String;
public class Card {
    int id;
    public String name;
    private int damage;
    public Element element;
    private String type;


    //TODO: Later on maybe the types persisted in database with weaknesses and strengths

    //TODO: Cards generally persisted in Database.

    public Card(int id, String name, Element element, int damage, String type){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.element = element;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getDamage() {
        return damage;
    }

    public Element getElement() {
        return element;
    }

    public String print(){
        return this.name;
    }

    public boolean isMonster(){
        return this.type.equals("monster");
    }

    public boolean isSpell(){
        return this.type.equals("spell");
    }

    public boolean isVulnerable(String element){
        //TODO: check vulnerabilities with database
        if(this.element.equals("water") && element.equals("fire")){
            return true;
        }else if(this.element.equals("fire") && element.equals("normal")){
            return true;
        }else if(this.element.equals("normal") && element.equals("water")){
            return true;
        }else{
            return false;
        }
    }
}

//Models