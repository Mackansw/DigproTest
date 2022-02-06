package se.digpro.client;

import se.digpro.model.ConnectionStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {

    private List<String> pointData = new ArrayList<>();
    private ConnectionStatus clientStatus;

    /**
     * Connects the client to the passed URL and web scrapes each point-data line into a string
     * that is saved to the point data list
     *
     * @param URLLink the URL to connect to
     */
    public void connect(String URLLink) {
        pointData.clear();
        setClientStatus(ConnectionStatus.CONNECTING);
        try {
            URL url = new URL(URLLink);
            setClientStatus(ConnectionStatus.CONNECTED);
            try {
                //Uses the ISO_8859_1 charset when reading the page
                Scanner urlScanner = new Scanner(url.openStream(), StandardCharsets.ISO_8859_1);
                while(urlScanner.hasNext()) {
                    String nextLine = urlScanner.nextLine();
                    if(!nextLine.startsWith("#")) {
                        pointData.add(nextLine);
                    }
                }
            }
            catch(UnknownHostException e){
                System.out.println("There was a problem connecting to the URL!");
                setClientStatus(ConnectionStatus.CONNECTION_FAILED);
            }
            catch(IOException e) {
                System.out.println("Content of URL stream is empty!");
                setClientStatus(ConnectionStatus.CONNECTION_FAILED);
            }
        }
        catch (MalformedURLException e) {
            System.out.println("URL is malformed!");
            setClientStatus(ConnectionStatus.CONNECTION_FAILED);
        }
    }

    public List<String> getPointsData(){
        return this.pointData;
    }

    public ConnectionStatus getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(ConnectionStatus clientStatus) {
        this.clientStatus = clientStatus;
    }
}