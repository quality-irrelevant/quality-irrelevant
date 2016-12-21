package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.services.DatabaseService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexEpisode extends FreeMarkerRoute {
  private final DatabaseService databaseService;

  public IndexEpisode(FreeMarkerEngine freeMarkerEngine, DatabaseService databaseService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.databaseService = databaseService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    ResultSet resultSet = databaseService.select("SELECT id, name, description, published_on, duration, size FROM episodes ORDER BY published_on DESC");
    List<Episode> episodes = new ArrayList<>();
    while (resultSet.next()) {
      Episode episode = new Episode();
      episode.setId(resultSet.getLong("id"));
      episode.setName(resultSet.getString("name"));
      episode.setDescription(resultSet.getString("description"));
      episode.setPublishedOn(resultSet.getTimestamp("published_on"));
      episode.setDuration(resultSet.getLong("duration"));
      episode.setSize(resultSet.getLong("size"));
      episodes.add(episode);
    }
    Map<String, Object> model = new HashMap<>();
    model.put("episodes", episodes);
    return new ModelAndView(model, getViewName());
  }
}
