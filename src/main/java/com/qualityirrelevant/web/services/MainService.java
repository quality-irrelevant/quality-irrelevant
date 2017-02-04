package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.Application;
import com.qualityirrelevant.web.config.ApplicationProperties;
import com.qualityirrelevant.web.email.EmailService;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.routes.GetContact;
import com.qualityirrelevant.web.routes.Index;
import com.qualityirrelevant.web.routes.PostContact;
import com.qualityirrelevant.web.routes.episodes.CreateEpisode;
import com.qualityirrelevant.web.routes.episodes.DeleteEpisode;
import com.qualityirrelevant.web.routes.episodes.EditEpisode;
import com.qualityirrelevant.web.routes.episodes.IndexEpisode;
import com.qualityirrelevant.web.routes.episodes.NewEpisode;
import com.qualityirrelevant.web.routes.episodes.RssEpisode;
import com.qualityirrelevant.web.routes.episodes.ShowEpisode;
import com.qualityirrelevant.web.routes.episodes.UpdateEpisode;
import com.qualityirrelevant.web.security.Authentication;
import com.qualityirrelevant.web.security.UnauthenticatedException;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

@Component
public class MainService {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private final ApplicationProperties applicationProperties;
  private final Authentication authentication;
  private final Flyway flyway;
  private final FreeMarkerEngine freeMarkerEngine;
  private final EpisodeService episodeService;
  private final Environment env;
  private final EmailService emailService;

  public MainService(ApplicationProperties applicationProperties, Authentication authentication, Flyway flyway, FreeMarkerEngine freeMarkerEngine, EpisodeService episodeService, Environment env, EmailService emailService) {
    this.applicationProperties = applicationProperties;
    this.authentication = authentication;
    this.flyway = flyway;
    this.freeMarkerEngine = freeMarkerEngine;
    this.episodeService = episodeService;
    this.env = env;
    this.emailService = emailService;
  }

  private void configureSpark() {
    Spark.port(Integer.parseInt(applicationProperties.getPort()));
    Spark.staticFiles.location("/static");
    Spark.staticFiles.externalLocation(applicationProperties.getBaseDirectory() + "external");

    Spark.exception(UnauthenticatedException.class, (exception, request, response) -> {
      Map<String, String> model = new HashMap<>();
      model.put("errorMessage", exception.getMessage());
      response.status(403);
      ModelAndView modelAndView = new ModelAndView(model, "error.ftl");
      response.body(freeMarkerEngine.render(modelAndView));
    });

    Spark.exception(Exception.class, (exception, request, response) -> {
      logger.error("error encountered", exception);
    });
  }

  public void run() throws Exception {
    logger.info("Runnin' in " + Arrays.toString(env.getActiveProfiles()) + " environment(s)");
    emailService.start();
    flyway.migrate();

    new File(applicationProperties.getBaseDirectory() + "external/media/episodes").mkdirs();

    if (env.acceptsProfiles("DEV")) {
      for (int i = 1; i <= 18; i++) {
        new File(applicationProperties.getBaseDirectory() + "external/media/episodes/" + i + ".mp3").createNewFile();
      }
    }

    configureSpark();

    get("/", new Index(freeMarkerEngine, episodeService, "index.ftl"));
    get("/art", new FreeMarkerRoute(freeMarkerEngine, "art.ftl"));
    get("/contact", new GetContact(freeMarkerEngine, "contact.ftl"));
    post("/contact", new PostContact(applicationProperties, freeMarkerEngine, "contact.ftl"));
    get("/contact/success", new FreeMarkerRoute(freeMarkerEngine, "contact-success.ftl"));

    get("/episodes", new IndexEpisode(authentication, freeMarkerEngine, episodeService, "episodes/index.ftl"));
    get("/rss.xml", new RssEpisode(freeMarkerEngine, episodeService, "rss.ftl"));
    get("/episodes/new", new NewEpisode(applicationProperties, authentication, freeMarkerEngine, "episodes/new.ftl"));
    post("/episodes", new CreateEpisode(applicationProperties, authentication, freeMarkerEngine, episodeService, "episodes/new.ftl"));
    get("/episodes/:id/edit", new EditEpisode(authentication, freeMarkerEngine, episodeService, "episodes/edit.ftl"));
    post("/episodes/:id/delete", new DeleteEpisode(applicationProperties, authentication, episodeService));
    post("/episodes/:id", new UpdateEpisode(authentication, freeMarkerEngine, episodeService, "episodes/edit.ftl"));
    get("/episodes/:id", new ShowEpisode(authentication, freeMarkerEngine, episodeService, "episodes/show.ftl"));
  }

}
