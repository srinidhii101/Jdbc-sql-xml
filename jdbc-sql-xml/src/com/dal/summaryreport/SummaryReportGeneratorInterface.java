package com.dal.summaryreport;

import java.util.Date;

public interface SummaryReportGeneratorInterface {

    void generate(Date startDate, Date endDate, String outputFile);

}
