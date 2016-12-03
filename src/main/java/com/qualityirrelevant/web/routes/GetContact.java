package com.qualityirrelevant.web.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

public class GetContact extends FreeMarkerRoute {
  public GetContact(FreeMarkerEngine freeMarkerEngine, String viewName) {
    super(freeMarkerEngine, viewName);
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Map<String, String> model = new HashMap<>();
    model.put("name", "");
    model.put("email", "");
    model.put("message", "");
    model.put("nameError", "");
    model.put("emailError", "");
    model.put("messageError", "");
    return new ModelAndView(model, getViewName());
  }
}