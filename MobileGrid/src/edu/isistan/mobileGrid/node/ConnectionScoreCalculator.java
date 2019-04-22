package edu.isistan.mobileGrid.node;

/**
 * Moving average calculator interface to get a score for the time a device remain connected to the proxy.
 * Every class that implements this interface should have a constructor with only one String[]args as param
 */
public interface ConnectionScoreCalculator {
    boolean CONNECT = true;
    boolean DISCONNECT = false;

    void average(long time, boolean connect);

    /**
     * return a long value that means the expected amount of time that the device will be connectd
     */

    double getScore();
}
