package com.test.main.util;

import com.test.main.config.PropertiesConfig;
import com.test.main.model.Account;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties envProps = PropertiesConfig.getProperties();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, envProps.getProperty("driver", "org.h2.Driver"));

                boolean userServer = Boolean.parseBoolean(envProps.getProperty("use-server", "false"));
                if (userServer) {
                    settings.put(Environment.URL, envProps.getProperty("url", "jdbc:h2:tcp://localhost/~/test"));
                    settings.put(Environment.USER, envProps.getProperty("user", "sa"));
                    settings.put(Environment.PASS, envProps.getProperty("pass", ""));

                } else {
                    settings.put(Environment.URL, "jdbc:h2:./db/repository");
                }
                settings.put(Environment.DIALECT, envProps.getProperty("dialect", "org.hibernate.dialect.H2Dialect"));
                settings.put(Environment.SHOW_SQL, envProps.getProperty("show-sql", "true"));
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, envProps.getProperty("session-context", "thread"));
                settings.put(Environment.HBM2DDL_AUTO, envProps.getProperty("hbm2ddl", "create"));
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Account.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
