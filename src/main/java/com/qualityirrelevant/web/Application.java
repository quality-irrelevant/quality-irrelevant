package com.qualityirrelevant.web;

import com.qualityirrelevant.web.services.MainService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@Configuration
@ComponentScan
public class Application {

  public static void main(String[] args) throws Exception {
    SimpleCommandLinePropertySource argsPropertySource = new SimpleCommandLinePropertySource(args);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.getEnvironment().getPropertySources().addFirst(argsPropertySource);
    context.register(Application.class);
    context.refresh();

    MainService mainService = context.getBean(MainService.class);
    mainService.run();
  }
}