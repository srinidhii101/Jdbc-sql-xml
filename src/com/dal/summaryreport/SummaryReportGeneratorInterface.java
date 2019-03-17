package com.dal.summaryreport;

import java.sql.SQLException;
import java.util.Date;

public interface SummaryReportGeneratorInterface {

    void generate(Date startDate, Date endDate, String outputFile) throws SQLException, ClassNotFoundException;

}
