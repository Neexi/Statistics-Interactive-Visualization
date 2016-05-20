package controllers;

import java.io.IOException;

import models.AnalysisResult;
import models.PurchaseRecord;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() throws IOException {
        AnalysisResult result = AnalysisResult.performBIAnalysis();
        render(result);
    }

    // you can ignore, this just generates the data.csv file that was provided
    // with the exercise
    public static void mockData() throws IOException {
        PurchaseRecord.mockData();
        render();
    }
}