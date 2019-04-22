package edu.isistan.mobileGrid.network;

import edu.isistan.mobileGrid.node.Device;
import edu.isistan.simulator.Entity;
import edu.isistan.simulator.Event;
import edu.isistan.simulator.Logger;
import edu.isistan.simulator.Simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class of all possible models that simulate the network through which all entities in the grid are connected.
 * This class is in charge of relaying messages from one entity to another and simulating their links.
 */
public abstract class NetworkModel {
    private static final String NETWORK_ENTITY_NAME = "network";
    private static double AckMessageSizeInBytes = 0; //2346; //http://stackoverflow.com/questions/5543326/what-is-the-total-length-of-pure-tcp-ack-sent-over-ethernet

    /**
     * Singleton holder.
     */
    private static volatile NetworkModel MODEL;

    /**
     * Lock for implementing double checked locking pattern.
     */
    private static final Object sLock = new Object();

	/*
	public static void setModel(NetworkModel model){
		MODEL = model;
	}
	*/

    /**
     * Singleton pattern implementation.
     *
     * @return The concrete network model implementation.
     */
    public static NetworkModel getModel() {
        if (MODEL == null) {
            synchronized (sLock) {
                if (MODEL == null) {
                    MODEL = new SimpleNetworkModel();
                }
            }
        }

        return MODEL;
    }

    private NetworkDelayEntity networkDelayEntity;

    /**
     * Might Cause IllegalArgumentException if other model was already created
     * and the simulation was not reset.
     */
    protected NetworkModel() {
        super();
        this.networkDelayEntity = new NetworkDelayEntity(NETWORK_ENTITY_NAME);
        Simulation.addEntity(this.networkDelayEntity);
    }

    public abstract long getTransmissionTime(Node scr, Node dst, int messageSize);

    /**
     * Sends a message through the network.
     *
     * @param source      The sender of the message.
     * @param destination The recipient of the message
     * @param id          An id for the message.
     * @param length      The size of the message in bytes.
     * @param data        The payload of the message.
     * @param offset      The order of a fragmented message. Used to reconstitute larger messages decomposed into several
     *                    smaller packages.
     * @param lastMessage A flag indicating whether additional messages should be expected containing more data that
     *                    should be appended to this message's payload.
     * @param <T>         The type of the payload object.
     * @return The time of the simulation at which the sent message is expected to be received by the receiver.
     */
    public abstract <T> long send(Node source, Node destination, int id, int length, T data, int offset, boolean lastMessage);

    /**
     * Adds a new node
     *
     * @param n The node to add.
     */
    public abstract void addNewNode(Node n);

    /**
     * Adds new link
     *
     * @param l The link to add.
     */
    public abstract void addNewLink(Link l);

    /**
     * Removes a node and all its associated links
     *
     * @param n The node to be removed.
     */
    public abstract void removeNode(Node n);

    /**
     * Removes a link
     *
     * @param l The link to be removed.
     */
    public abstract void removeLink(Link l);

    /**
     * Gets the nodes in the network
     *
     * @return The nodes in the network.
     */
    public abstract Set<Node> getNodes();

    public double getAckMessageSizeInBytes() {
        return AckMessageSizeInBytes;
    }

    public void setAckMessageSizeInBytes(double ackMessageSizeInBytes) {
        AckMessageSizeInBytes = ackMessageSizeInBytes;
    }

    protected int getNetworkDelayEntityId() {
        return networkDelayEntity.getId();
    }

    private Set<Message> messagesBeingTransmitted = new HashSet<>();

    protected <T> void addMessageBeingTransmited(Message<T> message) {
        messagesBeingTransmitted.add(message);
        Logger.logString("Message being transmited: ", message.getId(), message.getOffset());
    }

    /**
     * Provides a treatment for node disconnections
     *
     * @param node The node disconnected from the network
     */
    public void onDisconnectNode(Node node) {
        ArrayList<Message> messagesToBeDeleted=new ArrayList<>();
        for (Message message : messagesBeingTransmitted) {
            if (message.getDestination().equals(node) || message.getSource().equals(node)) {
                messagesToBeDeleted.add(message);
            }
        }
        for(Message m :messagesToBeDeleted){
            Logger.logString("Message being transmited canceled because src or dst disconnected: ", m.getId(), m.getOffset());
            messagesBeingTransmitted.remove(m);
            m.getSource().fail(m);
            m.getDestination().failReception(m.getSource(), m.getId());
        }
    }

    public abstract <T> long send(Message message);

    /**
     * Proxy that simulates the overhead associated with data transmissions. When re-routing messages through this
     * entity, the {@link Device#onMessageReceived(Message)} method will be invoked on the destination device before
     * relaying the respective message. As for the source, either {@link Device#onMessageSentAck(Message)} or
     * {@link Device#fail(Message)} will be invoked to notify if the transfer was successful.
     */
    protected class NetworkDelayEntity extends Entity {

        NetworkDelayEntity(String name) {
            super(name);
        }

        /**
         * Events defined by the {@link Message} class are relayed to both the sender and receiver so they can act upon
         * them and invoke custom message handling events. This serves to emulate an ACK/NACK for the sender, and to
         * emulate energy consumption due to network transmission overhead. The amount of energy used for this purpose
         * is to be defined by each {@link Entity} implementation.<br/>
         *
         * @param event The event that will be processed.
         */
        @Override
        public void processEvent(Event event) {
            Message message = (Message) event.getData();
            if (messagesBeingTransmitted.contains(message)) {
                if (message.getSource().isOnline() && (message.getDestination().isOnline())) {
                    message.getDestination().onMessageReceived(message);
                    message.getSource().onMessageSentAck(message);
                } else {
                    message.getSource().fail(message);
                    message.getDestination().failReception(message.getSource(), message.getId());
                }
                messagesBeingTransmitted.remove(message);
            }

        }

    }
}