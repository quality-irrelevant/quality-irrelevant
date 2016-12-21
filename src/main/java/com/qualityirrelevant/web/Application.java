package com.qualityirrelevant.web;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.qualityirrelevant.web.routes.FreeMarkerRoute;
import com.qualityirrelevant.web.routes.GetContact;
import com.qualityirrelevant.web.routes.PostContact;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {
  public static String baseDirectory = "";
  public static void main(String[] args) throws Exception {
    if (args.length > 0) {
      authorizedIp = args[0];
    }

    if (args.length > 1 && args[1].equals("DEV")) {
      logger.info("Runnin' in DEV environment");

      baseDirectory = "target/";
      GreenMail greenMail = new GreenMail(ServerSetup.SMTP);
      greenMail.start();
    }

    FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
    Configuration freeMarkerConfiguration = new Configuration();
    ClassTemplateLoader templateLoader = new ClassTemplateLoader(Application.class, "/templates");
    freeMarkerConfiguration.setTemplateLoader(templateLoader);
    freeMarkerEngine.setConfiguration(freeMarkerConfiguration);

    Spark.staticFiles.location("/static");

    get("/", new FreeMarkerRoute(freeMarkerEngine, "index.ftl"));
    get("/art", new FreeMarkerRoute(freeMarkerEngine, "art.ftl"));
    get("/contact", new GetContact(freeMarkerEngine, "contact.ftl"));
    post("/contact", new PostContact(freeMarkerEngine, "contact.ftl"));
    get("/contact/success", new FreeMarkerRoute(freeMarkerEngine, "contact-success.ftl"));
    get("/episodes", new FreeMarkerRoute(freeMarkerEngine, "episodes.ftl"));
    get("/episodes/:id", new FreeMarkerRoute(freeMarkerEngine, "episode.ftl"));
  }
}
