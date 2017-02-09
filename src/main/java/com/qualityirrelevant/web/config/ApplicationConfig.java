package com.qualityirrelevant.web.config;

import com.qualityirrelevant.web.security.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApplicationConfig {
  private final Environment env;

  public ApplicationConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public ApplicationProperties applicationProperties() {
    ApplicationProperties applicationProperties = new ApplicationProperties();

    applicationProperties.setAuthorizedIp(env.getRequiredProperty("authorizedIp"));
    applicationProperties.setSmtpPassword(env.getProperty("smtpPassword", ""));

    if (env.acceptsProfiles("DEV")) {

      applicationProperties.setBaseUrl("http://localhost:" + env.getRequiredProperty("server.port"));
      applicationProperties.setSmtpHost("localhost");
    } else if (env.acceptsProfiles("PREPROD_LOCAL")) {

      applicationProperties.setBaseUrl("http://localhost:" + env.getRequiredProperty("server.port"));
    } else if (env.acceptsProfiles("PREPROD")) {
      applicationProperties.setBaseUrl("https://preprod.qualityirrelevant.com:" + env.getRequiredProperty("server.port"));
    }
    return applicationProperties;
  }

  @Bean
  public Authentication authentication(ApplicationProperties applicationProperties) {
    return new Authentication(applicationProperties);
  }
}
