package se.digpro.model;

/**
 * The clients different connection statuses
 */
public enum ConnectionStatus {

    /**
     * Used when the client is connecting to the server
     */
    CONNECTING,

    /**
     * Used when the client is successfully connected to the server
     */
    CONNECTED,

    /**
     * Used when the client failed to connect to the server or if there was a problem with the server URL
     */
    CONNECTION_FAILED,
}