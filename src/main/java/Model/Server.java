package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private ArrayBlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private Integer ServerID;

    public Server(Integer ServerID) {
        this.ServerID = ServerID;
        this.tasks = new ArrayBlockingQueue<>(200);
        this.waitingPeriod = new AtomicInteger(0);
    }

    public Integer getServerID() {
        return ServerID;
    }

    public void decrementWaitingPeriod(Integer waitingPeriodInt) {

        int newValue = waitingPeriod.addAndGet(-waitingPeriodInt);
    }

    public ArrayBlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public synchronized void addTask(Task newTask) {
        tasks.add(newTask);
        int serviceTime = newTask.getServiceTime();
        waitingPeriod.addAndGet(serviceTime);
    }

    public void run() {
        while (true) {
            Task task = tasks.peek();
            if (task != null) {
                try {
                    Thread.sleep(task.getServiceTime() * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tasks.poll();
            }
        }
    }


}