write("", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex",append=FALSE)
resultDirectory<-"/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/data"
latexHeader <- function() {
  write("\\documentclass{article}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\maketitle", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\caption{", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(problem, "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(problem, "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)

  write("\\centering", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(tabularString, "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\hline ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  write("\\end{table}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("COPProblem") 
algorithmList <-c("csNSGA-II", "csMOPSO", "csSMSEMOA", "csSPEA2") 
tabularString <-c("lccc") 
latexTableFirstLine <-c("\\hline  & csMOPSO & csSMSEMOA & csSPEA2\\\\ ") 
indicator<-"IGD"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "csSPEA2") {
      write(i , "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
      write(" & ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
          }
          if (j != "csSPEA2") {
            write(" & ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{1}{c|}{csMOPSO} & \\multicolumn{1}{c|}{csSMSEMOA} & \\multicolumn{1}{c|}{csSPEA2} \\\\") 

# Step 3. Problem loop 
latexTableHeader("COPProblem ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "csSPEA2") {
    write(i , "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
    write(" & ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
          } 
          if (problem == "COPProblem") {
            if (j == "csSPEA2") {
              write(" \\\\ ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "/Users/robertopietrantuono/Dropbox/Papers/in_preparazione/TEVC-LEARNING/CCOP/MEDICAL/MyStandardStudyCOP-2000-3min/MyStandardStudyCOP/R/COPProblem.IGD.Wilcox.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

