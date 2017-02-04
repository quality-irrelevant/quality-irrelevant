package com.qualityirrelevant.web.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

@Configuration
public class DbConfig {
  private final ApplicationProperties applicationProperties;

  public DbConfig(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public Flyway flyway() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:sqlite:" + applicationProperties.getBaseDirectory() + "database.sqlite", null, null);
    return flyway;
  }

  @Bean
  public SQLiteDataSource sqLiteDataSource() {
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl("jdbc:sqlite:" + applicationProperties.getBaseDirectory() + "database.sqlite");
    return dataSource;
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(sqLiteDataSource());
  }
}