package com.es.core.db;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyDriverManagerDataResource extends DriverManagerDataSource {
    private boolean insertSchema;
    private boolean insertDemodata;

    public PropertyDriverManagerDataResource(String path) throws IOException {
        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(path);
        properties.load(in);
        super.setDriverClassName(properties.getProperty("db.driver"));
        super.setUrl(properties.getProperty("db.url"));
        super.setUsername(properties.getProperty("db.user"));
        super.setPassword(properties.getProperty("db.password"));
        insertSchema = Boolean.valueOf(properties.getProperty("db.insertSchema"));
        insertDemodata = Boolean.valueOf(properties.getProperty("db.insertDemodata"));
    }

    public boolean isInsertSchema() {
        return insertSchema;
    }

    public void setInsertSchema(boolean insertSchema) {
        this.insertSchema = insertSchema;
    }

    public boolean isInsertDemodata() {
        return insertDemodata;
    }

    public void setInsertDemodata(boolean insertDemodata) {
        this.insertDemodata = insertDemodata;
    }
}