package causalOptimization.variable;

import java.util.ArrayList;
import java.util.Collections;

import causalOptimization.Main;

//import org.apache.commons.lang3.RandomUtils;
//import java.util.Random;

import jmetal.core.Variable;
import util.SolutionUtils;

public class Sources extends Variable {
	
	public ArrayList<String> _sourcesList; // Both To Be Made "Private"
	public ArrayList<Double> _doubleValueList;  // double representation of the solution. Size = MaxSources, padded with '0' values
	public ArrayList<String> _sourcesFullList;  // Used by PSO 
	
	
	public Sources(ArrayList<String> sourcesList) {
		super();
		this._sourcesList = new ArrayList<String>();
		for (int i=0; i<sourcesList.size();i++) _sourcesList.add(sourcesList.get(i));
	}

	//COSTRUTTORE DI COPIA
	public Sources(Sources sources) throws Exception {
		this._sourcesList = new ArrayList<String>();
		this._doubleValueList = new ArrayList<Double>(COPConfigurator.MAX_sources); //will put '0' for the Max_source - n_sources empty values
		this._sourcesFullList = new ArrayList<String>(COPConfigurator.MAX_sources); //will put '0' for the Max_source - n_sources empty values
	
		for (int i=0; i<sources._sourcesList.size();i++) {
			this._sourcesList.add(sources._sourcesList.get(i));
			this._doubleValueList.add(SolutionUtils.getDoubleRepresentation(sources._sourcesList.get(i)));
			this._sourcesFullList.add(sources._sourcesList.get(i)); 
		}
		for(int i=0; i<COPConfigurator.MAX_sources - this._sourcesList.size();i++) { 
			this._doubleValueList.add(0.0); // Pad with '0'
			this._sourcesFullList.add("0");
		}
		
		}

	
	
	public Sources(int n_sources) throws Exception {
		//n_sources = Math.max(n_sources , Main.MAX_sources);
		this._sourcesList = new ArrayList<String>(n_sources);
		this._doubleValueList = new ArrayList<Double>(COPConfigurator.MAX_sources); //will put '0' for the Max_source - n_sources empty values
		this._sourcesFullList = new ArrayList<String>(COPConfigurator.MAX_sources); //will put '0' for the Max_source - n_sources empty values
		
		// Sources taken from knowledge base
		if (COPConfigurator.solutionTypeFlag.equals("factual")) {
			//ArrayList<Integer> n_values_index = new ArrayList<Integer>(Main.possibleKBsources.size());
			ArrayList<Integer> n_values_index = new ArrayList<Integer>(COPConfigurator.possibleKBsourcesPerType.size());
		    for (int ind = 0; ind < COPConfigurator.possibleKBsourcesPerType.size(); ind++) 
		    	n_values_index.add(ind);
		    Collections.shuffle(n_values_index);
		    for (int k = 0; k< n_sources; k++) {
		    	String soruceToAdd = (COPConfigurator.possibleKBsourcesPerType.get(n_values_index.get(k))).get(COPConfigurator.ran.nextInt(COPConfigurator.possibleKBsourcesPerType.get(n_values_index.get(k)).size()));
		    	//		System.out.println("source in constr "+soruceToAdd);
		    	this._sourcesList.add(soruceToAdd);
		    	this._doubleValueList.add(SolutionUtils.getDoubleRepresentation(soruceToAdd));
		    	this._sourcesFullList.add(soruceToAdd);
		    }
			}
		// Sources taken from ontology
	
		// two-level selection is needed: first select the source type and then the specific source (so as the probability of selection is not influenced by how many elements per source are present
		if (COPConfigurator.solutionTypeFlag.equals("analogical") || COPConfigurator.solutionTypeFlag.equals("creative")) {
			ArrayList<Integer> n_values_index_external = new ArrayList<Integer>(COPConfigurator.possibleSourcePerType.size());
			for (int ind = 0; ind < COPConfigurator.possibleSourcePerType.size(); ind++) 
				n_values_index_external.add(ind);
		    Collections.shuffle(n_values_index_external);
		    for (int k = 0; k< n_sources; k++) {
		    	String sourceToAdd = (COPConfigurator.possibleSourcePerType.get(n_values_index_external.get(k))).get(COPConfigurator.ran.nextInt(COPConfigurator.possibleSourcePerType.get(n_values_index_external.get(k)).size()));
		    	this._sourcesList.add(sourceToAdd);
		    	this._doubleValueList.add(SolutionUtils.getDoubleRepresentation(sourceToAdd));
		    	this._sourcesFullList.add(sourceToAdd);
		    }
		}
			/*
			ArrayList<Integer> n_values_index = new ArrayList<Integer>(Main.possibleSources.size());//from ontology
		    for (int ind = 0; ind < Main.possibleSources.size(); ind++) 
		    	n_values_index.add(ind);
		    Collections.shuffle(n_values_index);
		    for (int k = 0; k< n_sources; k++)
		    	this._sourcesList.add(Main.possibleSources.get(n_values_index.get(k)));
			}
		*/
	    
		// two-level selection is needed: first select the source type and then the specific source (so as the probability of selection is not influenced by how many elements per source are present
		if (COPConfigurator.solutionTypeFlag.equals("graph")) {
			double r;
			double cum=0;
			int index,element=0;
			for (int k = 0; k< n_sources; k++) {
				r = COPConfigurator.ran.nextDouble();
				cum=0; 
				index=0;
				for(index=0; index< COPConfigurator.graph_weights.length; index++){
					cum = cum + COPConfigurator.graph_weights[index];
					if(r<=cum)
						break;
				}
				
				element=0;
				cum=0;
				r = COPConfigurator.ran.nextDouble();
				for (element=0; element<COPConfigurator.possibleSourcePerType.get(index).size(); element++) {
					cum = cum + COPConfigurator.distributions[index].get(element);
					if(r<=cum)
						break;
				}
				
				double sum=0;
				for (int i=0; i<COPConfigurator.graph_weights.length; i++) {
					//System.out.println(" weight vector "+Main.graph_weights[i]);
					sum += COPConfigurator.graph_weights[i];
				}
				
				String sourceToAdd = (COPConfigurator.possibleSourcePerType.get(index)).get(element);
				System.out.println(" Main.possibleSourcePerType idnex  "+COPConfigurator.possibleSourcePerType.get(index));
				
				System.out.println("source to add: "+sourceToAdd);
				if(_sourcesList.contains(sourceToAdd)) {
					System.out.println("duplicate");
					k--;
				}else {
				   	this._sourcesList.add(sourceToAdd);
				   	this._doubleValueList.add(SolutionUtils.getDoubleRepresentation(sourceToAdd));
				   	this._sourcesFullList.add(sourceToAdd);
				}
			    }
		}
		
		if (!COPConfigurator.solutionTypeFlag.equals("factual") && !COPConfigurator.solutionTypeFlag.equals("analogical") && !COPConfigurator.solutionTypeFlag.equals("creative")&& !COPConfigurator.solutionTypeFlag.equals("graph")) 
			throw new Exception("Creation of random solution: solution type is not correct");
		
		for(int i=0; i<COPConfigurator.MAX_sources - this._sourcesList.size();i++) { 
			this._doubleValueList.add(0.0); // Pad with '0'
			this._sourcesFullList.add("0");
		}
	}

		
	public ArrayList<String> get_sourcesList() {
		return _sourcesList;
	}

	public void set_sourcesList(ArrayList<String> _sourcesList) {
		this._sourcesList = _sourcesList;
	}

	@Override
	public Variable deepCopy() {
		try {
			return new Sources(this);
		} catch (Exception e) {e.printStackTrace();}
		return null;
	}

	public void updateSource() throws Exception {
		_sourcesList.clear();
		ArrayList<Integer> zeroIndexes = new ArrayList<Integer>();
		//int duplicates=0;
		for (int i= 0; i< this._sourcesFullList.size(); i++) {
			if(!this._sourcesFullList.get(i).equals("0")) {
				if (!this._sourcesList.contains(this._sourcesFullList.get(i))) {
					_sourcesList.add(this._sourcesFullList.get(i));
				}
				else {
					//duplicates++;
					zeroIndexes .add(i);  // Pad with '0'
				}
			}
		else {
			// remember the 0 elements, can be useful later
			zeroIndexes .add(i); 
			}
		}
		
		int minSize = _sourcesList.size() - COPConfigurator.MIN_sources;  
		if (minSize<0) {
			ArrayList<Integer> n_values_index = new ArrayList<Integer>(COPConfigurator.possibleSources.size());
			for (int ind = 0; ind < COPConfigurator.possibleSources.size(); ind++) 
		    	n_values_index.add(ind);
		    Collections.shuffle(n_values_index);
			for (int k = 0; k < Math.abs(minSize); k++) {
		    	this._sourcesList.add(COPConfigurator.possibleSources.get(n_values_index.get(k)));
		    	this._doubleValueList.set(zeroIndexes .get(k), SolutionUtils.getDoubleRepresentation(COPConfigurator.possibleSources.get(n_values_index.get(k))));
		    	this._sourcesFullList.set(zeroIndexes .get(k), COPConfigurator.possibleSources.get(n_values_index.get(k)));
		    }
		    	
		}
		
	}
}
