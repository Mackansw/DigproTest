package se.digpro.main;

import se.digpro.client.Client;
import se.digpro.controller.ClientController;
import se.digpro.gui.ClientGui;

public class Start {

    /**
     * Main method
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Client client = new Client();
        ClientGui gui = new ClientGui();
        ClientController controller = new ClientController(client, gui);
    }
}