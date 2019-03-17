package com.dal.jdbc;

public class JdbcConfig {

    String username;
    String password;
    String database;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public JdbcConfig() {
    	System.out.println(System.getenv("DAL_MYSQL_USERNAME"));
        this.username = System.getenv("DAL_MYSQL_USERNAME");
        this.password = System.getenv("DAL_MYSQL_PASSWORD");
        this.database = System.getenv("DAL_MYSQL_DATABASE");
    }


}
