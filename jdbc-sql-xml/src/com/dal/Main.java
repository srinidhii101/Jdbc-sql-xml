package com.dal;

import com.dal.summaryreport.SummaryReportGenerator;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {
            if (args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty())
                throw new Exception("Incorrect Arguments");
            Date startDate = new Date(args[0]);
            Date endDate = new Date(args[1]);
            String outputFile = args[2];

            SummaryReportGenerator summaryReportGenerator = new SummaryReportGenerator();

            summaryReportGenerator.generate(startDate, endDate, outputFile);

        } catch (Exception ex) {
            System.out.println("Incorrect Arguments");
        }
    }
}
