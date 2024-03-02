package org.example;

import BusinessLogic.SimulationManager;
import View.Controller;
import View.Interface;

public class App {
    public static void main(String[] args) {

        Interface view = new Interface();
        view.setVisible(true);
        Controller controller = new Controller(view);
     //
     //   System.exit(0);
    }
}
