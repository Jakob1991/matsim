/* *********************************************************************** *
 * project: org.matsim.*
 * Id.java
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

package org.matsim.basic.v01;

import org.matsim.utils.identifiers.IdI;

public class Id implements IdI {

	private final String id;

	public Id(final String id) {
		this.id = id;
	}

	public Id(final int id) {
		this.id = Integer.toString(id);
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) return false;
		if (!(other instanceof Id)) return false;
		if (other == this) return true;
		return this.id.equals(((Id)other).id);
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	public int compareTo(final Id o) {
		return this.id.compareTo(o.id);
	}

	public int compareTo(final IdI id) {
		return this.id.compareTo(id.toString());
	}

	@Override
	public String toString() {
		return this.id;
	}

}
