package com.qualityirrelevant.web.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

@Configuration
public class DbConfig {
  private final Environment env;

  public DbConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public Flyway flyway() throws ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:sqlite:" + env.getRequiredProperty("app.base-directory") + "database.sqlite", null, null);
    return flyway;
  }

  @Bean
  public SQLiteDataSource sqLiteDataSource() {
    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl("jdbc:sqlite:" + env.getRequiredProperty("app.base-directory") + "database.sqlite");
    return dataSource;
  }

  @Bean
  public JdbcTemplate jdbcTemplate() {
    return new JdbcTemplate(sqLiteDataSource());
  }
}