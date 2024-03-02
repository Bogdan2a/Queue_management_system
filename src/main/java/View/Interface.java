package View;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class Interface extends JFrame {
    private JTextField minArrivalTimeInput;
    private JTextField maxArrivalTimeInput;
    private JTextField minServiceTimeInput;
    private JTextField maxServiceTimeInput;
    private JTextField queuesInput;
    private JTextField clientsInput;
    private JTextField timeLimitInput;
    private JButton startSimulationButton;
    private JLabel currentTimeOutput;
    private JPanel basePanel;
    private JLabel waitingClientsOutput;
    private JTextArea liveQueues;
    private JLabel averageServiceTimeOutput;
    private JLabel averageWaitingTimeOutput;
    private JLabel peakHourOutput;

    public Interface() {
        setDimension(900, 500);
    }

    public void setDimension(int w, int h) {
        add(basePanel);
        setBounds(300, 200, w, h);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public String getMinArrivalTimeInput() {
        return this.minArrivalTimeInput.getText();
    }

    public String getMaxArrivalTimeInput() {
        return this.maxArrivalTimeInput.getText();
    }

    public String getMinServiceTimeInput() {
        return this.minServiceTimeInput.getText();
    }

    public String getMaxServiceTimeInput() {
        return this.maxServiceTimeInput.getText();
    }

    public String getQueuesInput() {
        return this.queuesInput.getText();
    }

    public String getClientsInput() {
        return this.clientsInput.getText();
    }

    public void setCurrentTimeOutput(String currentTime) {
        currentTimeOutput.setText(currentTime);
    }

    public void setWaitingClientsOutput(String clientsList) {
        waitingClientsOutput.setText(clientsList);
    }

    public void setLiveQueues(String queuesPrint) {
        liveQueues.setText(queuesPrint);
    }

    public void setAverageServiceTimeOutput(String avgServiceTime) {
        averageServiceTimeOutput.setText(avgServiceTime);
    }

    public void setAverageWaitingTimeOutput(String avgWaitingTime) {
        averageWaitingTimeOutput.setText(avgWaitingTime);
    }

    public void setPeakHourOutput(String peakHour, String numberOfClientsInPeakHour) {
        peakHourOutput.setText("At time " + peakHour + ": " + numberOfClientsInPeakHour + " clients in queues");
    }

    public String getTimeLimitInput() {
        return this.timeLimitInput.getText();
    }

    public void addStartSimulationButtonListener(ActionListener listener) {
        this.startSimulationButton.addActionListener(listener);
    }

}
