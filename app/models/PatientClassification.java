package models;

import java.util.List;

import models.AnalysisResult.PatientType;

/*
 * Analyzing the group that single patient belong to using 
 * Assuming single threaded
 * lock need to be implemented during analysis process otherwise
 */
public class PatientClassification {
	
	//More detail for variables can be read at public/Classification.pdf
	private static int T; //Total number of medicine taken
	private static double V; //Number of detected violation / T
	private static double S; //Number of times the patient switched medicine / T (switching tendency)
	private static double R_I; //Number of times medicine I was taken / T (medicine ratio)
	private static String M_0; //First medicine taken by the patient
	
	private static boolean analyzed;
	
	//Constant
	private static final String medI = "I";
	private static final String medB = "B";
	private static final int iDuration = 90;
	private static final int bDuration = 30;
	
	//Currently the decision tree limit is a static constant for all decisions 
	//TODO : Improve accuracy with labeled training data
	private static final double cutoff = 0.5;
	//Weighting for decision tree, for now, let's leave it as 1 since there is no training data
	private static final int vWeight = 1;
	private static final int sWeight = 1;
	private static final int rIWeight = 1;
	
	private PatientClassification() {
		
	}
	
	public static PatientType getClassification(List<PurchaseRecord> patientRecords) {
		analyzed = false;
		analyze(patientRecords);
		return classify();
	}
	
	/*
	 * Analyze the patient records to get the necessary data
	 * Assumption:
	 * Medicine can stack, 
	 * i.e. if patient buy a medicine before the previous medicine run out, the remaining duration is added into calculation
	 */
	private static void analyze(List<PurchaseRecord> patientRecords) {
		T = patientRecords.size();
		if(T == 0) return;
		M_0 = patientRecords.get(0).medication;
		
		int curBDuration = 0;
		int curIDuration = 0;
		int vCount = 0;
		int sCount = 0;
		int iCount = 0;
		
		int lastDay = 0;
		String lastMed = M_0;
		
		for(PurchaseRecord r : patientRecords) {
			if(r.medication.equals(medI)) {
				curIDuration = Math.max(0, curIDuration - (r.day - lastDay));
				curBDuration = Math.max(0, curBDuration - (r.day - lastDay));
				vCount = (curBDuration > 0) ? vCount + 1 : vCount;
				sCount = (lastMed.equals(medB)) ? sCount + 1 : sCount;
				curIDuration += iDuration;
				iCount++;
			} else {
				curIDuration = Math.max(0, curIDuration - (r.day - lastDay));
				curBDuration = Math.max(0, curBDuration - (r.day - lastDay));
				vCount = (curIDuration > 0) ? vCount + 1 : vCount;
				sCount = (lastMed.equals(medI)) ? sCount + 1 : sCount;
				curBDuration += bDuration;
			}
			lastDay = r.day;
			lastMed = r.medication;
		}
		
		V = (double)vCount / (double)T;
		S = (double)sCount / (double)T;
		R_I = (double)iCount / (double)T;
		analyzed = true;
	}
	
	//-------------------------------------------------------------------------------------------------------------
	//Decision tree function
	
	private static PatientType classify() {
		return analyzed ? vCheck() : PatientType.VALID_NO_COMED;
	}
	
	private static PatientType vCheck() {
		double limit = cutoff; //Can be changed to Math.Random()
		if(V == 0) return PatientType.VALID_NO_COMED;
		else {
			return (limit > V * vWeight) ? rICheck() : PatientType.VIOLATED; 
		}
	}
	
	private static PatientType rICheck() {
		double limit = cutoff / 2; //Can be changed to Math.Random()
		if(R_I < 0.5) {
			return (limit > R_I * rIWeight) ? PatientType.VALID_I_TRIAL : sCheck();
		} else {
			return (limit > (1 - R_I) * rIWeight) ? PatientType.VALID_B_TRIAL : sCheck();
		}
	}
	
	private static PatientType sCheck() {
		double limit = cutoff; //Can be changed to Math.Random()
		return (limit > S * sWeight) ? m0Check() : PatientType.VIOLATED; 
	}
	
	private static PatientType m0Check() {
		return (M_0.equals(medI)) ? PatientType.VALID_IB_SWITCH : PatientType.VALID_BI_SWITCH;
	}
}
