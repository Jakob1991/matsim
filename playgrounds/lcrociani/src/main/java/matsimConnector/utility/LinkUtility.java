package matsimConnector.utility;

import java.util.Set;

import matsimConnector.environment.TransitionArea;
import matsimConnector.scenario.CAEnvironment;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.mobsim.qsim.qnetsimengine.QCALink;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;

import pedCA.environment.markers.TacticalDestination;

public class LinkUtility {
	public static void initLink(Link link, double length, Set<String> modes){
		link.setLength(length);
		link.setFreespeed(Constants.PEDESTRIAN_SPEED);
		
		//TODO FIX THE FLOW CAPACITY
		double width = Constants.FAKE_LINK_WIDTH;
		double cap = width*Constants.FLOPW_CAP_PER_METER_WIDTH;
		link.setCapacity(cap);
		link.setAllowedModes(modes);
	}

	public static void initLink(Link link, double length, int lanes,Set<String> modes){
		initLink(link, length, modes);
		link.setNumberOfLanes(lanes);
	}
	
	public static double getTransitionLinkWidth(Link link, CAEnvironment environmentCA){
		int destinationId = IdUtility.linkIdToDestinationId(link.getId());
		return ((TacticalDestination)environmentCA.getContext().getMarkerConfiguration().getDestination(destinationId)).getWidth();
	}
	
	public static int getTransitionAreaWidth(Link link, CAEnvironment env) {
		return (int)(getTransitionLinkWidth(link, env)/Constants.CA_CELL_SIDE);
	}
	
	public static TransitionArea getDestinationTransitionArea(QVehicle vehicle){
		Node destinationNode = vehicle.getCurrentLink().getToNode();
		for (Link link : destinationNode.getInLinks().values())
			if (link instanceof QCALink)
				return ((QCALink)link).getTransitionArea();
		
		throw new RuntimeException("QCALink not found!!!");
	}
}