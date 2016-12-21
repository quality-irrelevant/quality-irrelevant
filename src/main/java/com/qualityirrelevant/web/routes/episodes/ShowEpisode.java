package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.services.DatabaseService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ShowEpisode extends FreeMarkerRoute {
  private final DatabaseService databaseService;

  public ShowEpisode(FreeMarkerEngine freeMarkerEngine, DatabaseService databaseService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.databaseService = databaseService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Long id = Long.parseLong(request.params(":id"));
    ResultSet resultSet = databaseService.select("SELECT name, description, published_on, duration, size FROM episodes WHERE id = '" + id + "'");
    Episode episode = new Episode();
    while (resultSet.next()) {
      episode.setId(id);
      episode.setName(resultSet.getString("name"));
      episode.setDescription(resultSet.getString("description"));
      episode.setPublishedOn(resultSet.getTimestamp("published_on"));
      episode.setDuration(resultSet.getLong("duration"));
      episode.setSize(resultSet.getLong("size"));
    }
    Map<String, Object> model = new HashMap<>();
    model.put("episode", episode);
    return new ModelAndView(model, getViewName());
  }
}