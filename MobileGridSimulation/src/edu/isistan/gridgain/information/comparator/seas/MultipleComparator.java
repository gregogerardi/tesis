package edu.isistan.gridgain.information.comparator.seas;

import edu.isistan.mobileGrid.node.Device;
import edu.isistan.seas.proxy.DeviceComparator;
import java.util.HashMap;
import java.util.Map;

public class MultipleComparator extends DeviceComparator {

    private Map<DeviceComparator, Double> comparatorsPriorities = new HashMap<>();

    public MultipleComparator(Map<DeviceComparator, Double> comparatorsPriorities) throws Exception {
        double acumWeight=0d;
        for (Double d:comparatorsPriorities.values()){
            acumWeight+=d;
        }
        if (acumWeight!=1d){
            throw new Exception("The weights of comparators do not acum one");
        }
        else {
            this.comparatorsPriorities = comparatorsPriorities;
        }
    }

    @Override
    public double getValue(Device device) {
        double value = 0;
        for (Map.Entry<DeviceComparator,Double> e: comparatorsPriorities.entrySet()){
            value+=e.getKey().getValue(device)*e.getValue();
        }
        return value;
    }
}
