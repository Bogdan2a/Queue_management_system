package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;


public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;

    public Scheduler(int maxNoServers) {
        this.maxNoServers = maxNoServers;
        this.servers = new ArrayList<>(maxNoServers);
        for (int i = 1; i <= maxNoServers; i++) {
            Server server = new Server(i);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void dispatchTask(Task t) {
        Server fastestServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod().get() < fastestServer.getWaitingPeriod().get()) {
                fastestServer = server;
            }
        }
        fastestServer.addTask(t);
    }


    public List<Server> getServers() {
        return servers;
    }

}