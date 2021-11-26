package causalOptimization;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import util.ElementDistanceBinaryImpl;
import util.SolutionUtils;
import util.VectorDistanceJaccardImpl;

public class SimilarityEvalutaor {
	
	ElementDistanceBinaryImpl elemDist = new ElementDistanceBinaryImpl();  
	VectorDistanceJaccardImpl vecDist = new VectorDistanceJaccardImpl();
	
	public ArrayList<Double> computeKBSimilarity(Solution sol) throws Exception{
		
		ArrayList<String> sol_string = SolutionUtils.getNumericRepresentation(sol); 
		//System.out.println("\nDEBUG in KB similarity");
		//ArrayList<Integer> vector1 = new ArrayList<Integer>(Main.numericTransactionsDB.size());
		//ArrayList<Integer> vector2 = new ArrayList<Integer>(Main.numericTransactionsDB.size()); 
		ArrayList<Double> similarities = new ArrayList<Double>(); 
		//System.out.println("Solution strng "+sol_string);
		for (int i=0; i<COPConfigurator.listOfInternalSolutions.size();i++){ //KB solutions
			//vector1.clear();
			//vector2.clear();
			ArrayList<String> KB_sol_string = new ArrayList<String>();
			KB_sol_string = SolutionUtils.getNumericRepresentation(COPConfigurator.listOfInternalSolutions.get(i)); 
			//System.out.println("Main.listOfInternalSolutions.get(i) "+KB_sol_string);
			
			/*for (int j = 0; j<Main.numericTransactionsDB.size();j++) {
				//https://datascience.stackexchange.com/questions/15862/how-to-compute-the-jaccard-similarity-in-this-example-jaccard-vs-cosine 
				if (sol_string.contains(String.valueOf(j+1)))
					vector1.add(1);
				else
					vector1.add(0);
				
				if (KB_sol_string.contains(String.valueOf(j+1)))
					vector2.add(1);
				else
					vector2.add(0);
			}
			//for (int k=0; k<KB_sol_string.size();k++) if(sol_string.contains(KB_sol_string.get(k))) count++; CONTEGGIO ELEMENTI COMUNI, corrisponde a count11 di Jaccard
			//for (int k=0; k<vector1.size();k++) System.out.println("Element "+k+": "+vector1.get(k));
			//System.out.println("count "+count);
			Double index = vecDist.calculateDistance(vector1, vector2);
			*/
			
			
			//if (index.isNaN())
			//	throw new Exception("Jaccard distance returns NaN"); 
			
			// SIM: the greater, the more similar the vectors are
			//similarities.add((1 - index));
			
			
			similarities.add((((double)(intersection(sol_string,KB_sol_string)).size())/(union(sol_string,KB_sol_string)).size()));
			//similarities.add((((double)(intersection(sol_string,KB_sol_string)).size())/((sol_string.size()*2) - (intersection(sol_string,KB_sol_string)).size())));

			//System.out.println("jaccard subset "+similarities.get(i));//rescaled
			//System.out.println("jaccard classic "+(((double)(intersection(sol_string,KB_sol_string)).size())/(union(sol_string,KB_sol_string)).size()));
			
		//System.exit(0);
		/*	System.out.println("solution  "+sol_string);
			System.out.println("kb solution  "+KB_sol_string);
			System.out.println("Intersection "+intersection(sol_string,KB_sol_string));
			System.out.println("union "+union(sol_string,KB_sol_string));
			System.out.println("length Intersection "+intersection(sol_string,KB_sol_string).size());
			System.out.println("lenth union "+union(sol_string,KB_sol_string).size());*/
			//alternatives
			/*System.out.println("Divided by kb sol size "+(((double)(intersection(sol_string,KB_sol_string)).size())/(KB_sol_string.size())));
			System.out.println("Divided by sol size "+(((double)(intersection(sol_string,KB_sol_string)).size())/(sol_string.size())));
			
			//Jaccard with respect to the subset of sol_string variables
			//System.out.println("sol size "+sol_string.size()); 
			System.out.println("Jaccard in subset "+(((double)(intersection(sol_string,KB_sol_string)).size())/(sol_string.size()*2)));
			*/
		} 
		return similarities;
	}
	
	
	
	public void computeReferenceSetDistance(SolutionSet sol_set, SolutionSet reference_set, String filename, int runs, boolean refSet) throws Exception{
		
		
		
		ArrayList<Double> minimum_distances= new ArrayList<Double>();
		ArrayList<Double> distances= new ArrayList<Double>();
		ArrayList<Double> minimum_distances_2= new ArrayList<Double>();
		ArrayList<Double> distances_2= new ArrayList<Double>();
		
		if(runs==0) {
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_abs.txt"));
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_rel.txt"));
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_abs_RS_statistics.txt"));
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_rel_RS_statistics.txt"));
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_abs_KB_statistics.txt"));
			Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/"+filename+"_rel_KB_statistics.txt"));
		}
		
		FileWriter closestItem_RefSet = new FileWriter(COPConfigurator.baseDir+"/"+filename+"_abs.txt", true);
		FileWriter closestItem_RefSet_2 = new FileWriter(COPConfigurator.baseDir+"/"+filename+"_rel.txt", true);
		
		closestItem_RefSet.write("Run: "+runs+ "\n");
		closestItem_RefSet_2.write("Run: "+runs+ "\n");

		for (int index=0; index<sol_set.size();index++) {
			ArrayList<String> sol_string = SolutionUtils.getNumericRepresentation(sol_set.get(index));
			for (int i=0; i<reference_set.size();i++){ //reference set solutions
				ArrayList<String> rs_sol_string = new ArrayList<String>();
				rs_sol_string = SolutionUtils.getNumericRepresentation(reference_set.get(i));
				double distance = 1-((double)(intersection(sol_string,rs_sol_string)).size())/(union(sol_string,rs_sol_string).size());
				double distance_subset = 1-((double)(intersection(sol_string,rs_sol_string)).size())/(((Math.min(sol_string.size(),rs_sol_string.size())*2) - (intersection(sol_string,rs_sol_string)).size()));
				distances.add(distance);
				distances_2.add(distance_subset);
				//System.out.println("Distance "+distance);
				//System.out.println("Distance subset "+distance_subset);
				//distances.add(distance_subset);
			}
		//	System.out.println("\n Solution "+(index+1)+" out of "+sol_set.size()+": "+sol_string);

			//System.out.println("Number of cmputed distances "+distances.size()); 
			
//			minimum_distances.add(Collections.min(distances));
			double minValue = Collections.min(distances); 
			int minIndex = distances.indexOf(minValue);

			double minValue_2 = Collections.min(distances_2); 
			int minIndex_2 = distances_2.indexOf(minValue_2);
			
		/*	
			System.out.println("Solution size "+sol_string.size());
			System.out.println("Min Value "+minValue);
			System.out.println("Min Value "+minValue_2);
			System.out.println("Reference set item closest to solution: "+SolutionUtils.getNumericRepresentation(reference_set.get(minIndex)));
		*/	  
			//System.out.println("Intersections "+(2*sol_string.size()*(minValue-1))/(minValue-2));
			
			int intersection = (intersection(sol_string,SolutionUtils.getNumericRepresentation(reference_set.get(minIndex)))).size();
			int intersection_2 = (intersection(sol_string,SolutionUtils.getNumericRepresentation(reference_set.get(minIndex_2)))).size();
			//Test 
			/*ArrayList<String> test = SolutionUtils.getNumericRepresentation(reference_set.get(minIndex));
			ArrayList<String> test_2 = SolutionUtils.getNumericRepresentation(reference_set.get(minIndex_2));
			double testDist =1-((double)(intersection(sol_string,test)).size())/(((sol_string.size()*2) - (intersection(sol_string,test)).size())) ;
			System.out.println("Test "+testDist);
			ArrayList<String> common = new ArrayList<String>(sol_string);
			common.retainAll(test);
			System.out.println("Intersections test "+common.size());
			*/
			double frequency = 0.0;
			int[] itemset = new int[1];
			int size=(COPConfigurator.referenceSet.size()+COPConfigurator.listOfInternalSolutions.size()); 
			for (int col =0; col<sol_string.size() ; col++) {
				itemset[0] = Integer.parseInt(sol_string.get(col));
				frequency = frequency + (double)(COPConfigurator.itemsetTree_dataset.getSupportOfItemset(itemset))/size;
			}
			
			minimum_distances.add(minValue);		
			minimum_distances_2.add(minValue_2);
			distances.clear();
			distances_2.clear();
	
			/*
			//System.out.println("Minimum Distances vector size: "+minimum_distances.size());
			//System.out.println("Last Minimum distance (the smaller the better): "+ minimum_distances.get(index));
			System.out.println("Last Minimum distance (the smaller the better) - method 1: "+ minimum_distances.get(index));
			System.out.println("Last Minimum distance (the smaller the better) - method 2: "+ minimum_distances_2.get(index));
			*/
			
			closestItem_RefSet.write("Solution: "+ sol_string +": Size: "+sol_string.size()+": Min Abs Distance: " + minValue + ": Avg Frequency: "+(frequency/sol_string.size()) +": Closest Item: " +SolutionUtils.getNumericRepresentation(reference_set.get(minIndex)) + ": Intersections: "+intersection + "\n");
			closestItem_RefSet_2.write("Solution: "+ sol_string +": Size: "+sol_string.size()+": Min Rel Distance: " + minValue_2 + ": Avg Frequency: "+(frequency/sol_string.size())+": Closest Item: " +SolutionUtils.getNumericRepresentation(reference_set.get(minIndex_2)) + ": Intersections: "+intersection_2 + "\n");
			
		}
		closestItem_RefSet.close();
		closestItem_RefSet_2.close();
		if (refSet) {
			SolutionUtils.writeStatisticsToFile(COPConfigurator.baseDir+"/"+filename+"_abs_RS_statistics.txt", minimum_distances);
			SolutionUtils.writeStatisticsToFile(COPConfigurator.baseDir+"/"+filename+"_rel_RS_statistics.txt", minimum_distances_2);
		}
		else {
			SolutionUtils.writeStatisticsToFile(COPConfigurator.baseDir+"/"+filename+ "_abs_KB_statistics.txt", minimum_distances);
			SolutionUtils.writeStatisticsToFile(COPConfigurator.baseDir+"/"+filename+"_rel_KB_statistics.txt", minimum_distances_2);
		}
		
		
		// List of minimum distances from reference set: one element for each solution in sol_set
		//return minimum_distances;
		//return minimum_distances_2;
	}
	
public ArrayList<Double> computeElementKBSimilarity(String element) throws Exception{
		
	ArrayList<String> possibleElements;  
	boolean isTarget; 
		if (Integer.parseInt(element)<=COPConfigurator.targetLimitNumber) {
			possibleElements = COPConfigurator.possibleSources;
			isTarget=false;
		}
		else {
			possibleElements = COPConfigurator.possibleTargets;
			isTarget=true;
		}
		ArrayList<Integer> vector1 = new ArrayList<Integer>(possibleElements.size());
		ArrayList<Integer> vector2 = new ArrayList<Integer>(possibleElements.size()); 
		ArrayList<Double> similarities = new ArrayList<Double>();
				
		//System.out.println("Solution strng "+sol_string);
		for (int i=0; i<COPConfigurator.listOfInternalSolutions.size();i++){ //KB solutions
			ArrayList<String> KB_string = new ArrayList<String>();
			if (isTarget)
				KB_string = SolutionUtils.getTargets(COPConfigurator.listOfInternalSolutions.get(i));
			else
				KB_string = SolutionUtils.getSources(COPConfigurator.listOfInternalSolutions.get(i));
				
			//System.out.println("Main.listOfInternalSolutions.get(i) "+KB_sol_string);
			for (int j = 0; j<possibleElements.size();j++) {
				if (element.equals(String.valueOf(possibleElements.get(j))))
					vector1.add(1);
				else
					vector1.add(0);
				
				if (KB_string.contains(possibleElements.get(j)))
					vector2.add(1);
				else
					vector2.add(0);
			}
			//for (int k=0; k<KB_sol_string.size();k++) if(sol_string.contains(KB_sol_string.get(k))) count++; CONTEGGIO ELEMENTI COMUNI, corrisponde a count11 di Jaccard
			//for (int k=0; k<vector1.size();k++) System.out.println("Element "+k+": "+vector1.get(k));
			//System.out.println("count "+count);
			Double index = vecDist.calculateDistance(vector1, vector2);
			//System.out.println("Jaccard index "+index );
			if (index.isNaN())
				throw new Exception("Jaccard distance returns NaN"); 
			
			// SIM: the greater, the more similar the vectors are
			similarities.add((1 - index)); 
		}
		// SE QUELLE CHE HANNO ENTRAMBI 0 IMPATTANO SULLA DISTANZA, LE POSSO ELIMINARE. 
		
		// SE FACCIO LA SITNESI QUI, aLLORA RESTITUISCO UN SOLO NUMERO. SE NO, LA SINTESI LA DEMANDO AL CHIAMANTE
		return similarities;
	}
	

public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {

	ArrayList<String> list = new ArrayList<String>();

     for (String s : list1) {
         if(list2.contains(s)) {
             list.add(s);
         }
     }

     return list;
}

public ArrayList<String> union(ArrayList<String> list1, ArrayList<String> list2) {

	Set<String> set = new HashSet<String>();

    set.addAll(list1);
    set.addAll(list2);

    return new ArrayList<String>(set);
    
}

	

	/* NOT USED */
/*	public double computeOntologySimilarity(Solution sol){
		
		ArrayList<String> sol_string = SolutionUtils.getNumericRepresentation(sol); 
		// CONTARE QQUANTI CONCETTI DI QUELLI CHE USA SONO DELL'ONTOLOGIA
		System.out.println("\nDEBUG in ontology similarity");
		int countOntologyConcepts = 0;
		for (int j = 0; j<sol_string.size();j++) 
			if (Main.numericTransactionsDB.containsValue(sol_string.get(j)))
				countOntologyConcepts++;
		
		// SE SONO TUTTI DEL DOMINIO, SIMILARITY =1. SE NESSUNO E' DEL DOMINIO, SIM = 0; 
		return countOntologyConcepts/sol_string.size(); 
	}
*/	

	/* public double computeWeightedOntologySimilarity(Solution sol){
		
		ArrayList<String> sol_string = numeric_sol.getNumericRepresentation(sol); 
		//int max = Integer.parseInt(Main.numericTransactionsDB.get(Main.numericTransactionsDB.size()));;
	/*
		se ci sono 5 entità, di cui una nuova, essa pesa 1/5: 0.2 della "conoscenza" è nuovo. 
		se ci sono 10 proprietà, di cui una nuova, essa pesa: 1/10: 0.1. oppure, se l'entità padre ha 5 proprietà di cui una nuova, questa pesa 0.2 di 0.25 (se ci sono 4 entità) 
		idem per i valori. 
		devo però ignorare il primo passaggio e decrementare direttamente da sim=1. sono due metriche diverse.  
		per quest'ultima , assicurare 0-1. 
		
		
		double similarity = 1;	
		Iterator it = Main.newConceptsWeights.entrySet().iterator();
	     while (it.hasNext()) {
	    	 Map.Entry pair = (Map.Entry)it.next();
	    	 similarity =- (Double)(pair.getValue());
	     }
	     
	    // se è vuota, ritorna 1
	/* 	
		for (int i=0; i<sol_string.size();){
			if (Integer.parseInt(sol_string.get(i))>max){
				int new_concept = Integer.parseInt(sol_string.get(i));
				if (new_concept>Main.newEntityBound){ // posso assegnare ilteriori bound, pesati diversamente per indicare diverse "similarity" con il concetto di dominio
					Main.newEntityWeight; increae the tdistance by 1/(evTemp._entityList.size()); 
				}
				if (new_concept>Main.newPropertyBound){
					
					Main.newPropertyWeight; increae the tdistance by 1/(Nproperties+1) o Nproperties dell'entity 
				}
				if (new_concept>Main.newValueBound){
					Main.newValueWeight; ; increae the tdistance by 1/(nvalues+1) o nvalues delal property
				}
			}
		}
		
		return similarity;		
	}*/


}

