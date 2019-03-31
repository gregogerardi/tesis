package edu.isistan.seas.reader;

import edu.isistan.mobileGrid.node.*;
import edu.isistan.seas.node.DefaultBatteryManager;
import edu.isistan.seas.node.DefaultConnectionManager;
import edu.isistan.seas.node.DefaultExecutionManager;
import edu.isistan.seas.node.DefaultNetworkEnergyManager;

public interface ManagerFactory {

	DefaultBatteryManager createBatteryManager(int prof, int charge, long estUptime, long batteryCapacityInJoules);
	
	DefaultExecutionManager createExecutionManager();

	DefaultNetworkEnergyManager createNetworkEnergyManager(boolean enableNetworkExecutionManager, short wifiSignalString);
	
	Device createDevice(String name, BatteryManager bt, ExecutionManager em, NetworkEnergyManager nem, ConnectionManager cm);

    DefaultConnectionManager createConnectionManager();

    Device createDevice(String nodeName, DefaultBatteryManager batteryManager, DefaultExecutionManager executionManager, DefaultNetworkEnergyManager networkEnergyManager, DefaultConnectionManager connectionManager, int retryInterval, int amountOfRetries);

}
