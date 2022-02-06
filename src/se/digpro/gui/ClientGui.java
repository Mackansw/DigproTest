package se.digpro.gui;

import se.digpro.model.ConnectionStatus;
import se.digpro.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClientGui {

    //Gui components
    private final JFrame window;
    private final JPanel guiContainerPanel;
    private final JPanel statusMessagePanel;
    private final JPanel pointsContainerPanel;
    private final JPanel actionsPanel;
    private final JPanel checkboxPanel;
    private final JPanel buttonPanel;
    private final JLabel statusMessageLabel;
    private final JCheckBox disableUpdatesCheckbox;
    private final JButton refreshButton;
    private final JButton aboutButton;
    private final JButton exitButton;

    private final Color guiBackgroundColor = Color.darkGray;
    private final Color guiForegroundColor = Color.white;

    //Returns all vertex points to render in pointsContainerPanel
    private List<VertexPoint> pointVertices = new ArrayList<>();

    /**
     * Default constructor of Client GUI
     */
    public ClientGui() {
        window = new JFrame("Recruitment test");
        window.setSize(800, 600);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        guiContainerPanel = new JPanel();
        guiContainerPanel.setLayout(new BorderLayout());
        guiContainerPanel.setBackground(guiBackgroundColor);

        pointsContainerPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                renderPoints(g);
            }

            @Override
            public String getToolTipText(MouseEvent event) {
                for(VertexPoint vertex : pointVertices) {
                    if(vertex.contains(event.getPoint())) {
                        return vertex.getPoint().getName();
                    }
                }
                /* Hides tooltip when no point is focused on */
                return null;
            }
        };
        guiContainerPanel.add(pointsContainerPanel);
        pointsContainerPanel.setBackground(guiBackgroundColor);
        pointsContainerPanel.setToolTipText("");

        statusMessagePanel = new JPanel();
        pointsContainerPanel.add(statusMessagePanel);
        statusMessagePanel.setBackground(guiBackgroundColor);
        statusMessagePanel.setLayout(new BorderLayout());

        statusMessageLabel = new JLabel("Connecting...", JLabel.CENTER);
        statusMessagePanel.add(statusMessageLabel);
        statusMessageLabel.setForeground(guiForegroundColor);
        Font statusFont = new Font(Font.MONOSPACED, Font.CENTER_BASELINE, 23);
        statusMessageLabel.setFont(statusFont);

        actionsPanel = new JPanel();
        guiContainerPanel.add(actionsPanel, BorderLayout.SOUTH);
        actionsPanel.setBackground(guiBackgroundColor.brighter());
        actionsPanel.setLayout(new GridLayout(2,1));

        checkboxPanel = new JPanel();
        actionsPanel.add(checkboxPanel);
        checkboxPanel.setBackground(guiBackgroundColor.brighter().brighter().brighter());

        disableUpdatesCheckbox = new JCheckBox("Disable auto updates");
        checkboxPanel.add(disableUpdatesCheckbox);
        disableUpdatesCheckbox.setBackground(guiBackgroundColor.brighter().brighter().brighter());
        disableUpdatesCheckbox.setForeground(Color.black);

        buttonPanel = new JPanel();
        actionsPanel.add(buttonPanel);
        buttonPanel.setBackground(guiBackgroundColor.brighter().brighter().brighter());

        refreshButton = new JButton("Refresh");
        buttonPanel.add(refreshButton);
        refreshButton.setBackground(guiBackgroundColor.brighter().brighter());
        refreshButton.setForeground(guiForegroundColor);

        aboutButton = new JButton("About");
        buttonPanel.add(aboutButton);
        aboutButton.setBackground(guiBackgroundColor.brighter().brighter());
        aboutButton.setForeground(guiForegroundColor);

        exitButton = new JButton("Exit");
        buttonPanel.add(exitButton);
        exitButton.setBackground(guiBackgroundColor.brighter().brighter());
        exitButton.setForeground(guiForegroundColor);

        window.add(guiContainerPanel);
        window.setVisible(true);
    }

    /**
     * Creates and shows a themed dialog message
     * @param message the message to show
     */
    public void showMessageDialog(String message) {
        UIManager.put("Panel.background", this.guiBackgroundColor);
        UIManager.put("Button.background", this.guiBackgroundColor.brighter().brighter());
        UIManager.put("Button.foreground", this.guiForegroundColor);
        UIManager.put("OptionPane.messageForeground", this.guiForegroundColor);
        UIManager.put("OptionPane.background", this.guiBackgroundColor);
        JOptionPane.showMessageDialog(this.window, message);
    }

    /**
     * Updates the state of the GUIs state based in the clients connection status
     *
     * @param clientStatus the connection status of the client
     */
    public void updateGuiState(ConnectionStatus clientStatus) {
        switch(clientStatus) {
            case CONNECTED:
                pointsContainerPanel.remove(statusMessagePanel);
                break;
            case CONNECTION_FAILED:
                setStatusMessage("Connnection failed!");
                break;
            case CONNECTING:
                setStatusMessage("Connecting...");
                pointsContainerPanel.add(statusMessagePanel);
                break;
        }
        guiContainerPanel.revalidate();
        guiContainerPanel.repaint();
    }

    /**
     * Renders the points on the pointsContainerPanel
     * @param g the graphics object
     */
    private void renderPoints(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        VertexPoint origo = new VertexPoint(new Point(pointsContainerPanel.getWidth() / 2, pointsContainerPanel.getHeight() / 2,"Origo"));

        for(VertexPoint vertex : pointVertices) {
            g2d.setColor(Color.yellow);
            g2d.fill(vertex);
        }

        g2d.setColor(Color.red);
        g2d.fill(origo);

        g2d.dispose();

        //Adds origo to vertex list last to prevent duplicate-renders
        pointVertices.add(origo);
    }

    /**
     * Converts a list of points to vertex-points and sets the vertices to render
     * to the converted list
     * @param pointsIn the points to convert
     */
    public void setVertexFromPoints(List<Point> pointsIn) {
        List<Point> scaledPoints = scalePoints(pointsIn);
        List<VertexPoint> vertexes = new ArrayList<>();

        for(Point point : scaledPoints) {
            VertexPoint vertex = new VertexPoint(point);
            vertexes.add(vertex);
        }
        this.pointVertices = vertexes;
    }

    /**
     * Scales the Coordinates of all points in a list to be visible inside of a window without breaking their patter
     * @param pointsIn the points to scale
     * @return an arraylist of Points
     */
    private List<Point> scalePoints (List<Point> pointsIn) {

        //The center X and Y of the component that renders points
        int origoX = pointsContainerPanel.getWidth() / 2;
        int origoY = pointsContainerPanel.getHeight() / 2;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        //Finds the max and min of X and Y in the provided list of points
        for(Point point : pointsIn) {
            if(point.getX() > maxX) {
                maxX = point.getX();
            }
            if(point.getX() < minX) {
                minX = point.getX();
            }
            if(point.getY() > maxY) {
                maxY = point.getY();
            }
            if(point.getY() < minY) {
                minY = point.getY();
            }
        }

        //Finds the absolute max's of X and Y
        double maxDistanceX = Math.max(Math.abs(minX), Math.abs(maxX));
        double maxDistanceY = Math.max(Math.abs(minY), Math.abs(maxY));

        //Calculates the X and Y coefficients by dividing their respective origos and maxDistances
        double coefficientX = origoX / maxDistanceX;
        double coefficientY = origoY / maxDistanceY;

        //Defines The final coefficient by taking the min of coefficient X and Y in order to scale enough times
        double coefficient = Math.min(coefficientX, coefficientY);

        //Applies scaling to all points by multiplying their X and Y by the final coefficient
        for(Point p : pointsIn) {
            double newXDouble = p.getX() * coefficient;
            double newYDouble = p.getY() * coefficient;

            int newX = (int) newXDouble;
            int newY = (int) newYDouble;

            p.setX(origoX + newX);
            p.setY(origoY + newY);
        }

        return pointsIn;
    }

    public void addRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void addAboutButtonListener(ActionListener listener) {
        aboutButton.addActionListener(listener);
    }

    public void addExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public void addDisableUpdatesBoxListener(ActionListener listener) {
        disableUpdatesCheckbox.addActionListener(listener);
    }

    public boolean selectedDisableUpdates() {
        return this.disableUpdatesCheckbox.isSelected();
    }

    private void setStatusMessage(String message) {
        this.statusMessageLabel.setText(message);
    }

    public void clearVertexes() {
        pointVertices.clear();
    }
}