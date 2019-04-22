package edu.isistan.gridgain.information.comparator.seas;

import edu.isistan.mobileGrid.node.Device;
import edu.isistan.mobileGrid.node.SchedulerProxy;
import edu.isistan.seas.proxy.DeviceComparator;

public class MobilityComparator extends DeviceComparator {

    @Override
    public double getValue(Device device) {
        return SchedulerProxy.PROXY.getConnectionScore(device);
    }
}
