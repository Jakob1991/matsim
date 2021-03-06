package playground.kai.usecases.opdytsintegration.modechoice;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ModeParams;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup.ScoringParameterSet;

import floetteroed.opdyts.DecisionVariable;

/**
 * 
 * @author Kai Nagel based on Gunnar Flötteröd
 *
 */
final class ModeChoiceDecisionVariable implements DecisionVariable {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ModeChoiceDecisionVariable.class ) ;

	private final Scenario scenario;

	private final PlanCalcScoreConfigGroup newScoreConfig;

	ModeChoiceDecisionVariable(PlanCalcScoreConfigGroup newScoreConfig, Scenario scenario) {
		this.newScoreConfig = newScoreConfig;
		this.scenario = scenario;
	}

	PlanCalcScoreConfigGroup getScoreConfig() {
		return newScoreConfig ;
	}

	@Override public void implementInSimulation() {
		for ( Entry<String, ScoringParameterSet> entry : newScoreConfig.getScoringParametersPerSubpopulation().entrySet() ) {
			String subPopName = entry.getKey() ;
//			log.warn( "treating sub-population with name=" + subPopName );
			ScoringParameterSet newParameterSet = entry.getValue() ;
			for ( ModeParams newModeParams : newParameterSet.getModes().values() ) {
//				String mode = newModeParams.getMode() ;
//				log.warn( "treating mode with name=" + mode ) ;
				scenario.getConfig().planCalcScore().getScoringParameters( subPopName ).addModeParams( newModeParams ) ;
//				log.warn("new mode params:" + scenario.getConfig().planCalcScore().getScoringParameters( subPopName ).getModes().get(mode) );
			}
		}
	}

	@Override public String toString() {
		final Map<String, ModeParams> allModes = scenario.getConfig().planCalcScore().getScoringParameters(null).getModes();

		StringBuilder strb = new StringBuilder() ;
		for ( ModeParams modeParams : allModes.values() ) {
			final String mode = modeParams.getMode();
			if ( TransportMode.car.equals(mode) || TransportMode.pt.equals(mode) ) {
				strb.append( mode + ": " + modeParams.getConstant() + " + " + modeParams.getMarginalUtilityOfTraveling() + " * ttime ; " ) ;
			}
		}

		return strb.toString() ;
	}

}
