package com.flipfit;

import com.flipfit.configuration.FlipFitConfiguration;
import com.flipfit.health.DatabaseHealthCheck;
import com.flipfit.resources.GymResource;
import com.flipfit.resources.UserResource;
import com.flipfit.resources.BookingResource;
import com.flipfit.resources.SlotResource;
import com.flipfit.resources.NotificationResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;

public class FlipFitApplication extends Application<FlipFitConfiguration> {

    public static void main(String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    // Hibernate Bundle for database operations
    private final HibernateBundle<FlipFitConfiguration> hibernateBundle = new HibernateBundle<FlipFitConfiguration>(
            com.flipfit.bean.GymAdmin.class,
            com.flipfit.bean.GymOwner.class,
            com.flipfit.bean.GymCustomer.class,
            com.flipfit.bean.GymCenter.class,
            com.flipfit.bean.Slot.class,
            com.flipfit.bean.Booking.class,
            com.flipfit.bean.Notification.class
    ) {
        @Override
        public DataSourceFactory getDataSourceFactory(FlipFitConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    // Migrations Bundle for database migrations
    private final MigrationsBundle<FlipFitConfiguration> migrationsBundle = new MigrationsBundle<FlipFitConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(FlipFitConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "FlipFit Gym Management System";
    }

    @Override
    public void initialize(Bootstrap<FlipFitConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(migrationsBundle);
    }

    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) throws Exception {
        // Get SessionFactory
        final SessionFactory sessionFactory = hibernateBundle.getSessionFactory();

        // Register Resources
        environment.jersey().register(new UserResource(sessionFactory));
        environment.jersey().register(new GymResource(sessionFactory));
        environment.jersey().register(new BookingResource(sessionFactory));
        environment.jersey().register(new SlotResource(sessionFactory));
        environment.jersey().register(new NotificationResource(sessionFactory));

        // Register Health Checks
        environment.healthChecks().register("database", new DatabaseHealthCheck(configuration.getDataSourceFactory()));
    }
}
