package com.qualityirrelevant.web.routes.episodes;

import com.mpatric.mp3agic.Mp3File;
import com.qualityirrelevant.web.Application;
import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.services.EpisodeService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CreateEpisode extends FreeMarkerRoute {
  private final EpisodeService episodeService;

  public CreateEpisode(FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, String viewName) {
    super(freeMarkerEngine, viewName);
    this.episodeService = episodeService;
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    Authentication.authenticate(request, response);

    MultipartConfigElement configElement = new MultipartConfigElement(System.getenv("java.io.tmpdir"));
    request.attribute("org.eclipse.jetty.multipartConfig", configElement);

    File uploadDirectory = new File(Application.baseDirectory + "external/media/episodes");
    Path tempFile = Files.createTempFile(uploadDirectory.toPath(), "", "");
    InputStream file = request.raw().getPart("file").getInputStream();
    Files.copy(file, tempFile, StandardCopyOption.REPLACE_EXISTING);

    Mp3File mp3File = new Mp3File(tempFile.toFile());

    Episode episode = new Episode();
    episode.setName(multipartParam("name", request));
    episode.setDescription(multipartParam("description", request));
    episode.setDuration(mp3File.getLengthInSeconds());
    episode.setSize(tempFile.toFile().length());
    Long id = episodeService.create(episode);
    Files.move(tempFile, new File(uploadDirectory, id + ".mp3").toPath());

    response.redirect("/episodes/" + id);
    return null;
  }

  private String multipartParam(String name, Request request) {
    try {
      Part part = request.raw().getPart(name);
      if (part.getSize() > 0) {
        return toString(part.getInputStream());
      } else {
        return "";
      }
    } catch (IOException | ServletException e) {
      return "";
    }
  }

  private String toString(InputStream inputStream) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
      result.write(buffer, 0, length);
    }
    return result.toString("UTF-8");
  }
}