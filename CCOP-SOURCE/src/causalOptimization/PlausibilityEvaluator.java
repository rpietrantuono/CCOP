package causalOptimization;

import java.util.ArrayList;

import jmetal.core.Solution;
import util.SolutionUtils;

public class PlausibilityEvaluator {

	

	  
	  public double logarithmicPlausibilityEvaluation(Solution solution, boolean internal) throws Exception {
		  
		  ArrayList<String> targets = new ArrayList<String>();
		  targets.addAll(SolutionUtils.getTargets(solution));
		  ArrayList<String> sources =  new ArrayList<String>();
		  sources.addAll(SolutionUtils.getSources(solution));
		
		  //System.out.println("Size of the source "+sources.size());
		  int N = sources.size();
		  double x = plausibilityEvaluation(solution, internal); 
		  
		  
		  // ADJUST BASED ON SOURCE SIZE - TO DO
		  //LOGARITHMIC 
		 // System.out.println("result "+Math.log(1+Math.pow(x, 1.0/N))/Math.log(2));
		  
		  return  Math.log(1+Math.pow(x, 1.0/N))/Math.log(2);
		  
		  
	  }
	  
	  public double rootPlausibilityEvaluation(Solution solution, boolean internal) throws Exception {
		  
		  ArrayList<String> targets = new ArrayList<String>();
		  targets.addAll(SolutionUtils.getTargets(solution));
		  ArrayList<String> sources =  new ArrayList<String>();
		  sources.addAll(SolutionUtils.getSources(solution));
		
		  int N = sources.size();
		  double x = plausibilityEvaluation(solution, internal); 
		  
		  
		  // ADJUST BASED ON SOURCE SIZE - TO DO
		  
		  return  Math.pow(x, 1.0/N);
		  
		  
	  }


	  public double plausibilityEvaluation(Solution solution, boolean internal) throws Exception {
			
		  myItemsetTree.ItemsetTree tree = null;  
		  /*if(internal)*/
			  tree = COPConfigurator.itemsetTree;
		  /*else
			  tree = COPConfigurator.itemsetTree_external; 
			*/  
		  
	    	int support;
	    	COPConfigurator.supportMap.clear();
	    	
//			ArrayList<String> sol_string = SolutionUtils.getNumericRepresentation(solution);  
			ArrayList<String> targets = new ArrayList<String>();
			targets.addAll(SolutionUtils.getTargets(solution));
			ArrayList<String> sources =  new ArrayList<String>();
			sources.addAll(SolutionUtils.getSources(solution));
			  
	//		System.out.println("\nPlausibility evaluation...");
	//		System.out.println("sources: " + sources);
//			System.out.println("targets: " + targets);
				  
			  int N = sources.size();//SolutionUtils.getSolutionSize(example); 
			  
			  //LA FaCCIO CON Target.class  
			  
			  ArrayList<ArrayList<String>> combinationsEvaluated = new ArrayList<ArrayList<String>>(); 
			  ArrayList<ArrayList<String>> combinationsToEvaluate = new ArrayList<ArrayList<String>>();
			  ArrayList<ArrayList<String>> combinationsToSkip = new ArrayList<ArrayList<String>>();
			  ArrayList<ArrayList<String>>[] allCombinations = (ArrayList<ArrayList<String>>[])new ArrayList[N];
			  int count=0;
		
			  //TEMPORARY CHECK
		//	  if (targets.size()>1) throw new Exception("Solutions are built with one target at a time"); // MOVE THIS
			  int targetUnderEvaluation = Integer.parseInt(targets.get(0)); 
			  //for (int i=0; i<targets.size();i++){
			  //		  int targetUnderEvaluation = Integer.parseInt(targets.get(i));
			  // se conviene dal basso a dall'alto dipende da quanto tempo impiega getSupport su combinazioni corte o lunghe
			  // faccio dal basso
			  //INITIALIZE
			  ArrayList<String> temp = new ArrayList<String>(1);
			  for (int init=0; init<N; init++) {
					  temp.add(sources.get(init));
					  combinationsToEvaluate.add(new ArrayList<String>(temp));
					  temp.clear();
				  }

			  for (int k=1; k<=N;k++){  //trascuro K=0
		//		  System.out.println("K: "+k);
				  //System.out.println("binomial: "+binomial(N, k));
				  int numberOfEvaluations = combinationsToEvaluate.size();
				  for (int j=0; j<numberOfEvaluations ;j++) { //HazardAnalysisProblem.binomial(N, k)
					  /*** WITH TARGET ***/
					  int [] itemsToMatch = new int[k+1]; // because of appending the target					  
					  for (int h=0; h<k ;h++) itemsToMatch[h] = Integer.parseInt(combinationsToEvaluate.get(j).get(h));
					  itemsToMatch[k] = targetUnderEvaluation;   //append the target
					  /*** WITHOUT TARGET *** 
					  int [] itemsToMatch = new int[k]; 					  
					  for (int h=0; h<k ;h++) itemsToMatch[h] = Integer.parseInt(combinationsToEvaluate.get(j).get(h));
					  */
					  //for (int h=0; h<=k ;h++) System.out.println(" itemsToMatch[h] "+h+" : "+ itemsToMatch[h]);
					  support = tree.getSupportOfItemset(itemsToMatch);
					  //System.out.println(+j+"th combination Under Evaluation, k = "+k+" : "+combinationsToEvaluate.get(j));
					  //System.out.println("support: "+support); 
					  if(support!=0){
						  count++;
						  if(internal)
							  COPConfigurator.supportMap.put(combinationsToEvaluate.get(j), support); // TO RETRIVE, USE comparison (or a three-entry map with "Entry")
						  addUniqueParentsToEvaluate(combinationsToEvaluate.get(j), combinationsToEvaluate, combinationsToSkip,N, sources); 
						  	// add N-k parents (append k parents to the solution); CHECK FOR DUPLICATES
						  combinationsEvaluated.add(combinationsToEvaluate.get(j));
					  }
					  else{
						  addElementsToSkip(combinationsToEvaluate.get(j), combinationsToSkip, N, sources);
					  }
			  		}
				  for (int init=0; init<combinationsToEvaluate.size(); init++) {
					  if(combinationsToEvaluate.get(init).size()<=k) {
						  combinationsToEvaluate.remove(init);
						  init--;
					  }
				  }
		//		  System.out.println("combinations To Evaluate in the next step (of k-degree) "+combinationsToEvaluate.size());
		//		  for (int init=0; init<combinationsToEvaluate.size(); init++) 
		//			  System.out.println("combinations "+combinationsToEvaluate.get(init));
				  
				mergeCombToEvaluateToSkip(combinationsToEvaluate, combinationsToSkip); 
	//			System.out.println("combinations To Evaluate in the next step after merging (of k-degree) "+combinationsToEvaluate.size());
				  //combinationsToEvaluate.clear();
				//combinationsToSkip.clear();
			  }
	//		  System.out.println("\n ***+ Plausibility Evaluation: COUNT: "+count);
	//		  System.out.println("**** Plausibility Evaluation: plausibility value: "+((double)count)/(Math.pow(2, N)-1));
	//		  System.out.println("****Plausibility Evaluation: combinations Evaluated (combinations with Support): "+combinationsEvaluated.size()+"\n"); //POSSO CALCOLARMI I DEGREE
	//		  for (int a=0; a< combinationsEvaluated.size();a++ ) System.out.println("combinationsEvaluated  "+combinationsEvaluated.get(a));
			  
		/*	  Iterator it = Main.supportMap.entrySet().iterator();
			  System.out.println("\n Map size: "+Main.supportMap.size());
			  while (it.hasNext()) {
				  Map.Entry pair = (Map.Entry)it.next();
				  System.out.println("Key=value: "+pair.getKey()+" = " + pair.getValue());
			  }
			  */
			  //ArrayList<String> listToSearch = new ArrayList<String>();
			  //listToSearch .add("2");
			  //listToSearch .add("13");
			  //System.out.println("\n return support of 2, 13: "+supportMap.get(listToSearch ));
			  //}
			  return ((double)count)/(Math.pow(2, N)-1);
	    
	    }
		private static void addElementsToSkip(ArrayList<String> childCombination, 
				ArrayList<ArrayList<String>> combinationsToSkip, int N, ArrayList<String> sources) {
		
			ArrayList<String> newCombination = new ArrayList<String>(childCombination);
			//ArrayList<ArrayList<String>> parents = new ArrayList<ArrayList<String>>();
			boolean duplicate = false;
			for (int k1=0; k1< combinationsToSkip.size(); k1++ ) {
				if(combinationsComparison(newCombination, combinationsToSkip.get(k1))) {
					duplicate  = true; 
					break;
				}
			}
			if (!duplicate) combinationsToSkip.add(new ArrayList<String>(newCombination));
		}
		
		
	private static void addUniqueParentsToEvaluate(ArrayList<String> childCombination, ArrayList<ArrayList<String>> combinationsToEvaluate,
				ArrayList<ArrayList<String>> combinationsToSkip, int N, ArrayList<String> sources) {
			
			
			ArrayList<String> newCombination = new ArrayList<String>(childCombination);
			ArrayList<ArrayList<String>> parents = new ArrayList<ArrayList<String>>();
			
			// build parents
			boolean duplicate = false;
			boolean toSkip = false; 
			for (int i=0; i<N; i++) {
				if (!newCombination.contains(sources.get(i))) {  
					duplicate = toSkip =  false; 
					newCombination.add(sources.get(i));
					
					for (int k1=0; k1< combinationsToEvaluate.size(); k1++ ) {
						if(combinationsComparison(newCombination, combinationsToEvaluate.get(k1))) { 
							duplicate = true;
			//				System.out.println("duplicate "+k1);
			//				for (int index=0; index<combinationsToEvaluate.get(k1).size(); index++)System.out.println(" "+combinationsToEvaluate.get(k1).get(index)); 
						}
					}
					for (int k2 = 0; k2 < combinationsToSkip.size(); k2++) {
						if (combinationsToSkip.get(k2).contains(sources.get(i))) {
							toSkip=true;
		//					System.out.println("TO SKIP "+newCombination);
							}
						//for (int ind2 = 0; ind2< combinationsToSkip.get(ind1).size(); ind2++) {
					}
					
					if (duplicate == false && toSkip == false) {
						parents.add(new ArrayList<String>(newCombination));
						combinationsToEvaluate.add(new ArrayList<String>(newCombination));
					}
				}
				newCombination = new ArrayList<String>(childCombination);
			}
			//for (int i=0; i<parents.size(); i++) {
	//			System.out.println("PARENT "+i);
	//			System.out.println(parents.get(i));
				//for (int j=0; j<parents.get(i).size(); j++) {
					//System.out.println(parents.get(i));
				//}
			//}
			
			
		}
	private static void mergeCombToEvaluateToSkip(ArrayList<ArrayList<String>> combinationsToEvaluate,
			ArrayList<ArrayList<String>> combinationsToSkip) {
	//	System.out.println("Combinations to evaluate "+combinationsToEvaluate);	
//		System.out.println("Combinations to evaluate size "+combinationsToEvaluate.size());
	//	System.out.println("Combinations to skip "+combinationsToSkip);
//		System.out.println("Combinations to skip size "+combinationsToSkip.size());
		for (int j=0; j<combinationsToSkip.size(); j++ ) {
			int i = 0;
			if (!combinationsToEvaluate.isEmpty()) {
				while (i<combinationsToEvaluate.size()) { 
					if(combinationsToEvaluate.get(i).containsAll(combinationsToSkip.get(j))) {
						combinationsToEvaluate.remove(i);
		//				System.out.println("Combinations to evaluate IN "+combinationsToEvaluate);
	//					System.out.println("Combinations to evaluate SIZE IN "+combinationsToEvaluate.size());
					}
					else {
						i++;
					}
					if (combinationsToEvaluate.isEmpty()) {
						break;
					}
				}
			}
		}
		
		/*
		 * int i=0;
		 * 
		 * while (i<combinationsToEvaluate.size()) { for (int j=0;
		 * j<combinationsToSkip.size(); j++ ) {
		 * if(combinationsToEvaluate.get(i).containsAll(combinationsToSkip.get(j)))
		 * {//.get(k1))) { combinationsToEvaluate.remove(i);
		 * System.out.println("Combinations to evaluate IN "+combinationsToEvaluate);
		 * System.out.println("Combinations to evaluate SIZE IN "+combinationsToEvaluate
		 * .size()); } } i++; if(combinationsToEvaluate.isEmpty()) break; }
		 */
		//System.out.println("Combinations to evaluate AFTER "+combinationsToEvaluate);	
		
		/*for (int i=0; i<combinationsToEvaluate.size(); i++ ) {
			for (int j=0; j<combinationsToSkip.size(); j++ ) {
					if(combinationsToEvaluate.get(j).containsAll(combinationsToSkip.get(j))) {//.get(k1))) {
						combinationsToEvaluate.remove(j);
						i--;
						System.out.println("Merged, index "+i);
						System.out.println("Size of comb to ev. "+combinationsToEvaluate.size());
					}
			}
		}*/
			
	}
	private static boolean combinationsComparison(ArrayList<String> combination1, ArrayList<String> combination2) {
		// return true if one contains the same elements (in whatever order) than the other. Assume no duplicates in a list
		int count = 0;
//		int min;
		if (combination1.size() != combination2.size()) {
			//System.out.println("Not comparable, they are of different sizes "); 
			return false;
		}
//			return false; min = combination1.size();
//		else
//			min  = combination2.size();
		
		for (int i=0; i<combination1.size();i++) { 
			for (int j=0; j<combination2.size();j++) {
				if(combination1.get(i).equals(combination2.get(j))) 
					count++;
			}
		}
		if (count==combination1.size())
			return true;
		else
			return false; 
	}
/*
	public Integer getMaxKDegreeWithAutoExclusion(ArrayList<String> elements, boolean internal, int minSupport) {
		
		// the elements are excluded from the counting: in other words, a support > 1 is considered instesad than >0
		
		 myItemsetTree.ItemsetTree tree = null;
		 if(internal)
			 tree = COPConfigurator.itemsetTree;
		 else
			 tree = COPConfigurator.itemsetTree_external;
			
		 //tree.printTree();
		 
		  int N = elements.size(); 
//		  System.out.println("MaxKDegree computation: size of the group of elements: "+N);
		  
		  ArrayList<ArrayList<String>> combinationsEvaluated = new ArrayList<ArrayList<String>>(); 
		  ArrayList<ArrayList<String>> combinationsToEvaluate = new ArrayList<ArrayList<String>>();
		  ArrayList<ArrayList<String>> combinationsToSkip = new ArrayList<ArrayList<String>>();
		  ArrayList<ArrayList<String>>[] allCombinations = (ArrayList<ArrayList<String>>[])new ArrayList[N];
		  int count=0;
	
		  ArrayList<String> temp = new ArrayList<String>(1);
		  for (int init=0; init<N; init++) {
				  temp.add(elements.get(init));
				  combinationsToEvaluate.add(new ArrayList<String>(temp));
				  temp.clear();
			  }
		  
		  int maxk_degree  = N; 
		  for (int k=1; k < N; k++){  //trascuro K=0
			  count = 0; 
			  int numberOfEvaluations = combinationsToEvaluate.size();
			  for (int j=0; j<numberOfEvaluations ;j++) { 
				  int [] itemsToMatch = new int[k];
				  for (int h=0; h<k ;h++) itemsToMatch[h] = Integer.parseInt(combinationsToEvaluate.get(j).get(h)); 
				  //for (int h=0; h<k ;h++) System.out.println(" itemsToMatch[h] "+ itemsToMatch[h]);				  
				  int support = tree.getSupportOfItemset(itemsToMatch);
				  if(support>minSupport){ // so as to exclude the "elements" themselves from the counting
					  count++;
					  addUniqueParentsToEvaluate(combinationsToEvaluate.get(j), combinationsToEvaluate, combinationsToSkip,N, elements); 
					  combinationsEvaluated.add(combinationsToEvaluate.get(j));
				  }
				  else{
					  addElementsToSkip(combinationsToEvaluate.get(j), combinationsToSkip, N, elements);
				  }
		  		}
			  for (int init=0; init<combinationsToEvaluate.size(); init++) {
				  if(combinationsToEvaluate.get(init).size()<=k) {
					  combinationsToEvaluate.remove(init);
					  init--;
				  }
			  }
			mergeCombToEvaluateToSkip(combinationsToEvaluate, combinationsToSkip); 
			//System.out.println("combinations To Evaluate in the next step after merging (of k-degree)"+combinationsToEvaluate.size());
			  //combinationsToEvaluate.clear();
			//combinationsToSkip.clear();
			if (count == 0) {
				maxk_degree = k; 
				return maxk_degree;   
			}
		  }	
		  
		return maxk_degree;
	}
	*/
	
	
}
