package at.fhtw.swen.mctg.businesslayer;

import at.fhtw.swen.mctg.dataaccesslayer.DataHandler;
import at.fhtw.swen.mctg.models.Package;
import at.fhtw.swen.mctg.models.User;

public class BusinessHandler {


    public BusinessHandler() {
    }

    public String register(User user){
        DataHandler dataHandler = new DataHandler();
        String returnVal = dataHandler.register(user);
        return returnVal;
    }

    public String buyPackage(User user, int packageId){
        DataHandler dataHandler = new DataHandler();
        Package pack = dataHandler.getPackage(packageId);
        user.subtractCoins(pack.getCost());
        pack = pack.buy();
        user.addCardsToCardCollection(pack.getCards());
        return ("true");
    }

    public String play(User user){
        return "true";
    }


}
