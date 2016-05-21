package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/*
 * Handle original list of records to better format
 */
public class RecordHandler {
	
	//Based on information given by the CSV generator
	public static final int dayOfYear = 365;
	public static final int numPatient = 1000;
	
	private RecordHandler() {
		
	}
	
	/*
	 * Create list of purchase records indexed by day
	 */
	public static List<List<PurchaseRecord>> getRecordsByDay(List<PurchaseRecord> purchaseRecords) {
		List<List<PurchaseRecord>> purchaseRecordsByDay = new ArrayList<List<PurchaseRecord>>(dayOfYear);
		for(int day = 0; day < dayOfYear; day++) {
			purchaseRecordsByDay.add(new LinkedList<PurchaseRecord>());
		}
		for(PurchaseRecord pr : purchaseRecords) {
			if(pr.day > dayOfYear - 1 || pr.patientId > numPatient - 1) continue; //do not process bad record
			purchaseRecordsByDay.get(pr.day).add(pr);
		}
		return purchaseRecordsByDay;
	}
	
	/*
	 * Create list of purchase records indexed by patient ID
	 */
	public static List<List<PurchaseRecord>> getRecordsByPatientID(List<List<PurchaseRecord>> purchaseRecordsByDay) {
		List<List<PurchaseRecord>> purchaseRecordsByPatientID = new ArrayList<List<PurchaseRecord>>(numPatient);
		for(int pat = 0; pat < numPatient; pat++) {
			purchaseRecordsByPatientID.add(new LinkedList<PurchaseRecord>());
		}
		for(List<PurchaseRecord> prd : purchaseRecordsByDay) {
			for(PurchaseRecord pr : prd) {
				purchaseRecordsByPatientID.get(pr.patientId).add(pr);
			}
		}
		return purchaseRecordsByPatientID;
	}
}
