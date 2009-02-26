/* *********************************************************************** *
 * project: org.matsim.*
 * LinkIdComparator.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

package org.matsim.network;

import java.io.Serializable;
import java.util.Comparator;

import org.matsim.interfaces.core.v01.Link;

/**
 * Compares two links by their Id. A simple helper class so one is
 * able to put Links in a TreeMap or other sorted data structure.
 *
 * @author mrieser
 */
public class LinkIdComparator implements Comparator<Link>, Serializable {

	private static final long serialVersionUID = 1L;

	public int compare(final Link o1, final Link o2) {
		return o1.getId().compareTo(o2.getId());
	}

}
