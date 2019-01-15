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
     * It return true if the device is connected to the network
     */

    boolean isConnected();

    /**
     * It is call when the device shutdown
     */
    void shutdown();
}
