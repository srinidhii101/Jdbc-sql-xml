package com.dal.summaryreport;

import com.dal.jdbc.JDBCConnector;
import com.dal.jdbc.JdbcConfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryReportGenerator implements SummaryReportGeneratorInterface {

    @Override
    public void generate(Date start, Date end, String outputFile) throws SQLException, ClassNotFoundException {
    	
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
    	
    	String startDate = simpleDateFormat.format(start);
    	String endDate = simpleDateFormat.format(end);
    	
        JdbcConfig jdbcConfig = new JdbcConfig();
        JDBCConnector jdbcConnector = new JDBCConnector();
        Connection connection = jdbcConnector.connectionProvider(jdbcConfig);

        // Use Database Statement
        Statement useDatabaseStatement = connection.createStatement();
        useDatabaseStatement.executeQuery("use " + jdbcConfig.getDatabase() + ";");

        // Customer Information Report
        String customerInfoQuery = "Select customers.ContactName, " +
                "customers.Address, count(*) as \"No Of Orders\", " +
                "sum(orderdetails.UnitPrice * orderdetails.Quantity - orderdetails.Discount) as \"Sum\"\n" +
                "from orders, orderdetails, customers\n" +
                "where orders.OrderID = orderdetails.OrderID\n" +
                "and orders.CustomerID = customers.CustomerID\n" +
                "and orders.OrderDate between " + startDate.toString() + " and " + endDate.toString() + "\n" +
                "group by customers.CustomerID\n" +
                "having count(*) > 0;";

        System.out.println(customerInfoQuery);
        Statement customerInformationStatement = connection.createStatement();
        ResultSet customerInformationResults = customerInformationStatement.executeQuery(customerInfoQuery);

        String productInfoQuery = "Select categories.CategoryName, products.ProductName, suppliers.ContactName, sum(orderdetails.Quantity) as \"units_sold\", sum(orderdetails.Quantity * orderdetails.UnitPrice - orderdetails.Discount) as \"sale value\"\n" +
                "from categories, products, suppliers, orderdetails, orders\n" +
                "where categories.CategoryID = products.CategoryID\n" +
                "and suppliers.SupplierID = products.SupplierID\n" +
                "and orderdetails.OrderID = orders.OrderID\n" +
                "and orderdetails.ProductID = products.ProductID\n" +
                "and orders.OrderDate between " + startDate.toString() + " and " + endDate.toString() + "\n" +
                "group by categories.CategoryName, products.ProductName, suppliers.ContactName, orderdetails.ProductID\n" +
                "having count(products.ProductID) > 0";

        Statement productInformationStatement = connection.createStatement();
        ResultSet productInformationResults = productInformationStatement.executeQuery(productInfoQuery);

        String supplierInfoQuery = "Select suppliers.ContactName, suppliers.Address, count(products.ProductID) as \"num_of_products\", sum(orderdetails.Quantity * orderdetails.UnitPrice - orderdetails.Discount) as \"product value\"\n" +
                "        from products, suppliers, orderdetails, orders\n" +
                "        where suppliers.SupplierID = products.SupplierID\n" +
                "        and orderdetails.OrderID = orders.OrderID\n" +
                "        and orderdetails.ProductID = products.ProductID\n" +
                "and orders.OrderDate between " + startDate.toString() + " and " + endDate.toString() + "\n" +
                "        group by products.ProductName, suppliers.ContactName, orderdetails.ProductID\n" +
                "        having count(suppliers.SupplierID) > 0";

        Statement supplierInformationStatement = connection.createStatement();
        ResultSet supplierInformationResults = supplierInformationStatement.executeQuery(supplierInfoQuery);
    }
}
