package Model;

import java.util.HashMap;

public class Task {
    private int arrivalTime;
    private int serviceTime;
    private int ID;
    private int initialServiceTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.ID = ID;
        this.initialServiceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getID() {
        return ID;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

    public void setInitialServiceTime(int initialServiceTime) {
        this.initialServiceTime = initialServiceTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
