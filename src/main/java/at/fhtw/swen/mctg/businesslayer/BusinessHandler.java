package at.fhtw.swen.mctg.businesslayer;

import at.fhtw.swen.mctg.controller.Controller;
import at.fhtw.swen.mctg.dataaccesslayer.UnitOfWork;
import at.fhtw.swen.mctg.dataaccesslayer.repository.UserRepository;
import at.fhtw.swen.mctg.models.Package;
import at.fhtw.swen.mctg.models.User;

import java.util.ArrayList;

public class BusinessHandler extends Controller {


    public BusinessHandler() {
    }

    /*public String register(User user){
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
    */

    public User login(User user) {
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            User loggedUser = new UserRepository(unitOfWork).login(user);
            String userDataJSON = this.getObjectMapper().writeValueAsString(loggedUser);
            unitOfWork.commitTransaction();
            return loggedUser;
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return null;
        }
    }

    public String play(User user){
        return "true";
    }


}
