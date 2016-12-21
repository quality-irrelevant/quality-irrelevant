package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.Application;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
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
    if (!request.ip().equals(Application.authorizedIp)) {
      Map<String, String> model = new HashMap<>();
      model.put("errorMessage", "Fuck off, you're not allowed here sonny jim!");
      response.status(403);
      return new ModelAndView(model, "error.ftl");
    }

    Map<String, String> model = new HashMap<>();
    model.put("name", "");
    model.put("description", "");
    return new ModelAndView(model, getViewName());
  }
}