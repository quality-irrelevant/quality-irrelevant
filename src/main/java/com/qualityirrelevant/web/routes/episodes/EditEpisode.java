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

public class EditEpisode extends FreeMarkerRoute {
  private final EpisodeService episodeService;

  public EditEpisode(FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.episodeService = episodeService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Authentication.authenticate(request);

    String id = request.params(":id");
    Map<String, Object> model = new HashMap<>();
    model.put("episode", episodeService.find(id));
    return new ModelAndView(model, getViewName());
  }
}
