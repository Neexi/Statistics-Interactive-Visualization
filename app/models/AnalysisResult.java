package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnalysisResult {

    public enum PatientType {

        VIOLATED("Patients that violated by taking B and I together."), //
        VALID_NO_COMED("Patients that did not violate, because they never took B and I together."), //
        VALID_BI_SWITCH("Patients that did not violate, because they switched from B to I."), //
        VALID_IB_SWITCH("Patients that did not violate, because they switched from I to B."), //
        VALID_I_TRIAL("Patients that did not violate, because they simply trialled I during B."), //
        VALID_B_TRIAL("Patients that did not violate, because they simply trialled B during I.");

        public String name;

        PatientType(String name) {
            this.name = name;
        }
    }

    public Map<PatientType, Integer> patients = new LinkedHashMap<PatientType, Integer>();
    //TODO : use database instead to store this information?
    //Doc URL : https://www.playframework.com/documentation/1.3.x/configuration#dbconf
    
    //Implementation with map
    //public Map<String, Map<Integer, List<PurchaseRecord>>> patientsList =  new HashMap<String, Map<Integer, List<PurchaseRecord>>>();
    //Implementation with list
    public Map<PatientType, List<List<PurchaseRecord>>> patientsList =  new HashMap<PatientType, List<List<PurchaseRecord>>>();
    public int numRecords;

    public static AnalysisResult performBIAnalysis() throws IOException {
        List<PurchaseRecord> purchaseRecords = PurchaseRecord.loadFromFile();
        RecordHandler rh = new RecordHandler(purchaseRecords);
        List<List<PurchaseRecord>> purchaseRecordsByDay = 
        		rh.getRecordsByDay(purchaseRecords); //TODO : Maybe remove this later
        List<List<PurchaseRecord>> purchaseRecordsByPatientID = 
        		rh.getRecordsByPatientID(purchaseRecordsByDay);
        
        
        //Instantiate the map
        AnalysisResult result = new AnalysisResult();
        result.numRecords = purchaseRecords.size();
        result.patients.put(PatientType.VIOLATED, 0);
        result.patients.put(PatientType.VALID_NO_COMED, 0);
        result.patients.put(PatientType.VALID_BI_SWITCH, 0);
        result.patients.put(PatientType.VALID_IB_SWITCH, 0);
        result.patients.put(PatientType.VALID_I_TRIAL, 0);
        result.patients.put(PatientType.VALID_B_TRIAL, 0);
        result.patientsList.put(PatientType.VIOLATED, new LinkedList<List<PurchaseRecord>>());
        result.patientsList.put(PatientType.VALID_NO_COMED, new LinkedList<List<PurchaseRecord>>());
        result.patientsList.put(PatientType.VALID_BI_SWITCH, new LinkedList<List<PurchaseRecord>>());
        result.patientsList.put(PatientType.VALID_IB_SWITCH, new LinkedList<List<PurchaseRecord>>());
        result.patientsList.put(PatientType.VALID_I_TRIAL, new LinkedList<List<PurchaseRecord>>());
        result.patientsList.put(PatientType.VALID_B_TRIAL, new LinkedList<List<PurchaseRecord>>());
        
        for(int patID = 0; patID < rh.numPatient; patID++) {
        	List<PurchaseRecord> patientRecords = purchaseRecordsByPatientID.get(patID);
        	if(patientRecords.size() == 0) continue;
        	PatientType classification = PatientClassification.getClassification(patientRecords);
        	result.patients.put(classification, result.patients.get(classification) + 1);
        	result.patientsList.get(classification).add(patientRecords);
        }
        /*
        AnalysisResult result = new AnalysisResult();
        result.patients.put(PatientType.VIOLATED, 200);
        result.patients.put(PatientType.VALID_NO_COMED, 500);
        result.patients.put(PatientType.VALID_BI_SWITCH, 100);
        result.patients.put(PatientType.VALID_IB_SWITCH, 100);
        result.patients.put(PatientType.VALID_I_TRIAL, 50);
        result.patients.put(PatientType.VALID_B_TRIAL, 50);*/
        return result;
    }
    
    public static int getNumRecords(AnalysisResult result) {
    	return result.numRecords;
    }
    
    public static List<List<PurchaseRecord>> getPatientsListByTypeID(AnalysisResult result, int typeID) {
    	List<List<PurchaseRecord>> res;
    	switch(typeID) {
    	    case(0) : 
    	    	res = result.patientsList.get(PatientType.VIOLATED);
    	        break;
    	    case(1) : 
    	    	res = result.patientsList.get(PatientType.VALID_NO_COMED);
    	        break;
    	    case(2) : 
    	    	res = result.patientsList.get(PatientType.VALID_BI_SWITCH);
    	        break;
    	    case(3) : 
    	    	res = result.patientsList.get(PatientType.VALID_IB_SWITCH);
    	        break;
    	    case(4) : 
    	    	res = result.patientsList.get(PatientType.VALID_I_TRIAL);
    	        break;
    	    case(5) : 
    	    	res = result.patientsList.get(PatientType.VALID_B_TRIAL);
    	        break;
    	    default : 
    	    	res = null;
    	}
    	return res;

    }
}