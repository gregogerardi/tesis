package edu.isistan.seas.node;

import edu.isistan.mobileGrid.node.BatteryManager;
import edu.isistan.mobileGrid.node.ConnectionManager;
import edu.isistan.mobileGrid.node.Device;
import edu.isistan.mobileGrid.node.ExecutionManager;
import edu.isistan.simulator.Logger;

public class DefaultConnectionManager implements ConnectionManager {
    /**
     *represents the current connection state of the device
     */
    private boolean connected;

    private Device device;

    private ExecutionManager executionManager;

    private BatteryManager batteryManager;

    public void setExecutionManager(ExecutionManager executionManager) {
        this.executionManager = executionManager;
    }

    public void setBatteryManager(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    @Override
    public void onDisconnect() {
        connected=false;
        executionManager.onDisconnect();
        Logger.logEntity(device, "Device leave the network");
    }

    @Override
    public void onConnect() {
        connected=true;
        Logger.logEntity(device, "Device enter the network");
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void shutdown() {
        this.onDisconnect();
    }

    public void setDevice(Device device) {
        this.device=device;
    }
}
