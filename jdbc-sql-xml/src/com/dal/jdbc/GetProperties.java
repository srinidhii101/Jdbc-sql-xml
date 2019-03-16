package com.dal.jdbc;

import java.io.IOException;
import java.util.Properties;

public class GetProperties {
    Properties properties = new Properties();

    String configFileName = "application.conf";

    String configValues = getClass().getClassLoader().getResource("database").getContent().toString();

    public GetProperties() throws IOException {
        System.out.println(configValues);
    }
}
