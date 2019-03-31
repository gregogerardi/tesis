package edu.isistan.seas.reader;

import edu.isistan.mobileGrid.node.*;
import edu.isistan.seas.node.DefaultBatteryManager;
import edu.isistan.seas.node.DefaultConnectionManager;
import edu.isistan.seas.node.DefaultExecutionManager;
import edu.isistan.seas.node.DefaultNetworkEnergyManager;
import edu.isistan.seas.node.jobstealing.JSDevice;
import edu.isistan.seas.node.jobstealing.JSSEASBatteryManager;
import edu.isistan.seas.node.jobstealing.JSSEASExecutionManager;

public class JobStealingFactory implements ManagerFactory {

	@Override
	public DefaultBatteryManager createBatteryManager(int prof, int charge,
			long estUptime,long batteryCapacityInJoules) {
		return new JSSEASBatteryManager(prof, charge, estUptime,batteryCapacityInJoules);
	}

	@Override
	public DefaultExecutionManager createExecutionManager() {
		return new JSSEASExecutionManager();
	}

	@Override
	public DefaultNetworkEnergyManager createNetworkEnergyManager(
			boolean enableNetworkExecutionManager, short wifiSignalStrength) {		
		return new DefaultNetworkEnergyManager(enableNetworkExecutionManager,wifiSignalStrength);
	}

	//todo provide a connection events aware manager for JSDevices
	@Override
	public Device createDevice(String name, BatteryManager bt, ExecutionManager em, NetworkEnergyManager nem, ConnectionManager cm) {
		return new JSDevice(name, bt, em, nem, cm);
	}

	@Override
	public Device createDevice(String nodeName, DefaultBatteryManager batteryManager, DefaultExecutionManager executionManager, DefaultNetworkEnergyManager networkEnergyManager, DefaultConnectionManager connectionManager, int retryInterval, int amountOfRetries) {
		//TODO Provide a resend aware treatment for jobstealingfactory
		return new JSDevice(nodeName, batteryManager, executionManager, networkEnergyManager, connectionManager);
	}

	//todo provide a connection events aware treatment for the jobstealin logic
	//for now we use the default connectionManager
	@Override
	public DefaultConnectionManager createConnectionManager() {
		return new DefaultConnectionManager();
	}
}
