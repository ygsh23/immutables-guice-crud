package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.sql.DataSource;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(UserModule.class);

    @Override
    protected void configure() {
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(UserResource.class);
    }

    @Provides
    public DataSource provideDataSource() throws IOException {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        Properties properties = loadProperties();

        try {
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUser(properties.getProperty("db.user"));
            dataSource.setPassword(properties.getProperty("db.password"));
        } catch (SQLException e) {
            log.error("Failed to configure DataSource", e);
        }
        return dataSource;
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                log.error("Sorry, unable to find application.properties");
                return properties;
            }
            properties.load(input);
        }
        return properties;
    }
}