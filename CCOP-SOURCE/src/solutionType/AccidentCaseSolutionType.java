package solutionType;

import hazardAnalysis.*;
import hazardAnalysis.domain.AccidentCase.SOURCE;
import hazardAnalysis.variable.Anomalies;
import hazardAnalysis.variable.Causes;
import hazardAnalysis.variable.Events;
import hazardAnalysis.variable.Results;

import java.util.ArrayList;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import jmetal.encodings.variable.ArrayReal;
import jmetal.encodings.variable.Binary;
import jmetal.util.PseudoRandom;

public class AccidentCaseSolutionType extends SolutionType {

	public Events  _events; 
	public Causes _causes; 
	public Anomalies _anomalies;
	public Results _results;
	public enum SOURCE {REAL_DOMAIN, SIMILAR_DOMAIN };
	public SOURCE _source;
	
	public int numberOfVariables;    //somma delle 4 di sotto? oppure =4?
	private int numberOfCauses; 
	private int numberOfEvents;
	private int numberOfAnomalies;
	private int numberOfResults;
	private String OPERATOR; //operator that generated the solution
	
	public AccidentCaseSolutionType(Problem problem){
		super(problem);
	}
	
	public AccidentCaseSolutionType(Problem problem, int n_events, int n_causes,int  n_anomalies,int n_results){
		super(problem); 
		numberOfEvents = n_events;
		numberOfCauses = n_causes;
		numberOfAnomalies = n_anomalies;
		numberOfResults = n_results;
	}
	
	/*public void addEvent(Event event){ 
		_events.add(event);
	}
	
	public void removeEvent(Event event){
		_events.remove(event);
	}
	
	public void replaceEvent(Event oldEvent, Event newEvent) throws Exception{
		int ind =_events.indexOf(oldEvent); 
		if(ind ==-1) 
			throw new Exception("The Event to modify does not exist in the current list");
		_events.remove(ind);
		_events.add(newEvent); 
	}
*/
	public String getOperator(){
		return OPERATOR;
	}
	public void setOperator(String _operator){
		OPERATOR = _operator;
	}
	
	@Override
	public Variable[] createVariables() throws ClassNotFoundException { 
		Variable [] variables = new Variable[4];

	    try {
			variables[0] = new Events(PseudoRandom.randInt(hazardAnalysis.Main.MIN_events, hazardAnalysis.Main.MAX_events));
		} catch (Exception e) {e.printStackTrace();}
	    variables[1] = new Anomalies(PseudoRandom.randInt(hazardAnalysis.Main.MIN_anomalies, hazardAnalysis.Main.MAX_anomalies));
	    variables[2] = new Results(PseudoRandom.randInt(hazardAnalysis.Main.MIN_results, hazardAnalysis.Main.MAX_results));
	    variables[3] = new Causes(PseudoRandom.randInt(hazardAnalysis.Main.MIN_causes, hazardAnalysis.Main.MAX_causes));
	    return variables ;
	}
		
}
