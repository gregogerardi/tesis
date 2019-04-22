package edu.isistan.mobileGrid.node;

public interface ConnectionManager {

    /**
     * It is call when the device disconnect from the network
     */

    void onDisconnect();

    /**
     * It is call when the device disconnect from the network
     */

    void onConnect();

    /**
     * It is call when the device startup
     */

    void onStartUp();

    /**
     * It returns the connection related score for this device
     */
    double getConnectionScore();

    /**
     * It return true if the device is connected to the network
     */
    boolean isConnected();

    /**
     * It is call when the device shutdown
     */
    void shutdown();
}
