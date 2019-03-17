package com.dal.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {

    JdbcConfig jdbcConfig;

    public Connection connectionProvider(JdbcConfig jdbcConfig) throws ClassNotFoundException, SQLException {
        this.jdbcConfig = jdbcConfig;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306", this.jdbcConfig.username, this.jdbcConfig.password);
        return connection;
    }
}
