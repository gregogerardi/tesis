package edu.isistan.seas.reader;

import edu.isistan.mobileGrid.node.*;
import edu.isistan.seas.node.DefaultBatteryManager;
import edu.isistan.seas.node.DefaultConnectionManager;
import edu.isistan.seas.node.DefaultExecutionManager;
import edu.isistan.seas.node.DefaultNetworkEnergyManager;

public class DefaultManagerFactory implements ManagerFactory {

	@Override
	public DefaultBatteryManager createBatteryManager(int prof, int charge, long estUptime, long batteryCapacityInJoules) {
		return new DefaultBatteryManager(prof, charge, estUptime, batteryCapacityInJoules);
	}

	@Override
	public DefaultExecutionManager createExecutionManager() {
		return new DefaultExecutionManager();
	}

	@Override
	public DefaultConnectionManager createConnectionManager() {
		return new DefaultConnectionManager();
	}

	@Override
	public DefaultNetworkEnergyManager createNetworkEnergyManager(boolean enableNetworkExecutionManager, short wifiSignalString) {		
		return new DefaultNetworkEnergyManager(enableNetworkExecutionManager,wifiSignalString);
	}

	@Override
	public Device createDevice(String name, BatteryManager bt, ExecutionManager em, NetworkEnergyManager nem, ConnectionManager cm) {
		return new Device(name,bt,em,nem,cm);
	}

	@Override
	public Device createDevice(String nodeName, DefaultBatteryManager batteryManager, DefaultExecutionManager executionManager, DefaultNetworkEnergyManager networkEnergyManager, DefaultConnectionManager connectionManager, int retryInterval, int amountOfRetries) {
		return new Device(nodeName,batteryManager,executionManager,networkEnergyManager,connectionManager,retryInterval,amountOfRetries);
	}
}
