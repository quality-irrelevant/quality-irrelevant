package com.qualityirrelevant.web.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class FreeMarkerRoute implements Route {
  private final FreeMarkerEngine freeMarkerEngine;
  private final String viewName;

  public FreeMarkerRoute(FreeMarkerEngine freeMarkerEngine, String viewName) {
    this.freeMarkerEngine = freeMarkerEngine;
    this.viewName = viewName;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    ModelAndView modelAndView = run(request, response);
    if (modelAndView == null) {
      return null;
    }
    return freeMarkerEngine.render(modelAndView);
  }

  public ModelAndView run(Request request, Response response) throws Exception {
    Map<String, String> model = new HashMap<>();
    for (Map.Entry<String, String> param : request.params().entrySet()) {
      String key = param.getKey();
      key = key.substring(1);
      model.put(key, param.getValue());
    }
    return new ModelAndView(model, viewName);
  }

  public String getViewName() {
    return viewName;
  }
}