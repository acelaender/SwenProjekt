package at.fhtw.swen.mctg.httpserver.utils;
import at.fhtw.swen.mctg.businesslayer.BusinessHandler;
import at.fhtw.swen.mctg.httpserver.server.Service;

import java.util.HashMap;
import java.util.Map;


public class Router {
    private Map<String, Service> serviceRegistry = new HashMap<>();

    public void addService(String route, Service service)
    {
        this.serviceRegistry.put(route, service);
    }

    public void removeService(String route)
    {
        this.serviceRegistry.remove(route);
    }

    public Service resolve(String route)
    {
        return new BusinessHandler();
        //return this.serviceRegistry.get(route);
    }

}
