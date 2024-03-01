package at.fhtw.swen.mctg.models;

public class TradingDeal {
    private int id;
    private int cardToDeal;
    private String cardToDealFull;
    private String type;
    private int minimumDamage;

    public TradingDeal(int id, int cardToDeal, String cardToDealFull, String type, int minimumDamage) {
        this.id = id;
        this.cardToDealFull = cardToDealFull;
        this.cardToDeal = cardToDeal;
        this.type = type;
        this.minimumDamage = minimumDamage;
    }

    public TradingDeal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardToDeal() {
        return cardToDeal;
    }

    public void setCardToDeal(int cardToDeal) {
        this.cardToDeal = cardToDeal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinimumDamage() {
        return minimumDamage;
    }

    public void setMinimumDamage(int minimumDamage) {
        this.minimumDamage = minimumDamage;
    }

    public String getCardToDealFull() {
        return cardToDealFull;
    }

    public void setCardToDealFull(String cardToDealFull) {
        this.cardToDealFull = cardToDealFull;
    }
}
