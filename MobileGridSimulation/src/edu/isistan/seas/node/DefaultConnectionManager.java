package edu.isistan.seas.node;

import edu.isistan.mobileGrid.node.ConnectionManager;
import edu.isistan.mobileGrid.node.Device;

public class DefaultConnectionManager implements ConnectionManager {
    /**
     *represents the current connection state of the device
     */
    private boolean connected;

    private Device device;

    @Override
    public void onDisconnect() {
        connected=false;
    }

    @Override
    public void onConnect() {
        connected=true;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public void setDevice(Device device) {
        this.device=device;
    }
}
