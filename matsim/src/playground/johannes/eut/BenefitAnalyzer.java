/* *********************************************************************** *
 * project: org.matsim.*
 * BenefitAnalyzer.java
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

/**
 * 
 */
package playground.johannes.eut;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.matsim.controler.Controler;
import org.matsim.controler.events.IterationEndsEvent;
import org.matsim.controler.events.IterationStartsEvent;
import org.matsim.controler.events.ShutdownEvent;
import org.matsim.controler.events.StartupEvent;
import org.matsim.controler.listener.IterationEndsListener;
import org.matsim.controler.listener.IterationStartsListener;
import org.matsim.controler.listener.ShutdownListener;
import org.matsim.controler.listener.StartupListener;
import org.matsim.interfaces.core.v01.CarRoute;
import org.matsim.interfaces.core.v01.Leg;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Person;
import org.matsim.interfaces.core.v01.Plan;
import org.matsim.router.util.TravelTime;
import org.matsim.utils.io.IOUtils;

/**
 * @author illenberger
 *
 */
public class BenefitAnalyzer implements IterationEndsListener, ShutdownListener, IterationStartsListener, StartupListener {

	private TripAndScoreStats tripStats;
	
//	private EUTRouterAnalyzer routerAnalyzer;
	
//	private TravelTimeMemory ttKnowledge;
	private TwoStateTTKnowledge ttKnowledge;
	
	private ArrowPrattRiskAversionI utilFunc;
	
	private Map<Person, Double> ceMap;
	
	private Map<Person, Double> expTTMap;
	
	private List<Double> samplesCE;
	
	private List<Double> samplesExpTT;
	
	private BufferedWriter writer;
	
	private SummaryWriter summaryWriter;
	

	public BenefitAnalyzer(TripAndScoreStats tripStats, EUTRouterAnalyzer routerAnalyzer, TwoStateTTKnowledge ttKnowledge, ArrowPrattRiskAversionI utilFunc, SummaryWriter summaryWriter) {
		this.tripStats = tripStats;
//		this.routerAnalyzer = routerAnalyzer;
		this.ttKnowledge = ttKnowledge;
		this.utilFunc = utilFunc;
		this.summaryWriter = summaryWriter;
	}
	/*
	 * This is ugly, but there is no event between initialization and actual run
	 * of the queue sim.
	 */
	public void addGuidedPerson(Person p) {
		
			Plan plan = p.getSelectedPlan();
			double cesum = 0;
			double expTTSum = 0;
			int tripcounts = 0;
			for(Iterator it = plan.getIteratorLeg(); it.hasNext();) {
				tripcounts++;
				Leg leg = ((Leg)it.next());
				CarRoute route = (CarRoute) leg.getRoute();
				double totaltravelcosts = 0;
				double totaltraveltime = 0;
				
//				for (TravelTime traveltimes : ttKnowledge.getTravelTimes()) {
//					double traveltime = calcTravTime(traveltimes, route, leg.getDepTime());
//					totaltraveltime += traveltime;
//					double travelcosts = utilFunc.evaluate(traveltime);
//					totaltravelcosts += travelcosts;
//				}
				for (int i = 0; i < ttKnowledge.getTravelTimes().size(); i++) {
					double traveltime = calcTravTime(ttKnowledge.getTravelTimes(i), route, leg.getDepartureTime());
					double travelcosts = utilFunc.evaluate(traveltime);
					totaltravelcosts += travelcosts * ttKnowledge.getWeigth(i);
					totaltraveltime += traveltime * ttKnowledge.getWeigth(i);
				}
				double avrcosts =totaltravelcosts;
//				double avrcosts = totaltravelcosts
//						/ (double) ttKnowledge.getTravelTimes().size();
//				double avttime = totaltraveltime/ (double) ttKnowledge.getTravelTimes().size();
				double avttime = totaltraveltime;
				
				cesum += utilFunc.getTravelTime(avrcosts);
				expTTSum += avttime;
			}
			
			double ceavr = cesum/(double)tripcounts;
			ceMap.put(p, ceavr);
			
			expTTMap.put(p, expTTSum/(double)tripcounts);
	}
	
	public void notifyIterationEnds(IterationEndsEvent event) {
		/*
		 * Get the experienced trip duration from the tripstats
		 */
		double benefitsumExpTT = 0;
		double benefitsumCE = 0;
		for(Person p : ceMap.keySet()) {
			Double triptime = tripStats.getTripDurations().get(p);
			if (triptime != null) { // unfortunately this can happen -> withinday bug
				double ce = ceMap.get(p);
				double expTT = expTTMap.get(p);
				double benefitCE = ce - triptime + 72; //TODO f***ing node based routes!!!
				double benefitExpTT = ce - expTT;
				benefitsumCE += benefitCE;
				benefitsumExpTT += benefitExpTT;
				samplesCE.add(benefitCE);
				samplesExpTT.add(benefitExpTT);
			}
		}

		try {
			writer.write(String.valueOf(event.getIteration()));
			writer.write("\t");
			writer.write(String.valueOf(benefitsumCE/(double)ceMap.size()));
			writer.write("\t");
			writer.write(String.valueOf(benefitsumExpTT/(double)expTTMap.size()));
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyShutdown(ShutdownEvent event) {
		try {
			writer.write("avr\t");
			double sumCE = 0;
			double sumExpTT = 0;
			for(Double d : samplesCE)
				sumCE += d;
			for(Double d : samplesExpTT)
				sumExpTT += d;
			
			double avrCE = sumCE/(double)samplesCE.size();
			summaryWriter.setTt_benefitPerIter(avrCE);
			writer.write(String.valueOf(avrCE));
			writer.write("\t");
			writer.write(String.valueOf(sumExpTT/(double)samplesExpTT.size()));
			writer.newLine();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private double calcTravTime(TravelTime traveltimes, CarRoute route,
			double starttime) {
		double totaltt = 0;
		for (Link link : route.getLinks()) {
			totaltt += traveltimes.getLinkTravelTime(link, starttime + totaltt);
		}
		return totaltt;
	}

	public void notifyIterationStarts(IterationStartsEvent event) {
		ceMap = new HashMap<Person, Double>();
		expTTMap = new HashMap<Person, Double>();
	}

	public void notifyStartup(StartupEvent event) {
		try {
			writer = IOUtils.getBufferedWriter(Controler.getOutputFilename("benefits.txt"));
			writer.write("Iteration\tbenefitsCE\tbenefitsExpTT");
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		samplesCE = new LinkedList<Double>();
		samplesExpTT = new LinkedList<Double>();
	}

}
