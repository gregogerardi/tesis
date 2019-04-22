package edu.isistan.connectioneventsgenerator.intervalcalculators;

import java.util.concurrent.ThreadLocalRandom;

public class BoundedRandomInterval extends IntervalCalculator {
    private long maxTimeToConnect;
    private long minTimeToConnect;
    private long maxTimeToDisconnect;
    private long minTimeToDisconnect;

    public BoundedRandomInterval(long maxTimeToConnect, long minTimeToConnect, long maxTimeToDisconnect, long minTimeToDisconnect) {
        this.maxTimeToConnect = maxTimeToConnect;
        this.minTimeToConnect = minTimeToConnect;
        this.maxTimeToDisconnect = maxTimeToDisconnect;
        this.minTimeToDisconnect = minTimeToDisconnect;
    }

    public BoundedRandomInterval(String args) {
        String[] arguments=args.split("-");
        if (arguments.length!=4)throw new IllegalArgumentException("Wrong amount of arguments");
        else{
            this.maxTimeToConnect = Long.parseLong(arguments[0]);
            this.minTimeToConnect = Long.parseLong(arguments[1]);
            this.maxTimeToDisconnect = Long.parseLong(arguments[2]);
            this.minTimeToDisconnect = Long.parseLong(arguments[3]);
        }
    }

        @Override
    long getNextConnectionEvent() {
        return ThreadLocalRandom.current().nextLong(minTimeToConnect,maxTimeToConnect);
    }

    @Override
    long getNextDisconnectionEvent() {
        return ThreadLocalRandom.current().nextLong(minTimeToDisconnect,maxTimeToDisconnect);
    }
}
