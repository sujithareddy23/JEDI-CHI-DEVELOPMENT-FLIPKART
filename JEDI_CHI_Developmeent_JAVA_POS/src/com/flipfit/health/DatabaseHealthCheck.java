package com.flipfit.health;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.db.DataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHealthCheck extends HealthCheck {

    private final DataSourceFactory dataSourceFactory;

    public DatabaseHealthCheck(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    protected Result check() throws Exception {
        try {
            // Try to get a connection to validate database connectivity
            Connection connection = dataSourceFactory.build(null, "health-check").getConnection();
            if (connection.isValid(5)) {
                connection.close();
                return Result.healthy("Database connection is working");
            } else {
                connection.close();
                return Result.unhealthy("Database connection is not valid");
            }
        } catch (SQLException e) {
            return Result.unhealthy("Cannot connect to database: " + e.getMessage());
        }
    }
}
