/* *********************************************************************** *
 * project: org.matsim.*
 * DistanceCalculator.java
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

package playground.gregor.demandmodeling;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.gbl.Gbl;
import org.matsim.interfaces.core.v01.CarRoute;
import org.matsim.interfaces.core.v01.Node;
import org.matsim.network.MatsimNetworkReader;
import org.matsim.network.NetworkLayer;
import org.matsim.population.routes.NodeCarRoute;
import org.matsim.router.Dijkstra;
import org.matsim.router.costcalculators.FreespeedTravelTimeCost;
import org.matsim.router.util.LeastCostPathCalculator.Path;
import org.matsim.utils.geometry.CoordImpl;

public class DistanceCalculator {
	private final static Logger log = Logger.getLogger(DistanceCalculator.class);
	private final NetworkLayer network;
	private final String csvfile;
	private final List<String []> entries = new ArrayList<String []>();
	private final HashMap<String,Household> housholds = new HashMap<String,Household>();
	private final String outfile;
	
	public DistanceCalculator(NetworkLayer network, String csvfile, String outfile) {
		this.network = network;
		this.csvfile = csvfile;
		this.outfile = outfile;
	}
	
	
	public void run(){
		readCSVFile();
		localizeHousholds();
		routeHomeToActivities();
		
		
	}


	private void routeHomeToActivities() {
		this.network.connect();
		Dijkstra router = new Dijkstra(this.network, new FreespeedTravelTimeCost(), new FreespeedTravelTimeCost());
		CSVFileWriter writer = new CSVFileWriter(this.outfile);
		writer.writeLine(new String [] {"Excel_ID","ID","DISTANCE"});
		for (String [] entry : this.entries) {
			if (entry == null) {
				continue;
			}
			String excId = entry[0];
			String id = entry[1];
						
			double xcoord = Double.parseDouble(entry[7]);
			double ycoord = Double.parseDouble(entry[8]);
			CoordImpl actCoord = new CoordImpl(xcoord, ycoord);
						
			Household hh = this.housholds.get(id);
			if (hh == null) {
				writer.writeLine(new String [] {excId,id,"NO HOME LOCATION"});
				continue;
			}
			
			
			
			Node home = this.network.getNearestNode(hh.home);
			Node act = this.network.getNearestNode(actCoord);
			Path path = router.calcLeastCostPath(home, act, 0);
			CarRoute route = new NodeCarRoute();
			route.setNodes(path.nodes);
			double dist = route.getDist();
			writer.writeLine(new String [] {excId,id,Double.toString(dist)});
			
			
		}	
		
		writer.finish();
	}


	private void localizeHousholds() {
		for (String [] entry : this.entries) {
			if (entry == null) {
				continue;
			}
			String id = entry[1];
			String location = entry[9];
			if (!location.equals("HOME")) {
				continue;
			}
			if (this.housholds.get(id) != null) {
				continue;
			}
						
			double xcoord = Double.parseDouble(entry[7]);
			double ycoord = Double.parseDouble(entry[8]);
			
			Household hh = new Household();
			hh.id = id;
			CoordImpl c = new CoordImpl(xcoord,ycoord);
			hh.home = c;
			this.housholds.put(id, hh);
		}
		
	}


	private void readCSVFile() {
		CSVFileReader reader = new CSVFileReader(this.csvfile);
		reader.readLine(); //first line is the head - so skip it!
		String [] line = reader.readLine();
		
		while (line != null){
			entries.add(line);
			line = reader.readLine();
//			this.entries.add(line);
		}
		
		
	}

	
	private static class Household {
		String id;
		CoordImpl home;
	}
	

	public static void main(String [] args) {
		
		String infile = "./padang/demand_generation/working-day_coord_1activ.csv";
		String outfile = "./padang/demand_generation/routed.csv";
		String netfile = "./networks/padang_net_v20080618.xml";
		
		
		Gbl.createConfig(null);
		
		log.info("loading network layer...");
		final NetworkLayer network = new NetworkLayer();
		new MatsimNetworkReader(network).readFile(netfile);
		log.info("done.");
		
		new DistanceCalculator(network,infile,outfile).run();
		
	}

}
