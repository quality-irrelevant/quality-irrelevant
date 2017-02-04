package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.config.ApplicationProperties;
import com.qualityirrelevant.web.models.Episode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EpisodeService {
  private final JdbcTemplate jdbcTemplate;
  private final ApplicationProperties applicationProperties;

  public EpisodeService(ApplicationProperties applicationProperties, JdbcTemplate jdbcTemplate) {
    this.applicationProperties = applicationProperties;
    this.jdbcTemplate = jdbcTemplate;
  }

  private Episode build(ResultSet resultSet, int rowNum) throws SQLException {
    Episode episode = new Episode(applicationProperties.getBaseUrl());
    episode.setId(resultSet.getLong("id"));
    episode.setName(resultSet.getString("name"));
    episode.setDescription(resultSet.getString("description"));
    episode.setPublishedOn(resultSet.getTimestamp("published_on"));
    episode.setDuration(resultSet.getLong("duration"));
    episode.setSize(resultSet.getLong("size"));
    episode.setNumber(resultSet.getLong("number"));

    return episode;
  }

  public Episode find(Long id) {
    String sql = "SELECT id, name, description, published_on, duration, size, number FROM episodes WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, this::build, id);
  }

  public Episode find(String id) {
    return find(Long.parseLong(id));
  }

  public List<Episode> findAll(Integer limit) {
    String sql = "SELECT id, name, description, published_on, duration, size, number FROM episodes ORDER BY number DESC";
    List<Object> arguments = new ArrayList<>();
    if (limit > 0) {
      sql += " LIMIT ?";
      arguments.add(limit);
    }
    return jdbcTemplate.query(sql, this::build, arguments.toArray());
  }

  public List<Episode> findAll() {
    return findAll(0);
  }

  public Long create(Episode episode) {
    SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
    jdbcInsert.usingGeneratedKeyColumns("id");
    jdbcInsert.withTableName("episodes");
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(episode);
    Number id = jdbcInsert.executeAndReturnKey(parameterSource);
    return id.longValue();
  }

  public void update(Episode episode) {
    String sql = "UPDATE episodes SET name = ?, description = ?, number = ? WHERE id = ?";
    jdbcTemplate.update(sql, episode.getName(), episode.getDescription(), episode.getNumber(), episode.getId());
  }

  public void delete(Episode episode) {
    String sql = "DELETE FROM episodes WHERE id = ?";
    jdbcTemplate.update(sql, episode.getId());
  }
}
