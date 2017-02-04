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
    applicationProperties.setSmtpPassword(env.getProperty("smtpPassword"));

    if (env.acceptsProfiles("DEV")) {
      applicationProperties.setBaseDirectory("target/");
      applicationProperties.setBaseUrl("http://localhost:" + applicationProperties.getPort());
      applicationProperties.setSmtpHost("localhost");
    } else if (env.acceptsProfiles("PREPROD_LOCAL")) {
      applicationProperties.setBaseDirectory("target/");
      applicationProperties.setBaseUrl("http://localhost:" + applicationProperties.getPort());
    } else if (env.acceptsProfiles("PREPROD")) {
      applicationProperties.setPort("4577");
      applicationProperties.setBaseUrl("https://preprod.qualityirrelevant.com:" + applicationProperties.getPort());
    }
    return applicationProperties;
  }

  @Bean
  public Authentication authentication(ApplicationProperties applicationProperties) {
    return new Authentication(applicationProperties);
  }
}
