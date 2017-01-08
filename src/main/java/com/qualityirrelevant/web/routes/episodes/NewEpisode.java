package com.qualityirrelevant.web.routes.episodes;

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

    Map<String, String> model = new HashMap<>();
    model.put("name", "");
    model.put("description", "");
    return new ModelAndView(model, getViewName());
  }
}