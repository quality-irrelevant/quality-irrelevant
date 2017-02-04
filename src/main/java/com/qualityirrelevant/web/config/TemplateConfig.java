package com.qualityirrelevant.web.config;

import com.qualityirrelevant.web.Application;
import freemarker.cache.ClassTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spark.template.freemarker.FreeMarkerEngine;

@Configuration
public class TemplateConfig {

  @Bean
  public FreeMarkerEngine freeMarkerEngine() {
    FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine();
    freeMarkerEngine.setConfiguration(configuration());
    return freeMarkerEngine;
  }

  @Bean
  public freemarker.template.Configuration configuration() {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration();
    configuration.setTemplateLoader(classTemplateLoader());
    return configuration;
  }

  @Bean
  public ClassTemplateLoader classTemplateLoader() {
    return new ClassTemplateLoader(Application.class, "/templates");
  }
}