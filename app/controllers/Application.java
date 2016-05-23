package controllers;

import java.io.IOException;
import java.util.List;

import models.AnalysisResult;
import models.PurchaseRecord;
import play.mvc.Controller;

public class Application extends Controller {
	
    public static void index() throws IOException {
    	AnalysisResult result = AnalysisResult.performBIAnalysis();
        render(result);
    }
    
    public static void patientsList(String id) throws IOException {
    	AnalysisResult result = AnalysisResult.performBIAnalysis();
    	List<List<PurchaseRecord>> patientsRecordList = 
    			AnalysisResult.getPatientsListByTypeID(result, Integer.parseInt(id));
    	int recordSize = patientsRecordList.size();
    	String typeString = AnalysisResult.getPatientsTypeStringByTypeID(Integer.parseInt(id));
        render(result, patientsRecordList, recordSize, typeString);
    }

    // you can ignore, this just generates the data.csv file that was provided
    // with the exercise
    public static void mockData() throws IOException {
        PurchaseRecord.mockData();
        render();
    }
}