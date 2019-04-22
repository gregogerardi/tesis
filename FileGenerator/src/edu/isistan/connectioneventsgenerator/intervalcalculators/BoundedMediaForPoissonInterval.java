package edu.isistan.connectioneventsgenerator.intervalcalculators;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BoundedMediaForPoissonInterval extends IntervalCalculator {
    private static boolean parametersSetted;
    private static long maxTimeToConnect;
    private static long minTimeToConnect;
    private static long maxTimeToDisconnect;
    private static long minTimeToDisconnect;
    private final long averageTimeToConnect;
    private final long averageTimeToDisconnect;
    private final double lambdaTimeToConnect;
    private final double lambdaTimeToDisconnect;
    private static Random random = new Random();

    private void setParameters(long maxTimeToConnect, long minTimeToConnect, long maxTimeToDisconnect, long minTimeToDisconnect) {
        this.maxTimeToConnect = maxTimeToConnect;
        this.minTimeToConnect = minTimeToConnect;
        this.maxTimeToDisconnect = maxTimeToDisconnect;
        this.minTimeToDisconnect = minTimeToDisconnect;
        this.parametersSetted = true;
    }

    public BoundedMediaForPoissonInterval(long maxTimeToConnect, long minTimeToConnect, long maxTimeToDisconnect, long minTimeToDisconnect) {
        if (!isParametersSetted())
            setParameters(maxTimeToConnect, minTimeToConnect, maxTimeToDisconnect, minTimeToDisconnect);
        averageTimeToConnect = ThreadLocalRandom.current().nextLong(minTimeToConnect, maxTimeToConnect);
        averageTimeToDisconnect = ThreadLocalRandom.current().nextLong(minTimeToDisconnect, maxTimeToDisconnect);
        lambdaTimeToConnect = 1d/averageTimeToConnect;
        lambdaTimeToDisconnect = 1d/averageTimeToDisconnect;
    }

    public BoundedMediaForPoissonInterval(String args) {
        String[] arguments = args.split("-");
        if (arguments.length != 4) throw new IllegalArgumentException("Wrong amount of arguments");
        else {
            if (!isParametersSetted())
                setParameters(Long.parseLong(arguments[0]), Long.parseLong(arguments[1]), Long.parseLong(arguments[2]), Long.parseLong(arguments[3]));
            averageTimeToConnect = ThreadLocalRandom.current().nextLong(minTimeToConnect, maxTimeToConnect);
            averageTimeToDisconnect = ThreadLocalRandom.current().nextLong(minTimeToDisconnect, maxTimeToDisconnect);
            lambdaTimeToConnect = 1d/averageTimeToConnect;
            lambdaTimeToDisconnect = 1d/averageTimeToDisconnect;
        }
    }

    @Override
    long getNextConnectionEvent() {
        return (long) getNextExponential(lambdaTimeToConnect);
    }

    @Override
    long getNextDisconnectionEvent() {
        return (long) getNextExponential(lambdaTimeToDisconnect);
    }

    public boolean isParametersSetted() {
        return parametersSetted;
    }

    public double getNextExponential(double lambda) {
        double random;
        return random=Math.random()!=0?-(Math.log(Math.random()) / lambda):Double.MAX_VALUE;
    }
}
