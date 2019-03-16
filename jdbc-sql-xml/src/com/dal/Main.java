package com.dal;

import com.dal.jdbc.GetProperties;
import com.dal.jdbc.JDBCConnector;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {
            if (args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty())
                throw new Exception("Incorrect Arguments");
            Date startDate = new Date(args[1]);
            Date endDate = new Date(args[2]);
            String outputFile = args[3];

            JDBCConnector jdbcConnector = new JDBCConnector();
            GetProperties getProperties = new GetProperties();

        } catch (Exception ex) {
            System.out.println("Incorrect Arguments");
        }
    }
}
