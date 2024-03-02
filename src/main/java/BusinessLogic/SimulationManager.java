package BusinessLogic;

import Model.Server;
import Model.Task;
import View.Interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SimulationManager implements Runnable {

    public int timeLimit;
    public int maxServiceTime;
    public int minServiceTime;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int numberOfServers; //queues
    public int numberOfClients; //clients

    private Interface view;
    private Scheduler scheduler;

    private List<Task> generatedTasks;

    private PrintWriter printWriter;

    public SimulationManager(Interface view, int timeLimit, int maxArrivalTime, int minArrivalTime, int maxServiceTime, int minServiceTime, int numberOfClients, int numberOfServers) {
        //  anInterface.addStartSimulationButtonListener(new Simulate());
        this.view = view;
        this.timeLimit = timeLimit;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.maxServiceTime = maxServiceTime;
        this.minServiceTime = minServiceTime;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        generatedTasks = new ArrayList<>();
        scheduler = new Scheduler(numberOfServers);
        generateNRandomTasks();
        try {
            printWriter = new PrintWriter("output.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // run();
    }

    private void generateNRandomTasks() {
        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int serviceTime = minServiceTime + random.nextInt(maxServiceTime - minServiceTime + 1);
            int arrivalTime = minArrivalTime + random.nextInt(maxArrivalTime - minArrivalTime + 1);
            Task task = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(task);
        }
        Collections.sort(generatedTasks, Comparator.comparing(Task::getArrivalTime));
    }

    @Override
    public void run() {
        // System.out.println(timeLimit);
        int currentTime = 0;
        int peakHour = 0;
        int peakHourClientsNumber = 0;
        float avgServiceTime = 0;
        int[] serviceTimePerTask = new int[numberOfClients + 2];
        int[] waitingTimePerTask = new int[numberOfClients + 2];
        for (Task taski : generatedTasks) {
            serviceTimePerTask[taski.getID()] = taski.getServiceTime();
            waitingTimePerTask[taski.getID()] = 0;
        }
        while (currentTime <= timeLimit) {

            view.setCurrentTimeOutput(String.valueOf(currentTime));
            printWriter.println("Time " + currentTime);
            String clientsForPrinting = "";
            for (Task taski : generatedTasks) {
                String aux;
                if (currentTime == 0) {
                    avgServiceTime = avgServiceTime + taski.getServiceTime();
                }
                // System.out.println("initial service time: " + avgServiceTime);
                aux = "(" + taski.getID() + "," + taski.getArrivalTime() + "," + taski.getServiceTime() + "); ";
                if (taski.getArrivalTime() == currentTime)
                    clientsForPrinting = clientsForPrinting + "";
                else
                    clientsForPrinting = clientsForPrinting + aux;
            }
            printWriter.println("Waiting clients: " + clientsForPrinting);
            view.setWaitingClientsOutput(clientsForPrinting);
            for (Iterator<Task> iterator = generatedTasks.iterator(); iterator.hasNext(); ) {
                Task currentTask = iterator.next();
                if (currentTask.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(currentTask);
                    iterator.remove();
                }
            }
            System.out.println("Time " + currentTime);
            String queuesInterfacePrint = "";
            int numberOfClientsInQueues = 0;
            for (Server serverI : scheduler.getServers()) {
                ArrayBlockingQueue<Task> queue = serverI.getTasks();
                ArrayBlockingQueue<Task> allTasks = new ArrayBlockingQueue<>(200);
                allTasks = serverI.getTasks();
                String aux = "";
                for (Task taskI : allTasks) {
                    numberOfClientsInQueues++;
                    aux = aux + "(" + taskI.getID() + "," + taskI.getArrivalTime() + "," + taskI.getServiceTime() + "); ";
                    if (taskI == serverI.getTasks().peek()) {
                        serverI.getTasks().peek().setServiceTime(taskI.getServiceTime() - 1);
                        if (currentTime != timeLimit)
                            serviceTimePerTask[taskI.getID()]--;
                        //avgServiceTime--;
                        // System.out.println("am scazut service time");
                    } else {
                        waitingTimePerTask[taskI.getID()]++;
                    }
                }
                if (serverI.getWaitingPeriod().get() > 0) {
                    serverI.decrementWaitingPeriod(1);
                }
                String auxForPrinting = "";
                if (queue.isEmpty()) {
                    printWriter.println("Queue " + serverI.getServerID() + ": Closed");
                    auxForPrinting = "Queue " + serverI.getServerID() + ": Closed";
                } else {
                    printWriter.println("Queue " + serverI.getServerID() + ": " + aux);
                    auxForPrinting = "Queue " + serverI.getServerID() + ": " + aux;
                }
                queuesInterfacePrint = queuesInterfacePrint + auxForPrinting + "\n";
                System.out.println("Queue " + serverI.getServerID() + ": " + serverI.getWaitingPeriod());
            }
            if (numberOfClientsInQueues > peakHourClientsNumber) {
                peakHour = currentTime;
                peakHourClientsNumber = numberOfClientsInQueues;
            }
            view.setLiveQueues(queuesInterfacePrint);
            currentTime++;
            try {
                Thread.sleep(1000); // wait an interval of 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        float totalWaitingTime = 0;
        float leftServiceTime = 0;
        for (int i = 1; i <= numberOfClients; i++) {
            leftServiceTime = leftServiceTime + serviceTimePerTask[i];
            totalWaitingTime = totalWaitingTime + waitingTimePerTask[i];
        }
        view.setPeakHourOutput(String.valueOf(peakHour), String.valueOf(peakHourClientsNumber));
        avgServiceTime = avgServiceTime - leftServiceTime;
        view.setAverageServiceTimeOutput(String.valueOf(avgServiceTime / numberOfClients));
        view.setAverageWaitingTimeOutput(String.valueOf(totalWaitingTime / numberOfClients));
        printWriter.println("\n");
        printWriter.println("Average waiting time: " + totalWaitingTime / numberOfClients);
        printWriter.println("\n");
        printWriter.close();
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMaxServiceTime() {
        return maxServiceTime;
    }

    public void setMaxServiceTime(int maxServiceTime) {
        this.maxServiceTime = maxServiceTime;
    }

    public int getMinServiceTime() {
        return minServiceTime;
    }

    public void setMinServiceTime(int minServiceTime) {
        this.minServiceTime = minServiceTime;
    }

    public int getMaxArrivalTime() {
        return maxArrivalTime;
    }

    public void setMaxArrivalTime(int maxArrivalTime) {
        this.maxArrivalTime = maxArrivalTime;
    }

    public int getMinArrivalTime() {
        return minArrivalTime;
    }

    public void setMinArrivalTime(int minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }

    public int getNumberOfServers() {
        return numberOfServers;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

}