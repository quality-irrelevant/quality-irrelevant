package com.qualityirrelevant.web;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
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
import com.qualityirrelevant.web.services.EpisodeService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {
  public static String baseDirectory = "";
  public static String baseUrl = "https://qualityirrelevant.com";
  public static String port = "4567";
  public static String authorizedIp = "";
  public static String smtpUsername = "qualityirrelevant@gmail.com";
  public static String smtpPassword = "";
  public static String smtpHost = "smtp.gmail.com";
  public static String smtpPort = "465";

  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      authorizedIp = args[0];
    }

    if (args.length > 2) {
      smtpPassword = args[2];
    }

    if (args.length > 1 && args[1].equals("DEV")) {
      logger.info("Runnin' in DEV environment");

      baseDirectory = "target/";
      baseUrl = "http://localhost:" + port;
      smtpHost = "localhost";

      Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());

      GreenMail greenMail = new GreenMail(ServerSetup.SMTPS);
      greenMail.setUser(smtpUsername, smtpPassword).create();
      greenMail.start();
    } else if (args.length > 1 && args[1].equals("PREPROD_LOCAL")) {
      logger.info("Runnin' in PREPROD LOCAL environment");

      baseDirectory = "target/";
      baseUrl = "http://localhost:" + port;
    } else if (args.length > 1 && args[1].equals("PREPROD")) {
      logger.info("Runnin' in PREPROD environment");

      port = "4577";
      baseUrl = "https://preprod.qualityirrelevant.com:" + port;
    }

    Class.forName("org.sqlite.JDBC");
    Flyway flyway = new Flyway();
    flyway.setDataSource("jdbc:sqlite:" + Application.baseDirectory + "database.sqlite", null, null);
    flyway.migrate();

    SQLiteDataSource dataSource = new SQLiteDataSource();
    dataSource.setUrl("jdbc:sqlite:" + Application.baseDirectory + "database.sqlite");
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
    Configuration freeMarkerConfiguration = new Configuration();
    ClassTemplateLoader templateLoader = new ClassTemplateLoader(Application.class, "/templates");
    freeMarkerConfiguration.setTemplateLoader(templateLoader);
    freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

    new File(baseDirectory + "external/media/episodes").mkdirs();

    if (args.length > 1 && args[1].equals("DEV")) {
      for (int i = 1; i <= 18; i++) {
        new File(baseDirectory + "external/media/episodes/" + i + ".mp3").createNewFile();
      }
    }

    EpisodeService episodeService = new EpisodeService(jdbcTemplate);

    Spark.port(Integer.parseInt(port));
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
