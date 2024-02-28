package at.fhtw.swen.mctg.dataaccesslayer;

import at.fhtw.swen.mctg.models.Package;
import at.fhtw.swen.mctg.models.User;

public class DataHandler {
    public DataHandler() {
    }

    public String register(User user){
        return "user registered!";
    }

    public Package getPackage(int packageID){
        Package pack = new Package("standardPack", 5);
        pack = pack.buy();
        return pack;
    }



}
