/*
 * *********************************************************************** *
 * project: org.matsim.*                                                   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2014 by the members listed in the COPYING,        *
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
 * *********************************************************************** *
 */

package contrib.publicTransitMapping.mapping;

/**
 * Allows to run an implementation
 * of public transit mapping via config file path.
 *
 * Currently redirects to the only implementation
 * {@link PTMapperImpl}.
 *
 * @author polettif
 */
public class RunPublicTransitMapper {

	/**
	 * Routes the unmapped MATSim Transit Schedule to the network using the file
	 * paths specified in the config. Writes the resulting schedule and network to xml files.<p/>
	 *
	 * @see contrib.publicTransitMapping.config.CreateDefaultConfig
	 *
	 * @param args <br/>[0] PublicTransitMapping config file<br/>
	 */
	public static void main(String[] args) {
		if(args.length == 1) {
			run(args[0]);
		} else {
			throw new IllegalArgumentException("Incorrect number of arguments: [0] Public Transit Mapping config file");
		}
	}

	/**
	 * Routes the unmapped MATSim Transit Schedule to the network using the file
	 * paths specified in the config. Writes the resulting schedule and network to xml files.<p/>
	 *
	 * @see contrib.publicTransitMapping.config.CreateDefaultConfig
	 *
	 * @param configFile the PublicTransitMapping config file
	 */
	public static void run(String configFile) {
		new PTMapperImpl(configFile).run();
	}

}
