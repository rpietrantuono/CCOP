# CCOP Repository
## Source

This repository contains the material of the following work: 

"Roberto Pietrantuono, Automated Hypotheses Generation via Combinatorial Causal Optimization, submitted for review to the 2021 IEEE Congress on Evolutionary Computation."

## Description
The repository contains the artefacts required to run four MOEAs (csNSGA-II, csOMOPSO, csSMSEMOA, csSPEA2) implemented to solve Combinatorial Causal Optimization Problem (CCOP)  defined in the above-mentioned work, on four real-world benchmarks. The code is based on the jMetal framework (https://github.com/jMetal), a Java framework to develop and experiment MOEAs; therefore, beside allowing for replicating the results of the paper, it allows to easily implement new MOEAs exploiting the jMetal facilities. 

The artefacts include: 
- CCOP.jar. The executable JAR file to run the four MOEAs on a benchmark problem given as input. 
Usage: 
java -jar CCOP.jar <configuration.txt> 

"configuration.txt" is a file in which the user can set all the parameters required for the run. Examples are in the BENCHMARKS folder. To use it in a benchmark different from the experimented ones, just copy and customise one of the available files from the BENCHMARKS folder. In that file, the text after the "#" character are comments describing the meaning of the parameter. 

- CCOP-SOURCE. It contains the source code. To implement new MOEAs, just follows  the documentation of jMetal, setting as "problem" a "COPProblem". 

- BENCHMARKS. It contains the four benchmarks problems. For each of them, there is: the configuration.txt file mentioned above; the dataset (e.g., medical.csv for CCOP1); the OntologySource.txt and OntologyTargets.txt files that enumerates the variables and their possible values (in this format: 1:1, 1:2, ...2:1, 2:2, ..., meaning: variable1: value1, variable1:value2). It is also reported a "description" folder that has the original dataset and a description of the attributes, which we used as "variables:values".  

- RESULTS. It contains the results obtained for the four benchmarks described in the paper. There is one filder per benchmark problem. Results are in the form of text files reporting the distances obtained for each algorithm with respect to the test dataset (*Ref*.txt files) as well as with respect to the knoweldge base (*KB*.txt files); the folder  MyStandardCOP contains the results as stored by jMetal, hence with the HV and IGD indicators, and the possibiltiy to do Boxplots as well as Latex Tables reporting the resutls. 
