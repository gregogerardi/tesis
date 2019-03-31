package edu.isistan.mobileGrid.node;

import edu.isistan.mobileGrid.network.Message;
import edu.isistan.mobileGrid.network.Node;
import edu.isistan.simulator.Entity;
import edu.isistan.simulator.Event;
import edu.isistan.simulator.Logger;
import edu.isistan.simulator.Simulation;

import java.util.HashMap;

public abstract class ReSenderEntity extends Entity implements Node {

    private static final long NO_RETRY = 0;
    private long resendInterval = NO_RETRY;
    private int amountOfReintents = (int) NO_RETRY;
    private HashMap<Integer, Integer> messagesReintentsCount=new HashMap<>();
    public static final int EVENT_MESSAGE_RETRY = 99;

    public void setResendInterval(long resendInterval) {
        this.resendInterval = resendInterval;
    }

    public void setAmountOfReintents(int amountOfReintents) {
        this.amountOfReintents = amountOfReintents;
    }

    public ReSenderEntity(String name){
        super(name);
    }

    @Override
    public void fail(Message message) {
        Logger.logString("Message sent failed", message.getSource().getId(), message.getId(),message.getOffset(),message.isLastMessage());
        if (resendInterval !=NO_RETRY){
            if (!messagesReintentsCount.containsKey(message.getId())){
                messagesReintentsCount.put(message.getId(),0);
            }
            int currentAmountOfReintents = messagesReintentsCount.get(message.getId());
            if (currentAmountOfReintents<amountOfReintents) {
                retry(message);
                messagesReintentsCount.replace(message.getId(),++currentAmountOfReintents);
                Logger.logString("Message scheduled to resend", message.getId(),message.getOffset(),message.isLastMessage(),currentAmountOfReintents);
            }
            else{
                messagesReintentsCount.remove(message.getId());
                failToRetry(message);
                Logger.logString("Message sent fail to be resented",message.getSource().getId(), message.getId(),message.getOffset(),message.isLastMessage());
            }
        }
        else{
            failToRetry(message);
        }
    }

    protected abstract void failToRetry(Message message);

    private void retry(Message message){
        long retryTime = Simulation.getTime() + resendInterval;
        Event retryEvent = Event.createEvent(Event.NO_SOURCE, retryTime, this.getId(), EVENT_MESSAGE_RETRY, message);
        Simulation.addEvent(retryEvent);
    }

}
