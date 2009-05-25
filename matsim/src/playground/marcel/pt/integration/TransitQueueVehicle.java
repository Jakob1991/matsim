/* *********************************************************************** *
 * project: org.matsim.*
 * VehicleImpl.java
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

package playground.marcel.pt.integration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.matsim.core.basic.v01.vehicles.BasicVehicle;
import org.matsim.core.basic.v01.vehicles.BasicVehicleCapacity;
import org.matsim.core.mobsim.queuesim.QueueVehicleImpl;

import playground.marcel.pt.interfaces.PassengerAgent;
import playground.marcel.pt.interfaces.TransitVehicle;

public class TransitQueueVehicle extends QueueVehicleImpl implements TransitVehicle {

	private final int passengerCapacity;
	private final List<PassengerAgent> passengers = new LinkedList<PassengerAgent>();

	public TransitQueueVehicle(final BasicVehicle basicVehicle, final double sizeInEquivalents) {
		super(basicVehicle, sizeInEquivalents);
		BasicVehicleCapacity capacity = basicVehicle.getType().getCapacity();
		this.passengerCapacity = capacity.getSeats().intValue() + capacity.getStandingRoom().intValue() - 1; // the driver also takes on seat
	}

	public boolean addPassenger(final PassengerAgent passenger) {
		if (this.passengers.size() < this.passengerCapacity) {
			return this.passengers.add(passenger);
		}
		return false;
	}

	public int getPassengerCapacity() {
		return this.passengerCapacity;
	}

	public Collection<PassengerAgent> getPassengers() {
		return Collections.unmodifiableList(this.passengers);
	}

	public boolean removePassenger(final PassengerAgent passenger) {
		boolean removed = this.passengers.remove(passenger);
		if (removed) {
		}
		return removed;
	}

}
