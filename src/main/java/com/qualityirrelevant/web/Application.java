package com.qualityirrelevant.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
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
import com.qualityirrelevant.web.security.UnauthenticatedException;
import com.qualityirrelevant.web.services.DatabaseService;
import com.qualityirrelevant.web.services.EpisodeService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {
  public static String baseDirectory = "";
  public static String baseUrl = "https://qualityirrelevant.com";
  public static Integer port = 4567;
  public static String authorizedIp = "";
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      authorizedIp = args[0];
    }

    if (args.length > 1 && args[1].equals("DEV")) {
      logger.info("Runnin' in DEV environment");

      baseDirectory = "target/";
      baseUrl = "http://localhost:" + port;

      GreenMail greenMail = new GreenMail(ServerSetup.SMTP);
      greenMail.start();
    } else if (args.length > 1 && args[1].equals("PREPROD")) {
      logger.info("Runnin' in PREPROD environment");

      port = 4577;
      baseUrl = "https://dev.qualityirrelevant.com:" + port;
    }

    DatabaseService databaseService = new DatabaseService();
    databaseService.intialize();

    FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
    Configuration freeMarkerConfiguration = new Configuration();
    ClassTemplateLoader templateLoader = new ClassTemplateLoader(Application.class, "/templates");
    freeMarkerConfiguration.setTemplateLoader(templateLoader);
    freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

    new File(baseDirectory + "external/media/episodes").mkdirs();

    if (args.length > 1 && args[1].equals("DEV")) {
      Long id1 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us1', 'The one where Theos head falls off and Phil takes a big bite.', '2010-12-09 22:49:10.000', 54812, 65778909, 1)");
      new File(baseDirectory + "external/media/episodes/" + id1 + ".mp3").createNewFile();
      Long id2 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us2', 'The one where Theos head falls off again', '2011-12-09 22:49:10.000', 54812, 65778909, 5)");
      new File(baseDirectory + "external/media/episodes/" + id2 + ".mp3").createNewFile();
      Long id3 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us3', 'The one where Theos head falls off and Phil takes a big bite.', '2012-12-09 22:49:10.000', 54812, 65778909, 546789)");
      new File(baseDirectory + "external/media/episodes/" + id3 + ".mp3").createNewFile();
      Long id4 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us4', 'The one where Theos head falls off and Phil takes a big bite.', '2013-12-09 22:49:10.000', 54812, 65778909, 2)");
      new File(baseDirectory + "external/media/episodes/" + id4 + ".mp3").createNewFile();
      Long id5 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us5', 'The one where Theos head falls off and Phil takes a big bite.', '2014-12-09 22:49:10.000', 54813, 65778909, 13)");
      new File(baseDirectory + "external/media/episodes/" + id5 + ".mp3").createNewFile();
      Long id6 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us6', 'The one where Theos head falls off and Phil takes a big bite.', '2015-12-09 22:49:10.000', 54812, 65778909, 4)");
      new File(baseDirectory + "external/media/episodes/" + id6 + ".mp3").createNewFile();
      Long id7 = databaseService.insert("INSERT INTO episodes (name, description, published_on, duration, size, number) VALUES ('Fuck Dumps R Us7', 'The one where Theos head falls off and Phil takes a big bite.', '2016-12-09 22:49:10.000', 54812, 65778909, 8)");
      new File(baseDirectory + "external/media/episodes/" + id7 + ".mp3").createNewFile();
    }

    EpisodeService episodeService = new EpisodeService(databaseService);

    Spark.port(port);
    Spark.staticFiles.location("/static");
    Spark.staticFiles.externalLocation(baseDirectory + "external");

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

    get("/", new Index(freeMarkerEngine, episodeService, "index.ftl"));
    get("/art", new FreeMarkerRoute(freeMarkerEngine, "art.ftl"));
    get("/contact", new GetContact(freeMarkerEngine, "contact.ftl"));
    post("/contact", new PostContact(freeMarkerEngine, "contact.ftl"));
    get("/contact/success", new FreeMarkerRoute(freeMarkerEngine, "contact-success.ftl"));

    get("/episodes", new IndexEpisode(freeMarkerEngine, episodeService, "episodes/index.ftl"));
    get("/rss.xml", new RssEpisode(freeMarkerEngine, episodeService, "rss.ftl"));
    get("/episodes/new", new NewEpisode(freeMarkerEngine, "episodes/new.ftl"));
    post("/episodes", new CreateEpisode(freeMarkerEngine, episodeService, "episodes/new.ftl"));
    get("/episodes/:id/edit", new EditEpisode(freeMarkerEngine, episodeService, "episodes/edit.ftl"));
    post("/episodes/:id/delete", new DeleteEpisode(episodeService));
    post("/episodes/:id", new UpdateEpisode(freeMarkerEngine, episodeService, "episodes/edit.ftl"));
    get("/episodes/:id", new ShowEpisode(freeMarkerEngine, episodeService, "episodes/show.ftl"));

  }
}
