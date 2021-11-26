package causalOptimization;

import java.io.IOException;
import java.util.PriorityQueue;

import myItemsetTree.ItemsetTree;
import util.MyAlgoTopKClassRules;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.ClassRuleG;
import ca.pfv.spmf.algorithms.associationrules.TopKRules_and_TNR.Database;
import jmetal.core.Solution;


public class FrequencyEvalutaor {

	public ItemsetTree buildItemSetTree(String filepath){
		
		// Applying the algorithm to build the itemset tree
		ItemsetTree itemsetTree = new ItemsetTree();
		// method to construct the tree from a set of transactions in a file
		try {
			itemsetTree.buildTree(filepath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print the statistics about the tree construction time and print the tree in the console
		itemsetTree.printStatistics();
		//itemsetTree.printTree();
		return itemsetTree;
	}
	
	public PriorityQueue<ClassRuleG> minFixedConsequentRules(Solution sol, int k, Database database, double minConfidence, int[] itemToBeUsedAsConsequent, Integer... maxAntecedentSize) throws IOException{
		
		// TOP K ASSOCIATION RULES WITH FIXED CONSEQUENT 
	    Integer value = maxAntecedentSize.length > 0 ? maxAntecedentSize[0] : -1;
	    int maxAntSize = value.intValue();
	    
		//ALGORITHM TOP_K_CLASS 
		//Database database = new Database(); 
		//int k = 3; 
		//double minConfidence = 0.8; 
		// the item to be used as consequent for generating rules. 
		//POTREI ANCHE INVERTIRE E FISSARE GLI ANTECEDENTI 
		//int[] itemToBeUsedAsConsequent = new int[]{656};
		//database.loadFile((Main.baseDir+"/temporaryDBFile.txt"));
		
		MyAlgoTopKClassRules algo = new MyAlgoTopKClassRules();
		
		if(maxAntSize!=-1)
			algo.setMaxAntecedentSize(maxAntSize);  // optional
		
		algo.runAlgorithm(k, minConfidence, database, itemToBeUsedAsConsequent);
		algo.printStats();
		algo.writeResultTofile(COPConfigurator.baseDir+"/output.txt");   // to save results to file
		return algo.getKRules(); 
		//ALTERNATIVE: ALGORITHM FP_GROWTH_SAVE_TO_MEMORY_WITH_LIFT
		/*// Loading the binary context
		String input = fileToPath("contextIGB.txt");
		
		// By changing the following lines to some other values
		// it is possible to restrict the number of items in the antecedent and
		// consequent of rules
		int maxConsequentLength = 40;
		int maxAntecedentLength = 40;
		
		// STEP 1: Applying the FP-GROWTH algorithm to find frequent itemsets
		double minsupp = 0.5;
		AlgoFPGrowth fpgrowth = new AlgoFPGrowth();
		fpgrowth.setMaximumPatternLength(maxAntecedentLength + maxConsequentLength);
		Itemsets patterns = fpgrowth.runAlgorithm(input, null, minsupp);
		int databaseSize = fpgrowth.getDatabaseSize();
		patterns.printItemsets(databaseSize);
		
		// STEP 2: Generating all rules from the set of frequent itemsets (based on Agrawal & Srikant, 94)
		double  minlift = 0;
		double  minconf = 0.90;
		AlgoAgrawalFaster94 algoAgrawal = new AlgoAgrawalFaster94();
		algoAgrawal.setMaxConsequentLength(maxConsequentLength);
		algoAgrawal.setMaxAntecedentLength(maxAntecedentLength);
		// the next line run the algorithm.
		// Note: we pass null as output file path, because we don't want
		// to save the result to a file, but keep it into memory.
		AssocRules rules = algoAgrawal.runAlgorithm(patterns,null, databaseSize, minconf, minlift);
		rules.printRulesWithLift(databaseSize);*/
				
	}
	
	// ALTRI - per esempio fisso antecedenti, oppure uso ferquent itemset
	
	
	
}
