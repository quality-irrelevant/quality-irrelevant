package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.Application;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class NewEpisode extends FreeMarkerRoute {
  private static final Logger logger = LoggerFactory.getLogger(NewEpisode.class);
  public NewEpisode(FreeMarkerEngine freeMarkerEngine, String viewName) {
    super(freeMarkerEngine, viewName);
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    logger.info("Authorised IP:" + Application.authorizedIp);
    logger.info("Current IP:" + request.ip());

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