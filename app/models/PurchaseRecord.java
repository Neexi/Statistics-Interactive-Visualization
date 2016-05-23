package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class PurchaseRecord {

    private static final String MOCK_DATA_PATH = "./public/data.csv";

    public int day;
    public int dayEnd; //End day of the medication, proper value will be assigned in patient analyzation
    public String medication;
    public int patientId;
    public String recordTag; //Tag for timeline to distinguish special record
	

    public PurchaseRecord(String line) {
        String fields[] = line.split(",");
        day = Integer.parseInt(fields[0]);
        dayEnd = day; //Give it value for now
        medication = fields[1];
        patientId = Integer.parseInt(fields[2]);
        recordTag = medication;
    }

    public PurchaseRecord(int day, String medication, int patientId) {
        this.day = day;
        this.dayEnd = day; //Give it value for now
        this.medication = medication;
        this.patientId = patientId;
        this.recordTag = medication;
    }

    public static List<PurchaseRecord> loadFromFile() throws IOException {
        List<PurchaseRecord> records = new ArrayList<PurchaseRecord>();
        for (String line : FileUtils.readLines(new File(MOCK_DATA_PATH))) {
            records.add(new PurchaseRecord(line));
        }
        return records;
    }

    // you can ignore, this just generates the data.csv file that was provided
    // with the exercise
    public static void mockData() throws IOException {
        List<String> data = new ArrayList<String>();
        for (int patientId = 0; patientId < 1000; patientId++) {
            boolean takesB = Math.random() >= 0.5;
            boolean takesI = Math.random() >= 0.5;
            int numberOfB = (int) (takesB ? Math.random() * 12 : 0);
            int numberOfI = (int) (takesI ? Math.random() * 4 : 0);
            for (int i = 0; i < numberOfB; i++) {
                int day = (int) (Math.random() * 365);
                data.add(new PurchaseRecord(day, "B", patientId).toString());
            }
            for (int i = 0; i < numberOfI; i++) {
                int day = (int) (Math.random() * 365);
                data.add(new PurchaseRecord(day, "I", patientId).toString());
            }
        }
        Collections.sort(data);
        FileUtils.writeLines(new File(MOCK_DATA_PATH), data);
    }

    public String toString() {
        return day + "," + medication + "," + patientId;
    }
}