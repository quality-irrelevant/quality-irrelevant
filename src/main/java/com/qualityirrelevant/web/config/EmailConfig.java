package com.qualityirrelevant.web.config;

import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.qualityirrelevant.web.email.EmailService;
import com.qualityirrelevant.web.email.GreenMailEmailService;
import com.qualityirrelevant.web.email.NoOpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.security.Security;

@Configuration
public class EmailConfig {
  private final ApplicationProperties applicationProperties;

  public EmailConfig(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public GreenMail greenMail() {
    Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
    GreenMail greenMail = new GreenMail(ServerSetup.SMTPS);
    greenMail.setUser(applicationProperties.getSmtpUsername(), applicationProperties.getSmtpPassword()).create();
    return greenMail;
  }

  @Bean
  @Profile("!DEV")
  public EmailService noOpEmailService() {
    return new NoOpEmailService();
  }

  @Bean
  @Profile("DEV")
  public EmailService greenMailEmailService() {
    return new GreenMailEmailService(greenMail());
  }
}
