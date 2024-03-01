package at.fhtw.swen.mctg.models;

public class UserData {

    private int id;
    private String username;
    private String bio;
    private String image;

    private int coins;

    public UserData(int id, String username, String bio, String image, int coins) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.coins = coins;
    }

    public UserData() {
    }

    public void setName(String name) {
        this.username = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
