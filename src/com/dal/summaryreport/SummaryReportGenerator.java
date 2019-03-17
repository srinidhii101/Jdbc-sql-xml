package com.dal.summaryreport;

import com.dal.jdbc.JDBCConnector;

import com.dal.jdbc.JdbcConfig;

import java.sql.Connection;
import com.dal.jdbc.JDBCConnector;
import com.dal.jdbc.JdbcConfig;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryReportGenerator implements SummaryReportGeneratorInterface {

    @Override
    public void generate(Date start, Date end, String outputFile)throws SQLException, ClassNotFoundException, ParserConfigurationException, IOException 
		{
			System.out.println(start.toString());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
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
		            "customers.Address, count(*) as \"NoOfOrders\", " +
		            "sum(orderdetails.UnitPrice * orderdetails.Quantity - orderdetails.Discount) as \"Sum\"\n" +
		            "from orders, orderdetails, customers\n" +
		            "where orders.OrderID = orderdetails.OrderID\n" +
		            "and orders.CustomerID = customers.CustomerID\n" +
		            "and orders.OrderDate between '" + startDate.toString() + "' and '" + endDate.toString() + "'\n" +
		            "group by customers.CustomerID\n" +
		            "having count(*) > 0;";

		    System.out.println(customerInfoQuery);
		    Statement customerInformationStatement = connection.createStatement();
		    ResultSet customerInformationResults = customerInformationStatement.executeQuery(customerInfoQuery);

		    String productInfoQuery = "Select categories.CategoryName, products.ProductName, suppliers.ContactName, sum(orderdetails.Quantity) as \"units_sold\", sum(orderdetails.Quantity * orderdetails.UnitPrice - orderdetails.Discount) as \"sale_value\"\n" +
		            "from categories, products, suppliers, orderdetails, orders\n" +
		            "where categories.CategoryID = products.CategoryID\n" +
		            "and suppliers.SupplierID = products.SupplierID\n" +
		            "and orderdetails.OrderID = orders.OrderID\n" +
		            "and orderdetails.ProductID = products.ProductID\n" +
		            "and orders.OrderDate between '" + startDate.toString() + "' and '" + endDate.toString() + "'\n" +
		            "group by categories.CategoryName, products.ProductName, suppliers.ContactName, orderdetails.ProductID\n" +
		            "having count(products.ProductID) > 0";

		    Statement productInformationStatement = connection.createStatement();
		    ResultSet productInformationResults = productInformationStatement.executeQuery(productInfoQuery);

		    String supplierInfoQuery = "Select suppliers.ContactName, suppliers.Address, count(products.ProductID) as \"num_of_products\", sum(orderdetails.Quantity * orderdetails.UnitPrice - orderdetails.Discount) as \"product_value\"\n" +
		            "        from products, suppliers, orderdetails, orders\n" +
		            "        where suppliers.SupplierID = products.SupplierID\n" +
		            "        and orderdetails.OrderID = orders.OrderID\n" +
		            "        and orderdetails.ProductID = products.ProductID\n" +
		            "and orders.OrderDate between '" + startDate.toString() + "' and '" + endDate.toString() + "'\n" +
		            "        group by products.ProductName, suppliers.ContactName, orderdetails.ProductID\n" +
		            "        having count(suppliers.SupplierID) > 0";

		    Statement supplierInformationStatement = connection.createStatement();
		    ResultSet supplierInformationResults = supplierInformationStatement.executeQuery(supplierInfoQuery);
		    
		    Document document = generateDocument(customerInformationResults, productInformationResults,
		    		supplierInformationResults, startDate, endDate);
		    System.out.println(serialize(document));
		}
 
    
    public Document generateDocument(ResultSet customerInfo, ResultSet productInfo, ResultSet supplierInfo, String startDate, String endDate) throws ParserConfigurationException, SQLException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        Element results = document.createElement("year_end_summary");
        document.appendChild(results);

        Element year = document.createElement("year");
        Element start = document.createElement("start_date");
        Element end  = document.createElement("end_date");
        start.appendChild(document.createTextNode(startDate));
        end.appendChild(document.createTextNode(endDate));
        
        year.appendChild(start);
        year.appendChild(end);
        results.appendChild(year);
        
        Element customer_list = document.createElement("customer_list");
     
        ResultSetMetaData cresultSetMetaData = customerInfo.getMetaData();
        int ccolumnCount = cresultSetMetaData.getColumnCount();

        while (customerInfo.next()) {
            Element row = document.createElement("customer");
            customer_list.appendChild(row);

            for (int i = 1; i <= ccolumnCount; i++) {
                String columnName = cresultSetMetaData.getColumnName(i);
                Object value = customerInfo.getObject(i);

                Element node = document.createElement(columnName);
                node.appendChild(document.createTextNode(value.toString()));
                row.appendChild(node);
            }
        }
        

        
        Element product_list = document.createElement("product_list");
        
        ResultSetMetaData presultSetMetaData = productInfo.getMetaData();
        int pcolumnCount = presultSetMetaData.getColumnCount();

        while (productInfo.next()) {
            Element row = document.createElement("product");
            product_list.appendChild(row);

            for (int i = 1; i <= pcolumnCount; i++) {
                String columnName = presultSetMetaData.getColumnName(i);
                Object value = productInfo.getObject(i);

                Element node = document.createElement(columnName);
                node.appendChild(document.createTextNode(value.toString()));
                row.appendChild(node);
            }
        }
       
        Element supplier_list = document.createElement("supplier_list");
        
        ResultSetMetaData sresultSetMetaData = supplierInfo.getMetaData();
        int scolumnCount = sresultSetMetaData.getColumnCount();

        while (supplierInfo.next()) {
            Element row = document.createElement("supplier");
            supplier_list.appendChild(row);

            for (int i = 1; i <=scolumnCount; i++) {
                String columnName = sresultSetMetaData.getColumnName(i);
                Object value = supplierInfo.getObject(i);

                Element node = document.createElement(columnName);
                node.appendChild(document.createTextNode(value.toString()));
                row.appendChild(node);
            }
        }
        results.appendChild(customer_list);
        results.appendChild(product_list);
       
        results.appendChild(supplier_list);
   
        return document;
    }

    public static String serialize(Document doc) throws IOException {
        StringWriter writer = new StringWriter();
        OutputFormat format = new OutputFormat();
        format.setIndenting(true);

        XMLSerializer serializer = new XMLSerializer(writer, format);
        serializer.serialize(doc);

        return writer.getBuffer().toString();
}
}
