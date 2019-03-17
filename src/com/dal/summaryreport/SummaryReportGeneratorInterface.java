package com.dal.summaryreport;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

public interface SummaryReportGeneratorInterface {

    void generate(Date startDate, Date endDate, String outputFile) throws SQLException, ClassNotFoundException, ParserConfigurationException, IOException;


}
