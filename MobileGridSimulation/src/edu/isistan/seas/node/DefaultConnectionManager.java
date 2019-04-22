package edu.isistan.seas.node;

import edu.isistan.mobileGrid.node.*;
import edu.isistan.mobileGrid.node.movingAverageCalculators.factory.ConnectionScoreCalculatorFactory;
import edu.isistan.simulator.Logger;
import edu.isistan.simulator.Simulation;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static edu.isistan.mobileGrid.node.ConnectionScoreCalculator.CONNECT;
import static edu.isistan.mobileGrid.node.ConnectionScoreCalculator.DISCONNECT;

public class DefaultConnectionManager implements ConnectionManager {
    /**
     * represents the current connection state of the device
     */
    private boolean connected;

    private HashMap<Integer, ConnectionData> schedulersConnectionDatas = new HashMap<>();

    private Device device;

    private ExecutionManager executionManager;

    private BatteryManager batteryManager;

    private Class<ConnectionScoreCalculator> connectionScoreCalculatorClass;

    private String[] connectionScoreCalculatorArgs;

    public DefaultConnectionManager(Class<ConnectionScoreCalculator> connectionScoreCalculatorClass, String[] connectionScoreCalculatorArgs) {
        this.connectionScoreCalculatorClass = connectionScoreCalculatorClass;
        this.connectionScoreCalculatorArgs = connectionScoreCalculatorArgs;
    }

    public void setExecutionManager(ExecutionManager executionManager) {
        this.executionManager = executionManager;
    }

    public void setBatteryManager(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    public double getConnectionScore() {
        return schedulersConnectionDatas.get(SchedulerProxy.PROXY.getId()).getScore();

    }

    @Override
    public void onDisconnect() {
        connected = false;
        Logger.logEntity(device, "Device is out from the network");
        schedulersConnectionDatas.get(SchedulerProxy.PROXY.getId()).onDisconnectionTime(Simulation.getTime());
    }

    @Override
    public void onConnect() {
        connected = true;
        Logger.logEntity(device, "Device is into the network");
        if (!schedulersConnectionDatas.containsKey(SchedulerProxy.PROXY.getId())) {
            try {
                schedulersConnectionDatas.put(SchedulerProxy.PROXY.getId(), new ConnectionData(ConnectionScoreCalculatorFactory.createConnectionScoreCalculator(connectionScoreCalculatorClass, connectionScoreCalculatorArgs)));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        schedulersConnectionDatas.get(SchedulerProxy.PROXY.getId()).onConnectionTime(Simulation.getTime());
    }

    @Override
    public void onStartUp() {
        onDisconnect();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void shutdown() {
        if (this.isConnected()) this.onDisconnect();
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private class ConnectionData {

        private ConnectionScoreCalculator movingScoreCalculator;

        public ConnectionData(ConnectionScoreCalculator movingScoreCalculator) {
            this.movingScoreCalculator = movingScoreCalculator;
        }

        public void onDisconnectionTime(long time) {
            movingScoreCalculator.average(time, DISCONNECT);
        }

        public void onConnectionTime(long time) {
            movingScoreCalculator.average(time, CONNECT);
        }

        public double getScore() {
            return movingScoreCalculator.getScore();
        }
    }
}
