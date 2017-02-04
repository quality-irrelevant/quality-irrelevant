package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.config.ApplicationProperties;
import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.security.Authentication;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class NewEpisode extends FreeMarkerRoute {
  private final ApplicationProperties applicationProperties;
  private final Authentication authentication;

  public NewEpisode(ApplicationProperties applicationProperties, Authentication authentication, FreeMarkerEngine freeMarkerEngine, String viewName) {
    super(freeMarkerEngine, viewName);
    this.applicationProperties = applicationProperties;
    this.authentication = authentication;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    authentication.authenticate(request);

    Map<String, Object> model = new HashMap<>();
    model.put("episode", new Episode(applicationProperties.getBaseUrl()));
    return new ModelAndView(model, getViewName());
  }
}