package causalOptimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

import util.SolutionUtils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import ca.pfv.spmf.test.MainTestTopKClassRules;
import causalOptimization.CopVariable.*;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.problems.COPProblem;

public class COPConfigurator {

	
	
	public static int internalArchiveSize; // = 400; 
	//public static int externalArchiveSize  = 80;  
	public static int MAX_sources; // = 27; //Take from input file, number of columns 
	public static int MIN_sources; // = 5;
	public static int MAX_targets; // = 1;
	public static int MIN_targets; // = 1;
	public static int seed = 1; //=1;
	public static boolean unique = true; //=1;
	public static int targetLimitNumber;
	public static ArrayList<Integer> groupLimitNumber; 
		  
	public static ArrayList<String> possibleSources; // from ontology
	public static ArrayList<ArrayList<String>> possibleSourcePerType;
	public static ArrayList<ArrayList<String>> possibleTargetPerType;
	public static ArrayList<String> possibleTargets; // from ontology
	public static ArrayList<Object> ontology;
	public static ArrayList<String> numericSources;
	public static SimilarityEvalutaor sim;
	public static PlausibilityEvaluator plaus;
	public static FrequencyEvalutaor freq;
	

	public static SolutionSet listOfInternalSolutions;
	public static SolutionSet listOfExternalSolutions;
	public static SolutionSet referenceSet; 
	public static ArrayList<String> possibleKBsources;
	public static ArrayList<ArrayList<String>> possibleKBsourcesPerType;
	public static ArrayList<String> possibleKBtargets; 
	
	//public static COPProblem myProblem;
	public static COPProblem myProblemString;
	public long executionTime;

	// TAKE FROM INPUT
	public static String baseDir; // = new String("/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/GECCO");
	static Path ontologySourcesfile; // = Paths.get(baseDir + "/OntologySources.txt");
	static Path ontologyTargetsfile;// = Paths.get(baseDir + "/OntologyTargets.txt");
	static Path inputKB_CSVFile; // = Paths.get(baseDir+ "/internalCSV.csv"); 
	static String inputKBFile; // = baseDir + "/ASRS_DBOnline_final_formatted.xlsx";
	static String internalKnowledgeBaseFile; // = baseDir + "/internalKBFile.txt";
	static String datasetFile; // = baseDir + "/dataset.txt";
	//static String externalKnowledgeBaseFile = baseDir + "/externalKBFile.txt";
	//static String temporarySolutionsFile = baseDir + "/temporarySolutionsFile.txt";
	public static Map<Integer, String> numericTransactionsDB;
	public static Map<ArrayList<String>, Integer> supportMap;  

	public static myItemsetTree.ItemsetTree itemsetTree;
	public static myItemsetTree.ItemsetTree itemsetTree_dataset;
	public static Random ran;
	public static String technique="COP";
	public static void configure(String configurationFile) throws Exception {
		
		/****
		 * Format of the configuration file 
		 * BaseDir = /.../.../   					  #Base directory, absolute Path. 
		 * Ontology file with causes = filename1.txt  #Must be located in BaseDir
		 * Ontology file with effects = filename2.txt #Must be located in BaseDir
		 * Input dataset file = input.csv 			  #Input dataset. Must be located in BaseDir. Separator: ",". Spearator between multiple values in the same column (i.e., of the same variable) is "+" (e.g.: 14+17, 12, 10+3, 22). Note: the last column is assumed to be the effect to explain
		 * Knowledge base size = <integer number>	  #Size of the knowledge base used for plausibility and novelty assessment. Must be less than the size of the input dataset
		 * Min solution size    = <integer number>	  #Minimum size of the solutions (must be greater than 0 and less than the "number of variables - 1", namely number of columns -1, since the last column is the effect)
		 * Max solution size    = <integer number>	  #Maximum size of the solutions (must be greater than 0 and than minimum source, and less than the "number of variables - 1", namely number of columns -1, since the last column is the effect)
		 * List of constraints "forbidden" = x,y;z,w  #Format: x,y;w,z; ...  where x,y,w,z are numbers starting from 1 and representing the values we want to 								not appear together in a solution. 
							  						  #These values are taken as the row number of the cause ontology file (e.g., if the fifth and eighth value of the ontology and the second and sixth value of the ontology must not appear together in a solution, here you should put 5,8;2,6)  
		 * List of constraints "required" = x,y;z,w	  #Format: Format: x,y;w,z; ...where x,y,w,z are numbers starting from 1 and representing the values we require to 								appear together in a solution. 
							  						  #These values are taken as the row number of the cause ontology file (e.g., if the fifth and eighth value 								of the ontology and the second and sixth value of the ontology must appear together in a solution, here you should put 5,8;2,6) 
		 * Seed = <integer number>					  #Seed given to replicate the experiment. If empty, random generators will be invoked without the seed
		 * Unique = <true false>					  #Indicates if the columns'encoding use the same values or not (e.g., values of column 1: 1,2,3 ; values of columns 2: 1,2; ...=>unique=false. values of column 1: 1,2,3 ; values of columns 2: 4,5; ...=>unique=true). Default is true
		 * 
		 * */
		
		ArrayList<String> fileLines = (ArrayList<String>)Files.readAllLines(Paths.get(configurationFile), StandardCharsets.UTF_8);
		
		StringTokenizer st;		
		ArrayList<String> finalParameters = new ArrayList<String>(); 
		for (int r = 0; r < fileLines.size(); r++) {
			String row = fileLines.get(r);
			//System.out.println("current row +"+r);
			if (row != null) {
				String[] cells = row.split("=");
				if(cells.length>1) {
		//			System.out.println("cells "+cells[0]);
		//			System.out.println("cells "+cells[1]);
					String[] parameters =cells[1].split("#");  
					finalParameters.add(parameters[0].trim());
				}//else
			//		System.out.println("cells length 0 "+cells[0]); //to remove
			}
			//else
				//System.out.println("skip line ..."); //to remove
		}
		//System.out.println("Final params"+finalParameters); 
	
		baseDir = finalParameters.get(0); //+"/COP"; 
		ontologySourcesfile = Paths.get(baseDir + "/" +finalParameters.get(1));
		ontologyTargetsfile = Paths.get(baseDir + "/"+ finalParameters.get(2));
		inputKB_CSVFile = Paths.get(baseDir + "/" + finalParameters.get(3)); 

		internalKnowledgeBaseFile = baseDir + "/internalKBFile.txt";
		datasetFile = baseDir + "/dataset.txt";
		internalArchiveSize = Integer.parseInt(finalParameters.get(4)); 
		//public static int externalArchiveSize  = 80;  
		MIN_sources = Integer.parseInt(finalParameters.get(5));;
		MAX_sources = Integer.parseInt(finalParameters.get(6));;
		MAX_targets = 1;
		MIN_targets = 1;
		// processing the list of constraints
		
		ArrayList<ArrayList<String>> listForbidden = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> listRequired = new ArrayList<ArrayList<String>>();
		if (!finalParameters.get(7).isEmpty()) {
			String forbidConstraints[] = finalParameters.get(7).split(";");
			String requireConstraints[] = finalParameters.get(8).split(";"); 
			
			listForbidden = new ArrayList<ArrayList<String>>();  
			for (int i=0; i<forbidConstraints.length;i++) {
				listForbidden.add(new ArrayList<String>());
				String singleForbidConstraint[] = forbidConstraints[i].split(",");
				for (int j=0; j<listForbidden.get(i).size();j++) {
					listForbidden.get(i).add(singleForbidConstraint[j].trim()); 
				}
			}
			listRequired = new ArrayList<ArrayList<String>>();  
			for (int i=0; i<requireConstraints.length;i++) {
				listRequired.add(new ArrayList<String>());
				String singleRequireConstraint[] = requireConstraints[i].split(",");
				for (int j=0; j<listRequired.get(i).size();j++) {
					listRequired.get(i).add(singleRequireConstraint[j].trim()); 
				}
			}
		}else {
			listForbidden=null;
			listRequired=null;
		}
		seed=Integer.parseInt(finalParameters.get(9));
		unique=Boolean.parseBoolean(finalParameters.get(10));
		
		System.out.println("\nConfig parameters...\n");
		System.out.println("Ontology source file: "+ontologySourcesfile);
		System.out.println("Ontology source file: "+ontologyTargetsfile);
		System.out.println("Input CSV file: "+inputKB_CSVFile);
		System.out.println("KB Size: "+internalArchiveSize);
		System.out.println("MIN_sources: "+MIN_sources);
		System.out.println("MAX_sources: "+MAX_sources);
		System.out.println("List of forbidden co-occurrences: "+listForbidden);
		System.out.println("List of required co-occurrences: "+listRequired);
		System.out.println("Seed "+seed);
		System.out.println("Unique "+unique);
		
		/*******START *******/
		/*File directory = new File(baseDir);
		if (!directory.exists()) {
			new File(baseDir).mkdirs();
			System.out.println("Creating " + baseDir);
		}
		File directory_data = new File(baseDir+"/data/");
		if (!directory_data.exists()) {
			new File(baseDir+"/data/").mkdirs();
			System.out.println("Creating " + baseDir+"/data/");
		}*/
		
		Files.deleteIfExists(Paths.get(baseDir + "/output.txt"));
		Files.deleteIfExists(Paths.get(internalKnowledgeBaseFile));
		//Files.deleteIfExists(Paths.get(externalKnowledgeBaseFile));
		Files.deleteIfExists(Paths.get(datasetFile));
		//Files.deleteIfExists(Paths.get(temporarySolutionsFile));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/closestItem_RefSet_abs.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/closestItem_RefSet_rel.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/closestItem_KBSet_abs.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/closestItem_KBSet_rel.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/abs_dist_RS_statistics.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/rel_dist_RS_statistics.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/abs_dist_KB_statistics.txt"));
		Files.deleteIfExists(Paths.get(COPConfigurator.baseDir+"/rel_dist_KB_statistics.txt"));
		Files.deleteIfExists(Paths.get(baseDir + "/statistics_over_repetitions.txt"));

		
		sim = new SimilarityEvalutaor();
		plaus = new PlausibilityEvaluator(); 
		freq = new FrequencyEvalutaor();
		supportMap = new HashMap<ArrayList<String>, Integer>(); 
		groupLimitNumber = new ArrayList<Integer>();
		ran = new Random(seed);
		
		 
		myProblemString = new COPProblem("CauseEffectSolutionType");
		buildOntology();
		numericSources = new ArrayList<String>();
		for (int i =0 ; i< COPConfigurator.targetLimitNumber;i++) numericSources.add(String.valueOf(i+1));
	
	System.out.println("possible sources" +possibleSources);
	System.out.println("possible sources" +possibleTargets); 
	
	/*if (!unique) {
			System.out.println("\n\n*******TRANSOFRM INPUT FILE IF NEEDED  **********");
			ArrayList<String> fileLinesToConvert = (ArrayList<String>)Files.readAllLines(inputKB_CSVFile, StandardCharsets.UTF_8);
			boolean conversionOK = convertFile(COPConfigurator.getReferenceSet(1, (fileLinesToConvert.size()-1)));
			if(conversionOK)
				inputKB_CSVFile  = Paths.get(baseDir + "/" + "converted_"+finalParameters.get(3));
			else
				throw new Exception("Error occurred during the conversion of the input file triggered by the pararmeter unique=false of the config file");
		}
			
		System.out.println("inputKB_CSVFile "+inputKB_CSVFile.getFileName());
		System.exit(0);
		*/
		
		System.out.println("\n\n*******START IMPORT FROM DB  **********");
		int starting_solution = 1;
		int number_of_solutions = internalArchiveSize;
		
		try {

			System.out.println("\n*******INTERNAL AND EXTERNAL ARCHIVEs CREATION: KNOWLEDGE BASE *********");
			//listOfInternalSolutions = getInternalSolutions(starting_solution, number_of_solutions);
			listOfInternalSolutions = getInternalSolutionsFromCSV(starting_solution, number_of_solutions);
			writeSolutionsToFile(listOfInternalSolutions,internalKnowledgeBaseFile); // WRITE INTERNAL SOLUTION TO FILE
			System.out.println("Number of imported accidents " + listOfInternalSolutions.size());
		} catch (Exception e) {e.printStackTrace();}

		possibleKBsources = new ArrayList<String>();
		possibleKBsourcesPerType = new ArrayList<ArrayList<String>>();
		possibleKBtargets = new ArrayList<String>();
		for (int i=0; i<listOfInternalSolutions.size();i++ ) {
			ArrayList<String> sourcesTemp = SolutionUtils.getSources(listOfInternalSolutions.get(i));
			ArrayList<String> targetsTemp = SolutionUtils.getTargets(listOfInternalSolutions.get(i));
		//	possibleKBsourcesSet.addAll(sourcesTemp);
		//	possibleKBtargetsSet.addAll(targetsTemp);
		    for(int j=0; j<sourcesTemp.size();j++) if (!possibleKBsources.contains(sourcesTemp.get(j))) possibleKBsources.add(sourcesTemp.get(j));
		    for(int j=0; j<targetsTemp.size();j++) if (!possibleKBtargets.contains(targetsTemp.get(j))) possibleKBtargets.add(targetsTemp.get(j));
		}
		//System.out.println("kb source size sources "+possibleKBsources);
		//System.out.println("kb target size sources "+possibleKBtargets);
		
		System.out.println(" Main group limit "+COPConfigurator.groupLimitNumber);
		System.out.println(" Main group limit size "+COPConfigurator.groupLimitNumber.size());
		int previousLimit=0;
		
		
		for (int j=0; j<COPConfigurator.groupLimitNumber.size();j++) {
			possibleKBsourcesPerType.add(new ArrayList<String>()); // add a type	
			for (int i=0; i<possibleKBsources.size(); i++) {
				if (Integer.parseInt(possibleKBsources.get(i)) <= COPConfigurator.groupLimitNumber.get(j) && Integer.parseInt(possibleKBsources.get(i)) >previousLimit) 
					possibleKBsourcesPerType.get(j).add(possibleKBsources.get(i));
		 	}
			previousLimit = COPConfigurator.groupLimitNumber.get(j);
		}
		//System.out.println(" possibleKBsourcesPerType "+possibleKBsourcesPerType);
		//System.out.println(" possibleKBsourcesPerType.size "+possibleKBsourcesPerType.size());
		
		System.out.println("\n*******CONSTRUCTING THE ITEMSET TREE FOR INTERNAL ARCHIVEs*********\n");
	    itemsetTree = freq.buildItemSetTree(internalKnowledgeBaseFile);
			
		System.out.println("[***DEBUG***] Number of possible sources "+COPConfigurator.targetLimitNumber);
	
	}

		public synchronized static void computeTestSetDistance(SolutionSet rs, String caller_algorithm, int runs) {
		try {
			ArrayList<String> fileLines = (ArrayList<String>)Files.readAllLines(inputKB_CSVFile, StandardCharsets.UTF_8);

			referenceSet = COPConfigurator.getReferenceSet(internalArchiveSize+1, (fileLines.size()-internalArchiveSize-1));
		    SolutionSet union = ((SolutionSet) referenceSet).union(listOfInternalSolutions);
	        writeSolutionsToFile(union,datasetFile); // WRITE dataset TO FILE
	        
	        System.out.println("\n*******CONSTRUCTING THE ITEMSET TREE FOR THE WHOLE DATASETs*********\n");
	        System.out.println("\nThis is for evaluation purpose only, to collect statistics \n");
	        itemsetTree_dataset = freq.buildItemSetTree(datasetFile);
			
		    System.out.println("Computing test set distance...");

	        COPConfigurator.sim.computeReferenceSetDistance(rs, referenceSet,"closestItem_RefSet_"+caller_algorithm, runs, true);
	        COPConfigurator.sim.computeReferenceSetDistance(rs, COPConfigurator.listOfInternalSolutions,"closestItem_KBSet_"+caller_algorithm,runs, false);
		} catch (Exception e) {e.printStackTrace();}
  }
	public static void distanceStatistics(String[] alg_list) {
		String filename; 
		ArrayList<String> fileLines=null;
		StringTokenizer st;
		
		
		FileWriter output=null; 
		PrintWriter pw=null;
		try {
			output  = new FileWriter(baseDir+"/statistics_over_repetitions.txt", true);
			pw = new PrintWriter(new BufferedWriter(output));	
			
		} catch (IOException e) {e.printStackTrace();}
		
		String line="";
		/*ArrayList<Double> averages = new ArrayList<Double>();
		ArrayList<Double> medians = new ArrayList<Double>();
		ArrayList<Double> min = new ArrayList<Double>();
		ArrayList<Double> max = new ArrayList<Double>();
		ArrayList<Double> std = new ArrayList<Double>(); 
		*/
		double averages = 0;
		double 		medians = 0;
		double min = 0;
		double max = 0;
		double std = 0;
		line = "\nabsolute distance";
		pw.println(line);
		pw.flush();
	
		for (int i = 0; i< alg_list.length; i++) {
			filename = baseDir+"/closestItem_RefSet_"+alg_list[i]+"_abs_RS_statistics.txt";
			try {
				fileLines = (ArrayList<String>)Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			} catch (IOException e) {e.printStackTrace();}
			for (int j=0; j<fileLines.size();j++) {
				//			System.out.println("cells to stirng "+cell);
				st = new StringTokenizer(fileLines.get(j), ",");
				while (st.hasMoreTokens()) {
					averages = averages + (Double.parseDouble(((st.nextToken()).trim())));
					medians = medians  + (Double.parseDouble(((st.nextToken()).trim())));
					min = min + (Double.parseDouble(((st.nextToken()).trim())));
					max = max + (Double.parseDouble(((st.nextToken()).trim())));
					std = std + (Double.parseDouble(((st.nextToken()).trim())));
				}
				  
			}
			averages = averages/fileLines.size();
			medians = medians /fileLines.size();
			min = min/fileLines.size();
			max = max /fileLines.size();
			std = std /fileLines.size();
			line = alg_list[i];
			pw.println(line);
			pw.flush();
			line = "Average, Best, Worst, Median, Std";
			pw.println(line);
			pw.flush();
			line = averages+", "+min+ ", "+max+", "+medians+", "+std;
			pw.println(line);
			pw.flush();
			
			 averages = 0;
	 		medians = 0;
			 min = 0;
			 max = 0;
			 std = 0;
		line="";
		}
		// the same for rel
		line = "\nrelative distance";
		pw.println(line);
		pw.flush();
		
		for (int i = 0; i< alg_list.length; i++) {
			filename = baseDir+"/closestItem_RefSet_"+alg_list[i]+"_rel_RS_statistics.txt";
			try {
				fileLines = (ArrayList<String>)Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			} catch (IOException e) {e.printStackTrace();}
			for (int j=0; j<fileLines.size();j++) {
				//			System.out.println("cells to stirng "+cell);
				st = new StringTokenizer(fileLines.get(j), ",");
				while (st.hasMoreTokens()) {
					averages = averages + (Double.parseDouble(((st.nextToken()).trim())));
					medians = medians  + (Double.parseDouble(((st.nextToken()).trim())));
					min = min + (Double.parseDouble(((st.nextToken()).trim())));
					max = max + (Double.parseDouble(((st.nextToken()).trim())));
					std = std + (Double.parseDouble(((st.nextToken()).trim())));
				}
				  
			}
			averages = averages/fileLines.size();
			medians = medians /fileLines.size();
			min = min/fileLines.size();
			max = max /fileLines.size();
			std = std /fileLines.size();
			line = alg_list[i];
			pw.println(line);
			pw.flush();
			line = "Average, Best, Worst, Median, Std";
			pw.println(line);
			pw.flush();
			line = averages+", "+min+ ", "+max+", "+medians+", "+std;
			pw.println(line);
			pw.flush();
			
			 averages = 0;
	 		medians = 0;
			 min = 0;
			 max = 0;
			 std = 0;
		line="";
		}
		
		
		pw.close();
		
		
	}
		
	public static String fileToPath(String filename) throws UnsupportedEncodingException {
		URL url = MainTestTopKClassRules.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
	}
	
	public static void writeSolutionsToFile(SolutionSet listOfSolutions, String filename) {

		FileWriter output = null;
		PrintWriter pw = null;
		try {
			output = new FileWriter(filename, false);
			pw = new PrintWriter(new BufferedWriter(output));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String line = "";

		for (int i = 0; i < listOfSolutions.size(); i++) {
			ArrayList<String> sources = new ArrayList<String>();
			ArrayList<String> targets= new ArrayList<String>();
			sources = ((Sources)(listOfSolutions.get(i).getDecisionVariables()[0]))._sourcesList;
			targets = ((Targets)(listOfSolutions.get(i).getDecisionVariables()[1]))._targetsList;
			for(int j=0; j< sources.size();j++){
				line = line + sources.get(j) + " ";
			}//end for sources
				
			for(int j=0; j< targets.size();j++){
				line = line + targets.get(j) + " "; //targets
				}
			pw.println(line);
			line = "";
		}
		pw.flush();
		pw.close();
	}

	public static void buildOntology() throws Exception {
	
		possibleSources =  new ArrayList<String>();
		possibleTargets =   new ArrayList<String>();
		possibleSourcePerType = new ArrayList<ArrayList<String>>();
		possibleTargetPerType = new ArrayList<ArrayList<String>>();
		
		ArrayList<String> sources = null;
		ArrayList<String> targets = null;
		try {
			sources = (ArrayList<String>) Files.readAllLines(ontologySourcesfile, StandardCharsets.UTF_8);
			targets = (ArrayList<String>) Files.readAllLines(ontologyTargetsfile, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (sources.isEmpty())
			throw new Exception("Error while reading the ontology source file: please verify that the format is correct and the list is non-empty"); 
		
		if (targets.isEmpty())
			throw new Exception("Error while reading the ontology source file: please verify that the format is correct and the list is non-empty"); 
		
		//The CHARACTER ":" is not allowed as Name. It is the special character to be used as separator
		
		numericTransactionsDB = new TreeMap<Integer, String>(); 
		int globalKey = 0;
		String expColumnType = "1"; 
		possibleSourcePerType.add(new ArrayList<String>());
		int j=0;
		for (int i = 0; i < sources.size(); i++) {
			System.out.println("Source Name: " + sources.get(i));
			String[] columnType = sources.get(i).split(":");
			globalKey++;
			numericTransactionsDB.put(globalKey, (new String(sources.get(i))));
			possibleSources.add(String.valueOf(globalKey));
			if(columnType[0].equals(expColumnType)) { 
				possibleSourcePerType.get(j).add(String.valueOf(globalKey));
			}else {
				possibleSourcePerType.add(new ArrayList<String>());
				j++;
				possibleSourcePerType.get(j).add(String.valueOf(globalKey));
				int temp = Integer.parseInt(expColumnType);
				temp++;
				expColumnType = String.valueOf(temp);
				groupLimitNumber.add(globalKey-1);
			}

			// *******SYMBOLIC NAME IS OVERWRITTEN BY NUMERIC NAME. IF NEEDED, THE SYMBOLIC
			// NAME IS IN THE MAP *****//
			}
		//System.out.println(" possible source per type "+possibleSourcePerType); 

		groupLimitNumber.add(globalKey);
		targetLimitNumber = globalKey;
				
		int k=0;
		possibleTargetPerType.add(new ArrayList<String>());
		// It's left as array of array so as to manage future extensions to multiple target columns (multiple effects)
		for (int i = 0; i < targets.size(); i++) {
			System.out.println("Target Name: " + targets.get(i));
			globalKey++;
			numericTransactionsDB.put(globalKey, (new String(targets.get(i))));
			possibleTargets.add(String.valueOf(globalKey));
			possibleTargetPerType.get(k).add(String.valueOf(globalKey));

			// *******SYMBOLIC NAME IS OVERWRITTEN BY NUMERIC NAME. IF NEEDED, THE SYMBOLIC
			// NAME IS IN THE MAP *****//
			}

		System.out.println("Possible sources per type "+possibleSourcePerType);
		System.out.println("Possible targets per type "+possibleTargetPerType);
		//groupLimitNumber.add(globalKey);
		PrintWriter map_file_pw = null;
		try {
			map_file_pw = new PrintWriter(new BufferedWriter(new FileWriter(baseDir + "/map.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Iterator it = numericTransactionsDB.entrySet().iterator();
		System.out.println("\n size: " + numericTransactionsDB.size());
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println("Key=value: " + pair.getKey() + " = " + pair.getValue());
			map_file_pw.println("Key=value: " + pair.getKey() + " = " + pair.getValue());
		}
		map_file_pw.flush();
		map_file_pw.close();
	}

	public static SolutionSet getReferenceSet(int starting_solution_rs, int number_of_solutions_rs)
			throws Exception {
		
		SolutionSet referenceSet; 
		//referenceSet = COPConfigurator.getInternalSolutions(starting_solution_rs, number_of_solutions_rs);
		referenceSet = COPConfigurator.getInternalSolutionsFromCSV(starting_solution_rs, number_of_solutions_rs);

		for (int i = 0; i <referenceSet.size();i++)  
			referenceSet.get(i).isInternal=false;
		
		return referenceSet; 
	}

	public static SolutionSet getInternalSolutions(int starting_row, int number_of_rows) throws Exception {
		// READ FROM EXCEL
		// String file = new
		String file = new String(COPConfigurator.inputKBFile);
		
		SolutionSet solSetString = new SolutionSet(number_of_rows);
		
		ArrayList<String> sources = new ArrayList<String>();
		ArrayList<String> targets  = new ArrayList<String>();
	
		try {
			OPCPackage pkg = OPCPackage.open(new File(file));
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;
			int rows = sheet.getPhysicalNumberOfRows();

			int cols = 0;
			int tmp = 0;
			// This trick ensures that we get the data properly even if it doesn't start
			// from first few rows
			for (int i = 0; i < 10 || i < rows; i++) {
				row = sheet.getRow(i);
				if (row != null) {
					tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					if (tmp > cols)
						cols = tmp;
				}
			}

			System.out.println("Number of variables: "+cols); 
			
			StringTokenizer st;
			rows = number_of_rows + starting_row; // ROWS TO READ

			// Only the last column is assumed to be the effect (the target).
			
			for (int r = starting_row; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					for (int c = 0; c<cols-1; c++) {
						cell = row.getCell(c);
						if (cell != null) {
							ArrayList<String> enumList = new ArrayList<String>();
					//		System.out.println("cell string "+cell.toString());
							st = new StringTokenizer(cell.toString(), "+");
							while (st.hasMoreTokens()) 
								enumList.add(((st.nextToken()).trim()).replace(".0", ""));
							sources.addAll(enumList);
						}
					}
					//read last column
					
					cell = row.getCell(cols-1);
					if (cell != null) {
						ArrayList<String> enumList = new ArrayList<String>();
						st = new StringTokenizer(cell.toString(), "+");
						while (st.hasMoreTokens()) 
							enumList.add(((st.nextToken()).trim()).replace(".0", ""));
						targets.addAll(enumList); 
					}
					
					//STRING SOLUTION CAUSE-EFFECT
					Variable[] variables_string = new Variable[2];
					//ArrayList<String> sol_string = new ArrayList<String>();
					variables_string[0] = new Sources(sources); 
					variables_string[1] =  new Targets(targets);
					Solution ind = new Solution(myProblemString, variables_string);
					
			//		printSolution(ind);
					
					ind.isInternal=true;
					solSetString.add(ind);
					
					/*** CLEAR ****/
					sources.clear();
					targets.clear();
					
				//	System.exit(0);
				}
				else {
					System.out.println("WARNING: the "+(r+1)+"-th row is empty");
				}
			}
		} catch (Exception ioe) {ioe.printStackTrace();}
		return solSetString;

	}
	
	public static SolutionSet getInternalSolutionsFromCSV(int starting_row, int number_of_rows) throws Exception {
		// READ FROM CSV
		
		// THE FIRST ROW IS ASSUMED TO BE THE COLUMN NAMES; IT IS SKIPPED. 
		
		ArrayList<String> fileLines = new ArrayList<String>();
		
		SolutionSet solSetString = new SolutionSet(number_of_rows);
		
		ArrayList<String> sources = new ArrayList<String>();
		ArrayList<String> targets  = new ArrayList<String>();
	
		try {
			
			fileLines = (ArrayList<String>)Files.readAllLines(inputKB_CSVFile, StandardCharsets.UTF_8);
			String cell;

			StringTokenizer st;
			// Only the last column is assumed to be the effect (the target).
			//System.out.println("fiel size "+number_of_rows);
			//System.out.println("fiel size "+starting_row);
			for (int r = starting_row; r < (starting_row+number_of_rows); r++) {
				String row = fileLines.get(r);
				if (row != null) {
					String[] cells = row.split(","); 
				//	System.out.println("cells "+cells[0]);
					for (int c = 0; c<cells.length-1; c++) {
						cell = cells[c]; 
						
						if (cell != null || cell!="?") {  //or any character we want to represent as "null"
						
							ArrayList<String> enumList = new ArrayList<String>();
				//			System.out.println("cells to stirng "+cell);
							st = new StringTokenizer(cell.toString(), "+");
							if(unique) {
							while (st.hasMoreTokens()) 
								enumList.add(((st.nextToken()).trim()).replace(".0", ""));
							}
							else {
								while (st.hasMoreTokens()) {
									String s = (st.nextToken()).trim().replace(".0", "");
									for (int i= 0; i< COPConfigurator.possibleSourcePerType.get(c).size();i++){
										String[] temp = (COPConfigurator.numericTransactionsDB.get(Integer.parseInt(COPConfigurator.possibleSourcePerType.get(c).get(i)))).split(":");
										if (temp[temp.length-1].equals(s)) {
											enumList.add(COPConfigurator.possibleSourcePerType.get(c).get(i));
											break;
										}
									}
								}
							}
							sources.addAll(enumList);
						}
					}
					//read last column
					cell = cells[cells.length-1];
					if (cell != null) {
						ArrayList<String> enumList = new ArrayList<String>();
						st = new StringTokenizer(cell.toString(), "+");
						
						if(unique) {
							while (st.hasMoreTokens()) 
								enumList.add(((st.nextToken()).trim()).replace(".0", ""));
						}
						else {
							while (st.hasMoreTokens()) {
								String s = (st.nextToken()).trim().replace(".0", "");
								for (int i= 0; i< COPConfigurator.possibleTargetPerType.get(0).size();i++){
									String[] temp = (COPConfigurator.numericTransactionsDB.get(Integer.parseInt(COPConfigurator.possibleTargetPerType.get(0).get(i)))).split(":");
									if (temp[temp.length-1].equals(s)) {
										enumList.add(COPConfigurator.possibleTargetPerType.get(0).get(i));
										break;
									}
								}
							}
						}
						targets.addAll(enumList); 
					}
					
					//STRING SOLUTION CAUSE-EFFECT
					Variable[] variables_string = new Variable[2];
					//ArrayList<String> sol_string = new ArrayList<String>();
					variables_string[0] = new Sources(sources); 
					variables_string[1] =  new Targets(targets);
					Solution ind = new Solution(myProblemString, variables_string);
					
				//	printSolution(ind);
					
					ind.isInternal=true;
					solSetString.add(ind);
					
					/*** CLEAR ****/
					sources.clear();
					targets.clear();
				}
				else {
					System.out.println("WARNING: the "+(r+1)+"-th row is empty");
				}
			//System.exit(0);
			}
		} catch (Exception ioe) {System.out.println("Error while reading from the CSV. Please, verify that data starts at the second row, are numerical values, and use the ',' separator between columns and the '+' separator multiple values in a single cell"); 
		ioe.printStackTrace();}
		return solSetString;

	}
		
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = ran.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public static String getNameFromKey(String input_key) {
		// TODO Auto-generated method stub
		Iterator it = numericTransactionsDB.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if ((Integer) pair.getKey() == Integer.parseInt(input_key))
				return (String) pair.getValue();
		}
		return null;
	}

	public static ArrayList<String> getKeyContainingName(String name) {
		
		Iterator it = numericTransactionsDB.entrySet().iterator();
		ArrayList<String> list = new ArrayList<String>(); 
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((String) pair.getValue()).contains(name))
				list.add( String.valueOf((Integer)pair.getKey()));
		}
		return list;
	}
	
	
}