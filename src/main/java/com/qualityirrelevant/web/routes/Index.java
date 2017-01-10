package com.qualityirrelevant.web.routes;

import com.qualityirrelevant.web.services.EpisodeService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class Index extends FreeMarkerRoute {
  private final EpisodeService episodeService;

  public Index(FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.episodeService = episodeService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Map<String, Object> model = new HashMap<>();
    model.put("episodes", episodeService.findAll(5));
    return new ModelAndView(model, getViewName());
  }
}
