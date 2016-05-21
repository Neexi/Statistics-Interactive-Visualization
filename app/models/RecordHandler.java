package models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/*
 * Handle original list of records to better format
 */
public class RecordHandler {
	
	public int numDay;
	public int numPatient;
	
	public RecordHandler(List<PurchaseRecord> purchaseRecords) {
		int maxDay = 0;
		int maxPatientID = 0;
		for(PurchaseRecord pr : purchaseRecords) {
			maxDay = (maxDay < pr.day) ? pr.day : maxDay;
			maxPatientID = (maxPatientID < pr.patientId) ? pr.patientId : maxPatientID;
		}
		numDay = maxDay + 1;
		numPatient = maxPatientID + 1;
	}
	
	/*
	 * Create list of purchase records indexed by day
	 */
	public List<List<PurchaseRecord>> getRecordsByDay(List<PurchaseRecord> purchaseRecords) {
		List<List<PurchaseRecord>> purchaseRecordsByDay = new ArrayList<List<PurchaseRecord>>(numDay);
		for(int day = 0; day < numDay; day++) {
			purchaseRecordsByDay.add(new LinkedList<PurchaseRecord>());
		}
		for(PurchaseRecord pr : purchaseRecords) {
			if(pr.day > numDay - 1 || pr.patientId > numPatient - 1) continue; //do not process bad record
			purchaseRecordsByDay.get(pr.day).add(pr);
		}
		return purchaseRecordsByDay;
	}
	
	/*
	 * Create list of purchase records indexed by patient ID
	 */
	public List<List<PurchaseRecord>> getRecordsByPatientID(List<List<PurchaseRecord>> purchaseRecordsByDay) {
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
