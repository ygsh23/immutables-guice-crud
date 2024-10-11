package com.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.sql.DataSource;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;

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
    public DataSource provideDataSource() {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/user_db");
            dataSource.setUser("root");
            dataSource.setPassword("11223344");
        } catch (SQLException e) {
            log.error("Failed to configure DataSource", e);
        }
        return dataSource;
    }
}