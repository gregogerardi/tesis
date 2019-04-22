package edu.isistan.connectioneventsgenerator.intervalcalculators;

public class AlwaysConnectedInterval extends IntervalCalculator {
    @Override
    long getNextConnectionEvent() {
        return 0;
    }

    @Override
    long getNextDisconnectionEvent() {
        return Long.MAX_VALUE;
    }

    public AlwaysConnectedInterval(String args) {
    }
}
