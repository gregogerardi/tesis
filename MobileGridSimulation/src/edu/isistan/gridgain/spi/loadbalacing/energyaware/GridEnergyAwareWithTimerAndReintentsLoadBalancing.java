package edu.isistan.gridgain.spi.loadbalacing.energyaware;

import edu.isistan.mobileGrid.network.Message;
import edu.isistan.mobileGrid.network.NetworkModel;
import edu.isistan.simulator.Event;
import edu.isistan.simulator.Logger;


public class GridEnergyAwareWithTimerAndReintentsLoadBalancing extends GridEnergyAwareLoadBalancing {

    public GridEnergyAwareWithTimerAndReintentsLoadBalancing(String name) {
        super(name);
    }

    public GridEnergyAwareWithTimerAndReintentsLoadBalancing(String name, Long resendInterval,  Integer amountOfReintents) {
        super(name);
        setRetry(resendInterval, amountOfReintents);
    }

    private void setRetry(Long resendInterval, Integer amountOfReintents) {
        setResendInterval(resendInterval);
        setAmountOfReintents(amountOfReintents);
    }

    public GridEnergyAwareWithTimerAndReintentsLoadBalancing(String name, String arguments) {
        super(name);
        String[] schedulerConstructorArguments = arguments.split("-");
        Long resendInterval = Long.parseLong(schedulerConstructorArguments[0]);
        Integer amountOfReintents = Integer.parseInt(schedulerConstructorArguments[1]);
        setRetry(resendInterval,amountOfReintents);
    }

    @Override
    public void processEvent(Event event) {
        if (EVENT_MESSAGE_RETRY != event.getEventType()) super.processEvent(event);
        else {
            Message message= (Message) event.getData();
            if ((message.getDestination().isOnline())&&(!message.getDestination().isSending())&&(!message.getDestination().isReceiving())){
                Logger.logString("Message resended", message.getId(),message.getOffset(),message.isLastMessage());
                NetworkModel.getModel().send(message);
            }
            else{
                this.fail(message);
            }
        }
    }


}
