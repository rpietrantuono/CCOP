postscript("COPProblem.HV.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
filecsNSGA-II<-paste(resultDirectory, "csNSGA-II", sep="/")
filecsNSGA-II<-paste(filecsNSGA-II, problem, sep="/")
filecsNSGA-II<-paste(filecsNSGA-II, indicator, sep="/")
csNSGA-II<-scan(filecsNSGA-II)

filecsMOPSO<-paste(resultDirectory, "csMOPSO", sep="/")
filecsMOPSO<-paste(filecsMOPSO, problem, sep="/")
filecsMOPSO<-paste(filecsMOPSO, indicator, sep="/")
csMOPSO<-scan(filecsMOPSO)

filecsSMSEMOA<-paste(resultDirectory, "csSMSEMOA", sep="/")
filecsSMSEMOA<-paste(filecsSMSEMOA, problem, sep="/")
filecsSMSEMOA<-paste(filecsSMSEMOA, indicator, sep="/")
csSMSEMOA<-scan(filecsSMSEMOA)

filecsSPEA2<-paste(resultDirectory, "csSPEA2", sep="/")
filecsSPEA2<-paste(filecsSPEA2, problem, sep="/")
filecsSPEA2<-paste(filecsSPEA2, indicator, sep="/")
csSPEA2<-scan(filecsSPEA2)

algs<-c("csNSGA-II","csMOPSO","csSMSEMOA","csSPEA2")
boxplot(csNSGA-II,csMOPSO,csSMSEMOA,csSPEA2,names=algs, notch = TRUE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(1,1))
indicator<-"HV"
qIndicator(indicator, "COPProblem")
