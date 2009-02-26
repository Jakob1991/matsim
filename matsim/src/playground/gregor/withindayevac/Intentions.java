/* *********************************************************************** *
 * project: org.matsim.*
 * Intentsion.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package playground.gregor.withindayevac;

import org.matsim.interfaces.core.v01.Node;

public class Intentions {
	
	private Node destination;

	public void setDestination(final Node destination) {
		this.destination = destination;
	}

	public Node getDestination() {
		return this.destination;
	}
	
	
}
