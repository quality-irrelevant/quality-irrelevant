package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.models.Episode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EpisodeService {
  private final DatabaseService databaseService;

  public EpisodeService(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

  public Episode find(Long id) {
    ResultSet resultSet = databaseService.select("SELECT id, name, description, published_on, duration, size, number FROM episodes WHERE id = '" + id + "'");
    Episode episode = null;
    try {
      while (resultSet.next()) {
        episode = build(resultSet);
      }
      return episode;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Episode find(String id) {
    return find(Long.parseLong(id));
  }

  public List<Episode> findAll() {
    return findAll(0);
  }

  public List<Episode> findAll(Integer limit) {
    String sql = "SELECT id, name, description, published_on, duration, size, number FROM episodes ORDER BY number DESC";
    if (limit > 0) {
      sql += " LIMIT " + limit;
    }
    ResultSet resultSet = databaseService.select(sql);
    List<Episode> episodes = new ArrayList<>();
    try {
      while (resultSet.next()) {
        Episode episode = build(resultSet);
        episodes.add(episode);
      }
      return episodes;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Episode build(ResultSet resultSet) throws SQLException {
    Episode episode = new Episode();
    episode.setId(resultSet.getLong("id"));
    episode.setName(resultSet.getString("name"));
    episode.setDescription(resultSet.getString("description"));
    episode.setPublishedOn(resultSet.getTimestamp("published_on"));
    episode.setDuration(resultSet.getLong("duration"));
    episode.setSize(resultSet.getLong("size"));
    episode.setNumber(resultSet.getLong("number"));

    return episode;
  }

  public Long create(Episode episode) {
    return databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('" + episode.getName() +
        "', '" + episode.getDescription() + "', datetime('now') || '.000', " + episode.getDuration() + ", " + episode.getSize() + ", " + episode.getNumber() + ")");
  }

  public void update(Episode episode) {
    databaseService.execute("UPDATE episodes SET name = '" + episode.getName() + "', description = '" + episode.getDescription() + "', number = " + episode.getNumber() + " WHERE id = '" + episode.getId() + "'");
  }

  public void delete(Episode episode) {
    databaseService.execute("DELETE FROM episodes WHERE id = '" + episode.getId() + "'");
  }
}
