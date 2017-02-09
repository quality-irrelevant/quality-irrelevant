package com.qualityirrelevant.web.controllers;

import com.mpatric.mp3agic.Mp3File;
import com.qualityirrelevant.web.config.ApplicationProperties;
import com.qualityirrelevant.web.models.Episode;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.services.EpisodeService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Controller
public class EpisodeController {
  private final EpisodeService episodeService;
  private final Authentication authentication;
  private final ApplicationProperties applicationProperties;
  private final Environment env;

  public EpisodeController(EpisodeService episodeService, Authentication authentication, ApplicationProperties applicationProperties, Environment env) {
    this.episodeService = episodeService;
    this.authentication = authentication;
    this.applicationProperties = applicationProperties;
    this.env = env;
  }

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("episodes", episodeService.findAll(5));
    return "index";
  }

  @GetMapping("/episodes")
  public String index(Model model, HttpServletRequest request) {
    model.addAttribute("episodes", episodeService.findAll());
    model.addAttribute("authenticated", authentication.isAuthenticated(request));
    return "episodes/index";
  }

  @GetMapping("/rss.xml")
  public String rss(Model model, HttpServletResponse response) {
    response.setContentType("text/xml");
    model.addAttribute("episodes", episodeService.findAll());
    return "rss";
  }

  @GetMapping("/episodes/new")
  public String add(Model model, HttpServletRequest request) {
    authentication.authenticate(request);
    model.addAttribute("episode", new Episode(applicationProperties.getBaseUrl()));
    return "episodes/new";
  }

  @PostMapping("/episodes")
  public String create(HttpServletRequest request) throws Exception {
    authentication.authenticate(request);

    MultipartConfigElement configElement = new MultipartConfigElement(System.getenv("java.io.tmpdir"));
    request.setAttribute("org.eclipse.jetty.multipartConfig", configElement);

    File uploadDirectory = new File(env.getRequiredProperty("app.base-directory") + "external/media/episodes");
    Path tempFile = Files.createTempFile(uploadDirectory.toPath(), "", "");
    InputStream file = request.getPart("file").getInputStream();
    Files.copy(file, tempFile, StandardCopyOption.REPLACE_EXISTING);

    Mp3File mp3File = new Mp3File(tempFile.toFile());

    Episode episode = new Episode(applicationProperties.getBaseUrl());
    episode.setName(multipartParam("name", request));
    episode.setDescription(multipartParam("description", request));
    episode.setDuration(mp3File.getLengthInSeconds());
    episode.setSize(tempFile.toFile().length());
    episode.setNumber(multipartParam("number", request));
    Long id = episodeService.create(episode);
    Files.move(tempFile, new File(uploadDirectory, id + ".mp3").toPath());

    return "redirect:/episodes/" + id;
  }

  @GetMapping("/episodes/:id/edit")
  public String edit(@PathVariable String id, Model model, HttpServletRequest request) {
    authentication.authenticate(request);
    model.addAttribute("episode", episodeService.find(id));
    return "episodes/edit";
  }

  @PostMapping("/episodes/:id/delete")
  public String delete(@PathVariable String id, HttpServletRequest request) throws Exception {
    authentication.authenticate(request);
    File uploadDirectory = new File(env.getRequiredProperty("app.base-directory") + "external/media/episodes");
    Episode episode = episodeService.find(id);
    Files.deleteIfExists(new File(uploadDirectory, id + ".mp3").toPath());
    episodeService.delete(episode);

    return "redirect:/episodes";
  }

  @PostMapping("/episodes/:id")
  public String update(@PathVariable String id, @RequestParam String name,
                       @RequestParam String description, @RequestParam String number,
                       HttpServletRequest request) {
    authentication.authenticate(request);

    Episode episode = episodeService.find(id);

    episode.setName(name);
    episode.setDescription(description);
    episode.setNumber(number);

    episodeService.update(episode);

    return "redirect:/episodes/" + id;
  }

  @GetMapping("/episodes/:id")
  public String show(@PathVariable String id, Model model, HttpServletRequest request) {
    model.addAttribute("episode", episodeService.find(id));
    model.addAttribute("authenticated", authentication.isAuthenticated(request));
    return "episodes/show";
  }

  private String multipartParam(String name, HttpServletRequest request) {
    try {
      Part part = request.getPart(name);
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
