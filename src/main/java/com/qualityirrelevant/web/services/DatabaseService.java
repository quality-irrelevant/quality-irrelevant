package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.Application;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseService {
  public void intialize() throws Exception {
    Class.forName("org.sqlite.JDBC");

    Flyway flyway = new Flyway();
    flyway.setDataSource(connectionUrl(), null, null);
    flyway.migrate();
  }

  public ResultSet select(String sql) throws Exception {
    Connection connection = DriverManager.getConnection(connectionUrl());
    Statement statement = connection.createStatement();
    return statement.executeQuery(sql);
  }

  public Long insert(String sql) throws Exception {
    Connection connection = DriverManager.getConnection(connectionUrl());

    Statement statement = connection.createStatement();
    statement.executeUpdate(sql);

    ResultSet resultSet = statement.getGeneratedKeys();
    if (resultSet != null && resultSet.next()) {
      return resultSet.getLong(1);
    }
    throw new Exception("Unable to retrieve generated id");
  }

  private String connectionUrl(){
    return "jdbc:sqlite:" + Application.baseDirectory + "database.sqlite";
  }
}
