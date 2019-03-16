package com.dal.summaryreport;

import com.dal.jdbc.JDBCConnector;
import com.dal.jdbc.JdbcConfig;

import java.util.Date;

public class SummaryReportGenerator implements SummaryReportGeneratorInterface {

    @Override
    public void generate(Date startDate, Date endDate, String outputFile) {

        JdbcConfig jdbcConfig = new JdbcConfig();
        JDBCConnector jdbcConnector = new JDBCConnector(jdbcConfig);

    }
}
