package at.fhtw.swen.mctg.businesslayer;

import at.fhtw.swen.mctg.controller.Controller;
import at.fhtw.swen.mctg.dataaccesslayer.UnitOfWork;
import at.fhtw.swen.mctg.dataaccesslayer.repository.CardRepository;
import at.fhtw.swen.mctg.dataaccesslayer.repository.UserRepository;
import at.fhtw.swen.mctg.httpserver.http.ContentType;
import at.fhtw.swen.mctg.httpserver.http.HttpStatus;
import at.fhtw.swen.mctg.httpserver.http.Method;
import at.fhtw.swen.mctg.httpserver.server.Request;
import at.fhtw.swen.mctg.httpserver.server.Response;
import at.fhtw.swen.mctg.httpserver.server.Service;
import at.fhtw.swen.mctg.models.Package;
import at.fhtw.swen.mctg.models.Stack;
import at.fhtw.swen.mctg.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.ArrayList;

public class BusinessHandler extends Controller implements Service {


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

    public Response login(User user) {
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            User loggedUser = new UserRepository(unitOfWork).login(user);
            unitOfWork.commitTransaction();

            if(loggedUser != null){
                String userDataJSON = this.getObjectMapper().writeValueAsString(loggedUser);
                return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);
            }else {
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\" : \"wrong username or password\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    public Response register(User user){
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        try (unitOfWork) {

            if(userRepository.userExists(user)){
                unitOfWork.rollbackTransaction();
                unitOfWork.close();
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{ \"message\" : \"User exists\" }");
            }else{
                userRepository.register(user);
                unitOfWork.commitTransaction();
                return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\" : \"User created\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    public Response buyPackage(User user, int packageID){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            CardRepository cardRepository = new CardRepository(unitOfWork);
            Package pack = cardRepository.getPackage(packageID);

            user.subtractCoins(pack.cost);

            cardRepository.addCardsToInventory(user, pack.getCards());

            new UserRepository(unitOfWork).updateCoins(user);

            unitOfWork.commitTransaction();

            String packJSON = this.getObjectMapper().writeValueAsString(pack);
            return new Response(HttpStatus.ACCEPTED, ContentType.JSON, packJSON);

        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }




    public String play(User user){
        return "true";
    }


    @Override
    public Response handleRequest(Request request) {
        if(request.getMethod() == Method.POST){

            if(request.getPathParts().get(0).equals("users") && request.getPathParts().size() == 1){
                try {

                    User user = this.getObjectMapper().readValue(request.getBody(), User.class);
                    System.out.println(user);
                    return register(user);

                } catch (JsonProcessingException e){
                    e.printStackTrace();
                }

            }
            else if(request.getPathParts().get(0).equals("users") && request.getPathParts().size() == 2) {
                try {

                    User user = this.getObjectMapper().readValue(request.getBody(), User.class);
                    return login(user);

                } catch (JsonProcessingException e){
                    e.printStackTrace();
                }
            }
        }

        return new Response(HttpStatus.ACCEPTED, ContentType.PLAIN_TEXT, "Response");
    }
}
