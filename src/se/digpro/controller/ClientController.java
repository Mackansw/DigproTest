package se.digpro.controller;

import se.digpro.client.Client;
import se.digpro.gui.ClientGui;
import se.digpro.model.ConnectionStatus;
import se.digpro.model.Point;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ClientController {

    private Client client;
    private ClientGui gui;

    /**
     * Gets new points from the server every 30 seconds
     */
    private Timer refreshTimer = new Timer(30000, e-> {
        updatePoints();
    });

    /**
     * Controller constructor with client and gui parameters
     * @param client the client to connect
     * @param gui the client gui to show
     */
    public ClientController(Client client, ClientGui gui) {
        this.client = client;
        this.gui = gui;

        //Defines Gui components actions
        gui.addRefreshButtonListener(new RefreshButtonListerner());
        gui.addAboutButtonListener(new aboutButtonListener());
        gui.addExitButtonListener(new exitButtonListener());
        gui.addDisableUpdatesBoxListener(new DisableUpdatesBoxListener());

        refreshTimer.start();

        //Fetches initial points
        updatePoints();
    }

    /**
     * The action of the guis exit button
     */
    class exitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * The action of the guis about button
     */
    class aboutButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gui.showMessageDialog("Kontaktuppgifter: \nMail: mackansw@gmail.com \n" +"Mobil: 0760341432");
        }
    }

    /**
     * The action of the refesh button
     */
    class RefreshButtonListerner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updatePoints();
        }
    }

    /**
     * The action of the disable updates checkbox
     */
    class DisableUpdatesBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(gui.selectedDisableUpdates()) {
                refreshTimer.stop();
            }
            else {
                refreshTimer.start();
            }
        }
    }

    /**
     * Clears and Updates the points data and the gui state
     * Starts a new thread in order to not freeze the gui thread
     */
    private void updatePoints() {
        new Thread(() -> {
            this.gui.clearVertexes();
            this.gui.updateGuiState(ConnectionStatus.CONNECTING);
            this.client.connect("http://daily.digpro.se/bios/servlet/bios.servlets.web.RecruitmentTestServlet");

            List<Point> pointsFromClient = getPointsFromList(client.getPointsData());
            printPointsFromList(pointsFromClient);

            this.gui.setVertexFromPoints(pointsFromClient);
            this.gui.updateGuiState(client.getClientStatus());
        }).start();
    }

    /**
     * Prints all point elemnts in an arraylist
     * @param points the arraylist of points to print
     */
    public void printPointsFromList(List<Point> points) {
        points.forEach(point -> {
            System.out.println(point.getX() + " " + point.getY() + " " + point.getName());
        });
    }

    /**
     * Converts a list of point-data strings to a list of point objects
     *
     * @param pointLinesIn the points data strings to convery
     * @return an arraylist of points
     */
    private List<Point> getPointsFromList(List<String> pointLinesIn) {
        List<Point> pointsFromList = new ArrayList<>();

        pointLinesIn.forEach(line -> {
            int comma = line.indexOf(",");
            int X = Integer.parseInt(line.substring(0, comma));
            int Y = Integer.parseInt(line.substring(comma + 1, line.lastIndexOf(",")).trim());
            String Name = line.substring(line.lastIndexOf(",") + 1).trim();

            Point point = new Point(X,Y,Name);
            pointsFromList.add(point);
        });
        return pointsFromList;
    }
}