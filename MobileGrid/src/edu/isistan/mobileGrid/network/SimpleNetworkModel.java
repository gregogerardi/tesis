package edu.isistan.mobileGrid.network;

import java.util.*;

import edu.isistan.simulator.Entity;
import edu.isistan.simulator.Event;
import edu.isistan.simulator.Simulation;

/**
 * A simple network concrete implementation in which all links between {@link Entity}s are 100% reliable.
 * By default, entities added to this network are isolated from one another. Communication is rendered possible
 * only if a {@link Link} is added between them.
 */
public class SimpleNetworkModel extends NetworkModel {
	
	private Set<Node> nodes = new HashSet<Node>();
	private Map<Node,Map<Node,Link>> links = new HashMap<Node, Map<Node,Link>>();
	private Link defaultLink = new NullLink();

    /**
     * Stores the timestamp at which the different entities first sent a message during the simulation.
     */
	public final HashMap<Node, Long> firstTransferringTimes = new HashMap<Node, Long>();

    /**
     * Stores the timestamp at which the different entities last sent a message during the simulation.
     */
	public final HashMap<Node, Long> lastTransferringTimes = new HashMap<Node, Long>();

	@Override
	public <T> long send(Node scr, Node dst, int id, int length, T data, int offset, boolean lastMessage) {
		Message<T> message = new Message<>(id, scr, dst, data, length, offset, lastMessage);
		return send(message);
	}

	public <T> long send(Message message) {
		Link link = getLink(message.getSource(), message.getDestination());
		if(link.canSend(message.getSource(), message.getDestination())) {
			long currentTime = Simulation.getTime();
			long estimatedMessageReceivedTime = currentTime + link.getTransmissionTime((int) message.getMessageSize());

			// Updates first and last message sent timestamps. For post-simulation validation purposes only.
			if(!firstTransferringTimes.containsKey(message.getDestination())) {
				firstTransferringTimes.put(message.getDestination(), currentTime);
			}
			lastTransferringTimes.put(message.getSource(), estimatedMessageReceivedTime);

			// Notifies sender that it will start sending data.
			message.getSource().startTransfer(message.getDestination(),message.getId(), message.getData());

			// Notifies receiver that it will start receiving data.
			message.getDestination().incomingData(message.getSource(), message.getId());
			//save the message being sended in case that the destination device disconnects and we need to fail the message
			addMessageBeingTransmited(message);
			Simulation.addEvent(Event.createEvent(Event.NO_SOURCE, estimatedMessageReceivedTime,
					this.getNetworkDelayEntityId(), 0, message));
			return estimatedMessageReceivedTime;
		} else {
			message.getSource().fail(message);
		}
		return 0;
	}

	private Link getLink(Node scr, Node dst) {
		Link result = this.defaultLink;
		Map<Node, Link> map = this.links.get(scr);
		if(map != null && map.containsKey(dst)) {
			result = map.get(dst);
		}
		return result;
	}

	@Override
	public void addNewNode(Node n) {
		this.nodes.add(n);
	}
	
	@Override
	public void addNewLink(Link link) {
		for(Node scr: link.getSources()) {
			Map<Node,Link> map = this.links.get(scr);
			if(map == null) {
				map = new HashMap<Node, Link>();
				this.links.put(scr, map);
			}
			for(Node dst: link.getDestinations())
				map.put(dst, link);
		}
	}

	@Override
	public void removeNode(Node n) {
		this.nodes.remove(n);
		this.links.remove(n);
		for(Node key:this.links.keySet()){
			this.links.get(key).remove(n);
			if(this.links.get(key).isEmpty())this.links.remove(key);
		}
	}

	@Override
	public void removeLink(Link link) {
		for(Node scr: link.getSources()){
			Map<Node,Link> map = this.links.get(scr);
			if (map != null){
				for (Node dst: link.getDestinations()) {
					map.remove(dst);
				}
				if (map.isEmpty()) {
					this.links.remove(scr);
				}
			}
		}
	}

	@Override
	public Set<Node> getNodes() {
		return this.nodes;
	}

	public Link getDefaultLink() {
		return defaultLink;
	}

	public void setDefaultLink(Link defaultLink) {
		this.defaultLink = defaultLink;
	}

	@Override
	public long getTransmissionTime(Node scr, Node dst,int messageSize) {
		Link link = getLink(scr, dst);
		return link.getTransmissionTime(messageSize);
	}

}
