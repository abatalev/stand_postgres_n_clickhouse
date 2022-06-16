package com.abatalev.pgnch.archiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.main")
    public DataSourceProperties mainDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mainDataSource")
    public DataSource mainDataSource() {
        return mainDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name ="main")
    @Autowired
    public JdbcTemplate mainJdbcTemplate(@Qualifier("mainDataSource") DataSource mainDataSource) {
        return new JdbcTemplate(mainDataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.archive")
    public DataSourceProperties archiveDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "archiveDataSource")
    public DataSource archiveDataSource() {
        return archiveDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name="archive")
    @Autowired
    public JdbcTemplate archiveJdbcTemplate(@Qualifier("archiveDataSource") DataSource archiveDataSource){
        return new JdbcTemplate(archiveDataSource);
    }
}
