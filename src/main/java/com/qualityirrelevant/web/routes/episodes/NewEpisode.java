package com.qualityirrelevant.web.routes.episodes;

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
  public NewEpisode(FreeMarkerEngine freeMarkerEngine, String viewName) {
    super(freeMarkerEngine, viewName);
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Authentication.authenticate(request);

    Map<String, Object> model = new HashMap<>();
    model.put("episode", new Episode());
    return new ModelAndView(model, getViewName());
  }
}