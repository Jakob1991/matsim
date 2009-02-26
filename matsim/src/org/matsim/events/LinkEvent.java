/* *********************************************************************** *
 * project: org.matsim.*
 * LinkEvent.java
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

package org.matsim.events;

import java.util.Map;

import org.matsim.interfaces.core.v01.Leg;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Person;

public abstract class LinkEvent extends PersonEvent {

	public static final String ATTRIBUTE_LINK = "link";
	public static final String ATTRIBUTE_LEG = "leg";

	public String linkId;
	public transient Link link;
	public Leg leg = null;
	/** The number of the leg in the plans file, starting from 0. */
	public int legId;

	LinkEvent(final double time, final Person agent, final Link link, final Leg leg) {
		super(time, agent);
		this.link = link;
		this.linkId = link.getId().toString();
		this.leg = leg;
		this.legId = leg.getNum();
	}

	LinkEvent(final double time, final String agentId, final String linkId, final int legNumber) {
		super(time, agentId);
		this.legId = legNumber;
		this.linkId = linkId;
	}

	@Override
	public Map<String, String> getAttributes() {
		Map<String, String> attr = super.getAttributes();
		attr.put(ATTRIBUTE_LINK, this.linkId);
		attr.put(ATTRIBUTE_LEG, Integer.toString(this.legId));
		return attr;
	}

	protected String asString() {
		return getTimeString(this.time) + this.agentId + "\t" + this.legId + "\t" + this.linkId + "\t0\t"; // FLAG + DESCRIPTION is missing here: concat later
	}

}
