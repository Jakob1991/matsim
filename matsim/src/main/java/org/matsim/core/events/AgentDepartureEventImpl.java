/* *********************************************************************** *
 * project: org.matsim.*
 * AgentDepartureEvent.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007, 2008 by the members listed in the COPYING,  *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.core.events;

import org.matsim.api.core.v01.Id;
import org.matsim.core.api.experimental.events.AgentDepartureEvent;

public class AgentDepartureEventImpl extends AgentEventImpl implements AgentDepartureEvent {

	public static final String EVENT_TYPE = "departure";
	
	private Id destinationLinkId = null ;
	private Double travelTime = null ;
	private Boolean isTeleported = null ;

	public AgentDepartureEventImpl(final double time, final Id agentId, final Id linkId, final String legMode) {
		super(time, agentId, linkId, legMode);
	}

	public AgentDepartureEventImpl(double now, Id agentId, Id linkId, String mode, boolean isTeleported, Id destinationLinkId, double travelTime) {
		super(now, agentId, linkId, mode) ;
		this.destinationLinkId = destinationLinkId ;
		this.travelTime = travelTime ;
		this.isTeleported = isTeleported ;
	}

	@Override
	public String getEventType() {
		return EVENT_TYPE;
	}

	public Id getDestinationLinkId() {
		return destinationLinkId;
	}

	public Double getTravelTime() {
		return travelTime;
	}
	
	public Boolean isTeleported() {
		return this.isTeleported ;
	}

}
