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
import at.fhtw.swen.mctg.models.*;
import at.fhtw.swen.mctg.models.Package;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static at.fhtw.swen.mctg.httpserver.server.Server.*;


public class BusinessHandler extends Controller implements Service {

    public BusinessHandler() {
    }

    private Response login(UserCredentials user) {
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            boolean userLogin = new UserRepository(unitOfWork).login(user);
            unitOfWork.commitTransaction();
            if(userLogin){
                return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\" : \"sfdsafklösdfsdkl\"}");
                //TODO: Save Token somewhere??
            }else {
                return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\" : \"wrong username or password\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response register(UserCredentials user){

        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);

        try (unitOfWork) {
            if(userRepository.userExists(user.getUsername())){
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

    private void removeCard(int cid){

    }

    private Response getUserData(String username){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            UserData data = new UserRepository(unitOfWork).getUser(username);
            if (data != null){
                String userDataJSON = this.getObjectMapper().writeValueAsString(data);
                return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);
            }else{
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\" : \"User not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response updateUser(String username, UserData userData){
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);

        try (unitOfWork) {
            if(!userRepository.userExists(username)){
                unitOfWork.rollbackTransaction();
                unitOfWork.close();
                return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{ \"message\" : \"User doesnt exist\" }");
            }else{
                userRepository.updateUser(username, userData);
                unitOfWork.commitTransaction();
                return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\" : \"User updated\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private Response buyPackage(UserCredentials user){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            UserRepository userRepository = new UserRepository(unitOfWork);
            boolean paid = userRepository.updateCoins(user);

            if(!paid){
                unitOfWork.rollbackTransaction();
                return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{ \"message\" : \"Not enough money!\" }");
            }else {

                CardRepository cardRepository = new CardRepository(unitOfWork);
                ArrayList<Card> cards = cardRepository.getPackage();

                userRepository.addCardsToInventory(user, cards);

                unitOfWork.commitTransaction();

                //String packJSON = this.getObjectMapper().writeValueAsString(cards);
                return new Response(HttpStatus.ACCEPTED, ContentType.JSON, "{ \"message\" : \"Purchase successful!\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private Response getInventory(UserData user){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            ArrayList<Card> inventory = new CardRepository(unitOfWork).getInventory(user);

            if (inventory != null){
                String userDataJSON = this.getObjectMapper().writeValueAsString(inventory);
                return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);
            }else{
                return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\" : \"Inventory is empty!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response getStack(UserData user){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            ArrayList<Card> stack = new CardRepository(unitOfWork).getStack(user);

            if (stack != null){
                String userDataJSON = this.getObjectMapper().writeValueAsString(stack);
                return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);
            }else{
                return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\" : \"Stack is empty!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response getStats(UserData user){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            UserStats stats = new UserRepository(unitOfWork).getStats(user);
            if (stats != null){
                String userDataJSON = this.getObjectMapper().writeValueAsString(stats);
                return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);
            }else{
                return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\" : \"No stats available!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response getScoreboard(){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            ArrayList<UserStats> stats =  new UserRepository(unitOfWork).getScoreboard();
            if (stats != null){
                String scoreboardDataJSON = this.getObjectMapper().writeValueAsString(stats);
                return new Response(HttpStatus.OK, ContentType.JSON, scoreboardDataJSON);
            }else{
                return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\" : \"No stats available!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response getTradingDeals(){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){
            ArrayList<TradingDeal> tradingDeals =  new UserRepository(unitOfWork).getTradingDeals();

            for (int i = 0; i < tradingDeals.size(); i++) {
                Card curr = new CardRepository(unitOfWork).getCardTypeFromInvCard(tradingDeals.get(i).getCardToDeal());
                String cardAsJSON = this.getObjectMapper().writeValueAsString(curr);
                tradingDeals.get(i).setCardToDealFull(cardAsJSON);
            }

            if (!tradingDeals.isEmpty()){
                String tradingDealsJSON = this.getObjectMapper().writeValueAsString(tradingDeals);
                return new Response(HttpStatus.OK, ContentType.JSON, tradingDealsJSON);
            }else{
                return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\" : \"No trading deals available!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\"");
        }
    }

    private Response updateStack(UserData user, ArrayList<Card> stack){
        UnitOfWork unitOfWork = new UnitOfWork();
        CardRepository cardRepository = new CardRepository(unitOfWork);

        try (unitOfWork) {
            cardRepository.updateStack(user, stack);
            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\" : \"Stack updated\" }");
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private Response createTradingDeal(TradingDeal deal){
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);

        try (unitOfWork) {
            if(new UserRepository(unitOfWork).tradeExists(deal.getCardToDeal())){
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{ \"message\" : \"This Card is already being traded\" }");
            }
            CardRepository repos = new CardRepository(unitOfWork);

            if(repos.isInStack(deal.getCardToDeal())){
                return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{ \"message\" : \"Card is in stack!\" }");
            }else{
                new UserRepository(unitOfWork).startTradingDeal(deal);
                unitOfWork.commitTransaction();
                return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\" : \"Trade created\" }");
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private Response deleteTradingDeal(int dealID){
        UnitOfWork unitOfWork = new UnitOfWork();

        try (unitOfWork) {
            new UserRepository(unitOfWork).deleteTradingDeal(dealID);
            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\" : \"Deal deleted!\" }");
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private Response createPackage(ArrayList<Card> pack){
        UnitOfWork unitOfWork = new UnitOfWork();
        CardRepository cardRepository = new CardRepository(unitOfWork);

        try (unitOfWork) {
            cardRepository.createPackage(pack);
            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{ \"message\" : \"Package created\" }");

        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{ \"message\" : \"Internal Server Error\" }");
        }
    }

    private String play(int opponent, int user){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork){

            User player1;
            User player2;

            String username1 = new UserRepository(unitOfWork).getUser(user).getName();
            String username2 = new UserRepository(unitOfWork).getUser(user).getName();

            ArrayList<Card> stack1 = new CardRepository(unitOfWork).getStack(new UserData(user, username1, "", "", 0));
            ArrayList<Card> stack2 = new CardRepository(unitOfWork).getStack(new UserData(opponent, username2, "", "", 0));

            player1 = new User(user, username1, new Stack(stack1));
            player2 = new User(opponent, username2, new Stack(stack2));

            Game game = new Game(player1, player2);
            String gameProtocol = game.fight();

            if(game.winner == 1){
                ArrayList<Card> Winningstack = new ArrayList<>();
                CardRepository cardRepository = new CardRepository(unitOfWork);
                for (int i = 0; i < stack2.size(); i++) {
                    Card current = cardRepository.getCardTypeFromInvCard(stack2.get(i).getId());
                    Winningstack.add(current);
                    cardRepository.removeCard(stack2.get(i).getId());
                }

                new UserRepository(unitOfWork).addCardsToInventory(new UserCredentials(username1, ""), Winningstack);
                new UserRepository(unitOfWork).win(user);
                new UserRepository(unitOfWork).loose(opponent);
                unitOfWork.commitTransaction();
            }else if(game.winner == 2){
                ArrayList<Card> Winningstack = new ArrayList<>();
                CardRepository cardRepository = new CardRepository(unitOfWork);
                for (int i = 0; i < stack1.size(); i++) {
                    Card current = cardRepository.getCardTypeFromInvCard(stack1.get(i).getId());
                    Winningstack.add(current);
                    cardRepository.removeCard(stack1.get(i).getId());
                }

                new UserRepository(unitOfWork).addCardsToInventory(new UserCredentials(username2, ""), Winningstack);
                new UserRepository(unitOfWork).win(opponent);
                new UserRepository(unitOfWork).loose(user);
                unitOfWork.commitTransaction();
            }

            return gameProtocol;

        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
        }
        return null;
    }


    @Override
    public Response handleRequest(Request request) {

        if(request.getPathParts().get(0).equals("users")){
            if(request.getPathParts().size() == 1 && request.getMethod() == Method.POST){
                try {
                    UserCredentials user = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
                    System.out.println(user);
                    return register(user);
                } catch (JsonProcessingException e){
                    e.printStackTrace();
                }
            }
            else if(request.getPathParts().size() == 2){

                if(request.getMethod() == Method.GET){
                    return getUserData(request.getPathParts().get(1));
                }

                else if (request.getMethod() == Method.PUT){
                    try {

                        UserData userData = this.getObjectMapper().readValue(request.getBody(), UserData.class);
                        return updateUser(request.getPathParts().get(1), userData);
                    } catch (JsonProcessingException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(request.getPathParts().get(0).equals("sessions") && request.getMethod() == Method.POST){
            try {
                UserCredentials userCredentials = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
                return login(userCredentials);
            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        else if(request.getPathParts().get(0).equals("packages") && request.getMethod() == Method.POST){
            try {
                UserCredentials userCredentials = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
                return login(userCredentials);
            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        else if(request.getPathParts().get(0).equals("transaction") && request.getPathParts().get(1).equals("packages") && request.getMethod() == Method.POST){
            try {
                UserCredentials user = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
                return buyPackage(user);

            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        else if(request.getPathParts().get(0).equals("cards") && request.getMethod() == Method.GET){
            try {
                UserData user = this.getObjectMapper().readValue(request.getBody(), UserData.class);
                return getInventory(user);

            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        else if(request.getPathParts().get(0).equals("deck")){
            if(request.getMethod() == Method.GET){
                try {
                    UserData user = this.getObjectMapper().readValue(request.getBody(), UserData.class);
                    return getStack(user);

                } catch (JsonProcessingException e){
                    e.printStackTrace();
                }
            }
            else if(request.getMethod() == Method.PUT){
                try {
                    UserData user = this.getObjectMapper().readValue(request.getBody(), UserData.class);
                    ArrayList<Card> stack= this.getObjectMapper().readValue(request.getBody(), ArrayList.class);

                    return updateStack(user, stack);

                } catch (JsonProcessingException e){
                    e.printStackTrace();
                }
            }
        }
        else if(request.getPathParts().get(0).equals("stats") && request.getMethod() == Method.GET){
            try {
                UserData user = this.getObjectMapper().readValue(request.getBody(), UserData.class);
                return getStats(user);

            } catch (JsonProcessingException e){
                e.printStackTrace();
            }
        }
        else if(request.getPathParts().get(0).equals("scoreboard") && request.getMethod() == Method.GET){
            return getScoreboard();
        }
        else if(request.getPathParts().get(0).equals("battles") && request.getMethod() == Method.POST){
            int userId = 0;
            try {
                userId = this.getObjectMapper().readValue(request.getBody(), UserData.class).getId();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if(threads.isEmpty()){

                threads.add(Thread.currentThread());
                userIdsMap.put(Thread.currentThread(), userId);

                //try {
                    while(playedGameQueue.get(Thread.currentThread()) == null){

                    };

                    String gameProtocol = playedGameQueue.get(Thread.currentThread());
                    playedGameQueue.remove(Thread.currentThread());

                    return new Response(HttpStatus.ACCEPTED, ContentType.JSON, gameProtocol);

                //}
                /* catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                */
            }else{
                Thread opponent = threads.get(0);
                threads.remove(0);
                int opponentID = userIdsMap.get(opponent);
                userIdsMap.remove(opponent);

                String gameProtocol = play(opponentID, userId);
                playedGameQueue.put(opponent, gameProtocol);
                return new Response(HttpStatus.ACCEPTED, ContentType.JSON, gameProtocol);

            }
        }
        else if(request.getPathParts().get(0).equals("tradings")){
            if(request.getPathParts().size() == 1) {
                if (request.getMethod() == Method.GET) {
                    //Retrieves currently available trading offers
                    return getTradingDeals();
                } else if (request.getMethod() == Method.POST) {
                    //Creates new trading deal
                    try {
                        TradingDeal deal = this.getObjectMapper().readValue(request.getBody(), TradingDeal.class);
                        return createTradingDeal(deal);
                    } catch (JsonProcessingException e){
                        e.printStackTrace();
                    }
                }
            }else if(request.getPathParts().size() == 2){
                if (request.getMethod() == Method.DELETE) {
                    return deleteTradingDeal(Integer.parseInt(request.getPathParts().get(1)));
                } else if (request.getMethod() == Method.POST) {
                }
            }
        }
        else{
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\" : \"Request not supported!\"}");
        }

        return new Response(HttpStatus.BAD_REQUEST, ContentType.PLAIN_TEXT, "Response");
    }
}
