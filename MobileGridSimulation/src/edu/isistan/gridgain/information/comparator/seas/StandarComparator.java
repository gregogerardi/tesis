package edu.isistan.gridgain.information.comparator.seas;

import edu.isistan.mobileGrid.node.Device;
import edu.isistan.seas.proxy.DeviceComparator;
import edu.isistan.seas.util.ProgressiveStandardizer;

public abstract class StandarComparator extends DeviceComparator {
    ProgressiveStandardizer progressiveStandardizer;
    DeviceComparator deviceComparator;

    public StandarComparator(ProgressiveStandardizer progressiveStandardizer, DeviceComparator deviceComparator) {
        this.progressiveStandardizer = progressiveStandardizer;
        this.deviceComparator = deviceComparator;
    }

    @Override
    public double getValue(Device device) {
        double score = deviceComparator.getValue(device);
        return progressiveStandardizer.getStandar(score);
    }
}
