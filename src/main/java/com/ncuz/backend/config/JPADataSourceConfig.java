package com.ncuz.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPADataSourceConfig {
    @Autowired
    Environment env;
    @Bean
    public DataSource dataSources() {
        DataSourceBuilder dataSource =   DataSourceBuilder.create();
        System.out.println("url :"+env.getProperty("mysql.db.url"));
//        System.out.println("user :"+env.getProperty("mysql.db.username"));
//        System.out.println("password :"+env.getProperty("mysql.db.password"));
        dataSource.driverClassName(env.getProperty("mysql.db.driver"));
        dataSource.username(env.getProperty("mysql.db.username") );
        dataSource.password(env.getProperty("mysql.db.password") );
        dataSource.url(
                env.getProperty("mysql.db.url")
        );

        return dataSource.build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSources());
        em.setPackagesToScan(new String[] { "com.ncuz" });
        Properties properties = new Properties();

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        properties.setProperty("hibernate.dialect", env.getProperty("mysql.db.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("mysql.db.show.sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("mysql.db.hbm2ddl.auto"));
        em.setJpaProperties(properties);

        return em;
    }
}
