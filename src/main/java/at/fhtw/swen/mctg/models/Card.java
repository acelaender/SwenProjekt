package at.fhtw.swen.mctg.models;
import java.lang.String;
public class Card {
    int id;
    public String name;
    private int damage;
    public Element element;
    private String type;

    private String elementName;


    public Card(int id, String name, Element element, int damage, String type, String elementName){
        this.id = id;
        this.name = name;
        this.damage = damage;
        this.element = element;
        this.type = type;
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Card() {
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
        if(this.elementName.equals("water") && element.equals("normal")){
            return true;
        }else if(this.elementName.equals("fire") && element.equals("water")){
            return true;
        }else if(this.elementName.equals("normal") && element.equals("fire")){
            return true;
        }else{
            return false;
        }
    }

}

//Models