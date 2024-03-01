package businesslayerTest;

import at.fhtw.swen.mctg.businesslayer.BusinessHandler;
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


import at.fhtw.swen.mctg.httpserver.server.Request;
import org.junit.jupiter.api.Test;

public class BusinessHandlerTest {

     @Test
    public void handleRequestTest(){
         Request request = new Request();
         request.setMethod(Method.POST);
         request.setUrlContent("/test/abcd");
         ArrayList<String> pathParts = new ArrayList<>();
         pathParts.add("test");
         pathParts.add("abcd");
         request.setPathParts(pathParts);
         request.setBody("testBody");


         BusinessHandler testBusinessHandler = new BusinessHandler();

         Response testsResponse = testBusinessHandler.handleRequest(request);

         System.out.println(testsResponse);
     }

     @Test
    public void handleRequestTest2(){
         Request request = new Request();
         request.setMethod(Method.GET);
         request.setUrlContent("/users");
         ArrayList<String> pathParts = new ArrayList<>();
         pathParts.add("users");
         //pathParts.add("abcd");
         request.setPathParts(pathParts);
         request.setBody("{ \"id\" : \"1\", \"name\" : \"luki\"}");


         BusinessHandler testBusinessHandler = new BusinessHandler();

         Response testsResponse = testBusinessHandler.handleRequest(request);

         System.out.println(testsResponse);
     }

    @Test
    public void handleRequestTest3(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/cards");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("cards");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"id\" : \"1\", \"name\" : \"luki\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest4(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/scoreboard");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("scoreboard");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("testBody");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest5(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/stats");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("stats");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"id\" : \"1\", \"name\" : \"luki\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest6(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/tradings");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("tradings");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"id\" : \"1\", \"name\" : \"luki\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest7(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/cards");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("cards");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"id\" : \"12\", \"name\" : \"luki14312\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest8(){
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setUrlContent("/sessions");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("sessions");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"username\" : \"luki\", \"password\" : \"1234\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest9(){
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setUrlContent("/sessions");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("sessions");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"username\" : \"lukiasfd\", \"password\" : \"1234\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTest10(){
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setUrlContent("/sessions");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("sessions");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"username\" : \"luki\", \"password\" : \"123safdas4\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }

    @Test
    public void handleRequestTestDeck(){
        Request request = new Request();
        request.setMethod(Method.GET);
        request.setUrlContent("/deck");
        ArrayList<String> pathParts = new ArrayList<>();
        pathParts.add("deck");
        //pathParts.add("abcd");
        request.setPathParts(pathParts);
        request.setBody("{ \"id\" : \"1\", \"name\" : \"luki\"}");


        BusinessHandler testBusinessHandler = new BusinessHandler();

        Response testsResponse = testBusinessHandler.handleRequest(request);

        System.out.println(testsResponse);
    }


}
