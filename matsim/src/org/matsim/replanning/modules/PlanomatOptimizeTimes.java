/* *********************************************************************** *
 * project: org.matsim.*
 * PlanomatOptimizeTimes.java
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

package org.matsim.replanning.modules;

import org.matsim.controler.Controler;
import org.matsim.gbl.Gbl;
import org.matsim.planomat.PlanOptimizeTimes;
import org.matsim.planomat.costestimators.DepartureDelayAverageCalculator;
import org.matsim.planomat.costestimators.LegTravelTimeEstimator;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * This class is just a multithreading wrapper for instances of the
 * optimizing plan algorithm which is usually called "planomat".
 *
 * @author meisterk
 */
public class PlanomatOptimizeTimes extends MultithreadedModuleA {

	private Controler controler;
	private DepartureDelayAverageCalculator tDepDelayCalc = null;
	
	public PlanomatOptimizeTimes(Controler controler) {
		super();
		this.controler = controler;
	}
	
	@Override
	public void init() {
		if (this.tDepDelayCalc == null) {
			this.tDepDelayCalc = new DepartureDelayAverageCalculator(
					this.controler.getNetwork(), 
					this.controler.getTraveltimeBinSize());
		}
		this.controler.getEvents().addHandler(tDepDelayCalc);
		super.init();
	}

	@Override
	public PlanAlgorithm getPlanAlgoInstance() {

		LegTravelTimeEstimator legTravelTimeEstimator = Gbl.getConfig().planomat().getLegTravelTimeEstimator(
				this.controler.getTravelTimeCalculator(), 
				this.controler.getTravelCostCalculator(), 
				this.tDepDelayCalc, 
				this.controler.getNetwork());
		
		PlanAlgorithm planomatAlgorithm = null;
		planomatAlgorithm = new PlanOptimizeTimes(
				legTravelTimeEstimator, 
				this.controler.getScoringFunctionFactory());

		return planomatAlgorithm;
	}

}
