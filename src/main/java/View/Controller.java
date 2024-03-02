package View;

import BusinessLogic.SimulationManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    private Interface anInterface;
    private SimulationManager gen;


    public Controller(Interface anInterface) {
        this.anInterface = anInterface;
        anInterface.addStartSimulationButtonListener(new Simulate());
       // this.gen = new SimulationManager();

    }

    class Simulate implements ActionListener {
        public void actionPerformed(ActionEvent e) {
       //
            Integer timeLimit=Integer.parseInt(anInterface.getTimeLimitInput());
            Integer maxServiceTime=Integer.parseInt(anInterface.getMaxServiceTimeInput());
            Integer minServiceTime=Integer.parseInt(anInterface.getMinServiceTimeInput());
            Integer maxArrivalTime=Integer.parseInt(anInterface.getMaxArrivalTimeInput());
            Integer minArrivalTime=Integer.parseInt(anInterface.getMinArrivalTimeInput());
            Integer numberOfServers=Integer.parseInt(anInterface.getQueuesInput());
            Integer numberOfClients=Integer.parseInt(anInterface.getClientsInput());
            gen = new SimulationManager(anInterface,timeLimit,maxArrivalTime,minArrivalTime,maxServiceTime,minServiceTime,numberOfClients,numberOfServers);
            Thread t = new Thread(gen);
            t.start();
        }
    }
}
