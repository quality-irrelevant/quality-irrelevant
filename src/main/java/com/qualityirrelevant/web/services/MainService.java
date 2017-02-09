package com.qualityirrelevant.web.services;

import com.qualityirrelevant.web.email.EmailService;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class MainService implements CommandLineRunner {
  private final Flyway flyway;
  private final Environment env;
  private final EmailService emailService;

  public MainService(Flyway flyway, Environment env, EmailService emailService) {
    this.flyway = flyway;
    this.env = env;
    this.emailService = emailService;
  }

  @Override
  public void run(String... args) throws Exception {
    emailService.start();
    flyway.migrate();

    new File(env.getRequiredProperty("app.base-directory") + "external/media/episodes").mkdirs();

    if (env.acceptsProfiles("DEV")) {
      for (int i = 1; i <= 18; i++) {
        new File(env.getRequiredProperty("app.base-directory") + "external/media/episodes/" + i + ".mp3").createNewFile();
      }
    }
  }
}
