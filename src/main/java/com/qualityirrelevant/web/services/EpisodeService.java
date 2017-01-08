package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.models.Episode;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EpisodeService {
  private final DatabaseService databaseService;

  public EpisodeService(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

  public Episode find(Long id) {
    ResultSet resultSet = databaseService.select("SELECT name, description, published_on, duration, size FROM episodes WHERE id = '" + id + "'");
    Episode episode = new Episode();
    try {
      while (resultSet.next()) {
        episode.setId(id);
        episode.setName(resultSet.getString("name"));
        episode.setDescription(resultSet.getString("description"));
        episode.setPublishedOn(resultSet.getTimestamp("published_on"));
        episode.setDuration(resultSet.getLong("duration"));
        episode.setSize(resultSet.getLong("size"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return episode;
  }

  public Episode find(String id) {
    return find(Long.parseLong(id));
  }
}
