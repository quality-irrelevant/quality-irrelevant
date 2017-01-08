package com.qualityirrelevant.web.routes.episodes;

import com.qualityirrelevant.web.Application;
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

  public DeleteEpisode(EpisodeService episodeService) {
    this.episodeService = episodeService;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Authentication.authenticate(request);
    String id = request.params(":id");
    File uploadDirectory = new File(Application.baseDirectory + "external/media/episodes");
    Episode episode = episodeService.find(id);
    Files.deleteIfExists(new File(uploadDirectory, id + ".mp3").toPath());
    episodeService.delete(episode);

    response.redirect("/episodes");
    return null;
  }
}
