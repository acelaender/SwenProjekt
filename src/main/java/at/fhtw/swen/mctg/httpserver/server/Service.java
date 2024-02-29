package at.fhtw.swen.mctg.httpserver.server;

public interface Service {
    Response handleRequest(Request request);
}

