package edu.isistan.connectioneventsgenerator.intervalcalculators;


public abstract class IntervalCalculator {

    private TypeOfNextEvent typeOfNextEvent;

    public TypeOfNextEvent getTypeOfNextEvent() {
        return typeOfNextEvent;
    }

    public enum TypeOfNextEvent {
        CONNECTION, DISCONNECTION;

        public TypeOfNextEvent swap() {
            return this == CONNECTION ? DISCONNECTION : CONNECTION;
        }
    }

    public IntervalCalculator() {
        typeOfNextEvent = TypeOfNextEvent.CONNECTION;
    }

    public long getNewInterval() {
        long newEventTime = typeOfNextEvent == TypeOfNextEvent.CONNECTION ? getNextConnectionEvent() : getNextDisconnectionEvent();
        typeOfNextEvent = typeOfNextEvent.swap();
        return newEventTime;
    }

    abstract long getNextConnectionEvent();

    abstract long getNextDisconnectionEvent();
}
