package com.dal.jdbc;

public class JdbcConfig {

    String username;
    String password;
    String database;

    public JdbcConfig() {
        this.username = System.getenv("DAL_MYSQL_USERNAME");
        this.password = System.getenv("DAL_MYSQL_PASSWORD");
        this.database = System.getenv("DAL_MYSQL_DATABASE");
    }
}
