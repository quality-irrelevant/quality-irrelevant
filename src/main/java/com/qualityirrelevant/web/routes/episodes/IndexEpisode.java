package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.services.EpisodeService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class IndexEpisode extends FreeMarkerRoute {
  private final EpisodeService episodeService;
  private final Authentication authentication;

  public IndexEpisode(Authentication authentication, FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.authentication = authentication;
    this.episodeService = episodeService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Map<String, Object> model = new HashMap<>();
    model.put("episodes", episodeService.findAll());
    model.put("authenticated", authentication.isAuthenticated(request));
    return new ModelAndView(model, getViewName());
  }
}
