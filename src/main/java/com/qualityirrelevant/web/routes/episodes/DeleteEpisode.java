package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.config.ApplicationProperties;
import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.services.EpisodeService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.nio.file.Files;

public class DeleteEpisode implements Route {
  private final EpisodeService episodeService;
  private final ApplicationProperties applicationProperties;
  private final Authentication authentication;

  public DeleteEpisode(ApplicationProperties applicationProperties, Authentication authentication, EpisodeService episodeService) {
    this.applicationProperties = applicationProperties;
    this.authentication = authentication;
    this.episodeService = episodeService;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    authentication.authenticate(request);
    String id = request.params(":id");
    File uploadDirectory = new File(applicationProperties.getBaseDirectory() + "external/media/episodes");
    Episode episode = episodeService.find(id);
    Files.deleteIfExists(new File(uploadDirectory, id + ".mp3").toPath());
    episodeService.delete(episode);

    response.redirect("/episodes");
    return null;
  }
}
