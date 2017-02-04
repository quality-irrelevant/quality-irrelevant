package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.services.EpisodeService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

public class UpdateEpisode extends FreeMarkerRoute {
  private final EpisodeService episodeService;
  private final Authentication authentication;

  public UpdateEpisode(Authentication authentication, FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.authentication = authentication;
    this.episodeService = episodeService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    authentication.authenticate(request);
    String id = request.params(":id");

    Episode episode = episodeService.find(id);

    episode.setName(request.queryParams("name"));
    episode.setDescription(request.queryParams("description"));
    episode.setNumber(request.queryParams("number"));

    episodeService.update(episode);
    response.redirect("/episodes/" + id);
    return null;
  }
}
