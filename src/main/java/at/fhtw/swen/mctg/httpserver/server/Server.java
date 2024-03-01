package at.fhtw.swen.mctg.httpserver.server;

import at.fhtw.swen.mctg.httpserver.utils.RequestHandler;
import at.fhtw.swen.mctg.httpserver.utils.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private Router router;

    public static ArrayList<Thread> threads = new ArrayList<>();
    public static HashMap<Thread, Integer> userIdsMap = new HashMap<>();
    public static HashMap<Thread, String> playedGameQueue = new HashMap<>();

    public Server(int port, Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() throws IOException  {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        System.out.println("Starting server");
        System.out.println("server running at http://localhost:" + this.port);

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while(true) {
                final Socket clientConnection = serverSocket.accept();
                final RequestHandler socketHandler = new RequestHandler(clientConnection, this.router);
                executorService.submit(socketHandler);
            }
        }
    }

}
